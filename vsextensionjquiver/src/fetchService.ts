import * as vscode from 'vscode';

// ----------------------------
// Server Config
// ----------------------------
export async function readJquiverConfig() {
    const workspaceRoot = vscode.workspace.workspaceFolders?.[0]?.uri;
    if (!workspaceRoot) return null;

    const configUri = vscode.Uri.joinPath(workspaceRoot, 'config.jquiver');

    try {
        const fileData = await vscode.workspace.fs.readFile(configUri);
        const text = Buffer.from(fileData).toString('utf8');

        const config = JSON.parse(text);

        return {
            server: config.server.replace(/\/+$/, ''),
            token: config.token,
            autoRefreshInMinutes: config.autoRefreshInMinutes ?? 5
        };
    } catch {
        vscode.window.showErrorMessage("This is not a JQuiver Project");
        return null;
    }
}

// ----------------------------
// Fetch Entity
// ----------------------------
export async function getData(): Promise<any[] | null> {
    const config = await readJquiverConfig();
    if (!config?.server) return [];
    try {
        const res = await fetch(`${config.server}/api/fel`, {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                't': `${config.token}`
            }
        });
        if (!res.ok) return [];


        const raw = await res.text();

        try {
            const json = JSON.parse(raw);

            if (json.status === 401) {
                vscode.window.showErrorMessage(json.message || "Unauthorized");
                return [];
            }

            if (json.status === 403) {
                vscode.window.showErrorMessage(json.message || "Forbidden");
                return [];
            }

            if (json.accessDenied) {
                vscode.window.showWarningMessage(json.message || "Access denied");
                return [];
            }

            return Array.isArray(json)
                ? json
                : Array.isArray(json.data)
                    ? json.data
                    : [];

        } catch {
            vscode.window.showErrorMessage("Invalid response format");
            return [];
        }
    } catch (err: any) {
        console.error("Server connection failed:", err.message);
        vscode.window.setStatusBarMessage(
            "$(warning) JQuiver server offline",
            5000
        );

        return null;
    }

}


//Check Updated Date
export async function checkUpdatedDate(
    item: any,
    config: any,
    filePath: string
): Promise<boolean> {

    const params = new URLSearchParams();

    params.set('entityId', item.entityId);

    if (item.module) {
        params.set('module', item.module);
    }

    if (item.savedQuery?.id) {
        params.set('id', item.savedQuery.id);
    }

    if (item.savedQuery?.innerQuery) {
        params.set('innerQuery', item.savedQuery.innerQuery);
    }

    if (!item.savedQuery?.id && item.parentInfo?.id) {
        params.set('id', item.parentInfo.id);
    }

    if (item.lastUpdated) {
        params.set('updatedDate', item.lastUpdated);
    }

    // file bin case
    if (item.savedQuery?.columnName) {
        params.set('columnName', item.savedQuery.columnName);
    }
    else if (filePath.endsWith('.html') && item.htmlColumn) {
        params.set('columnName', item.htmlColumn);
    }
    else if (item.selectQueryColumn) {
        params.set('columnName', item.selectQueryColumn);
    }

    const res = await fetch(`${config.server}/api/cud`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            't': `${config.token}`
        },
        body: params.toString()
    });

    if (!res.ok) return false;
    try {
        const json: any = await res.json();
        return json.changed === true;
    } catch {
        return true;
    }
}
//Done checking update date

export async function getLatestServerContent(
    item: any,
    config: any,
    filePath: string,
    saveTemplateMap: Map<string, any>
) {

    const meta = saveTemplateMap.get(filePath);

    const params = new URLSearchParams();

    params.set('entityId', item.entityId);

    if (item.module) params.set('module', item.module);

    if (item.savedQuery?.innerQuery) {
        params.set('innerQuery', item.savedQuery.innerQuery);
    }

    if (item.masterModuleId) {
        params.set('masterModuleId', String(item.masterModuleId));
    }

    if (item.savedQuery?.columnName) {
        params.set('columnName', item.savedQuery.columnName);
    }

    if (filePath.endsWith('.html') && item.htmlColumn) {
        params.set('columnName', item.htmlColumn);
    } else if (item.selectQueryColumn) {
        params.set('columnName', item.selectQueryColumn);
    }

    const finalId =
        item.savedQuery?.id ||
        meta?.savedQueryId ||
        item.parentInfo?.id;

    if (finalId) {
        params.set('id', finalId);
    }

    const res = await fetch(`${config.server}/api/ged`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            't': `${config.token}`
        },
        body: params.toString()
    });

    if (!res.ok) {
        return null;
    }

    let content = "";
    let lastUpdated = "";

    const raw = await res.text();

    try {
        const json = JSON.parse(raw);

        if (json.status === 401) {
            throw new Error(json.message || "Unauthorized");
        }

        if (json.status === 403) {
            throw new Error(json.message || "Forbidden");
        }

        content = json.formData || "";
        lastUpdated = json.lastUpdated ?? "";

    } catch {
        content = raw;
    }

    content = content.replace(/\\n/g, '\n');

    return {
        content,
        lastUpdated
    };
}
