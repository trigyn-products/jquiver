import * as vscode from 'vscode';

const fetch = async (...args: [string, any?]) => {
    const mod = await import('node-fetch');
    return mod.default(...args);
};

// ----------------------------
// Helper
// ----------------------------
function normalizePath(p: string) {
    return p.replace(/\\/g, '/');
}

// ----------------------------
// TREE ITEM
// ----------------------------
class EntityItem extends vscode.TreeItem {
    children?: EntityItem[];
    entityId?: string;
    entityInfo?: any;
    module?: string;
    htmlFile?: string;
    selectQueryFile?: string;
    htmlColumn?: string;
    selectQueryColumn?: string;
    masterModuleId?: number;
    businessModuleList?: any[];

    parentInfo?: any;

    savedQuery?: {
        id?: string;
        innerQuery?: string;
    };

    constructor(
        label: string,
        entityId?: string,
        entityInfo?: any,
        children: EntityItem[] = [],
        module?: string
    ) {
        super(
            label,
            children.length
                ? vscode.TreeItemCollapsibleState.Collapsed
                : vscode.TreeItemCollapsibleState.None
        );

        this.label = label;
        this.entityId = entityId;
        this.entityInfo = entityInfo;
        this.children = children;
        this.module = module;

        this.iconPath = new vscode.ThemeIcon(children.length ? 'folder' : 'file');
    }
}

// ----------------------------
// TREE PROVIDER
// ----------------------------
class EntityProvider implements vscode.TreeDataProvider<EntityItem> {

    private _onDidChangeTreeData = new vscode.EventEmitter<EntityItem | undefined>();
    readonly onDidChangeTreeData = this._onDidChangeTreeData.event;

    private data: EntityItem[] = [];

    setData(data: EntityItem[]) {
        this.data = data;
        this._onDidChangeTreeData.fire(undefined);
    }

    getTreeItem(element: EntityItem): vscode.TreeItem {
        return element;
    }

    getChildren(element?: EntityItem): Thenable<EntityItem[]> {
        if (!element) return Promise.resolve(this.data);
        return Promise.resolve(element.children || []);
    }
}

// ----------------------------
// ACTIVATE
// ----------------------------
export async function activate(context: vscode.ExtensionContext) {

    const saveTemplateMap = new Map<string, {
        entityId: string;
        savedQueryId?: string;
    }>();

    const fileRegistry = new Map<string, any>();

    const serverPaths = new Set<string>();

    const provider = new EntityProvider();

    let isLoadingContentFromServer = false;

    // ----------------------------
    // Server Config
    // ----------------------------
    async function readJquiverConfig() {
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
    // LOAD TREE
    // ----------------------------
    async function loadTree() {
        const oldRegistry = new Map(fileRegistry);
        serverPaths.clear();
        const oldPaths = [...fileRegistry.keys()];
        fileRegistry.clear();
        saveTemplateMap.clear();
        const data = await getData();
        if (data === null) {
            return;
        }

        const items = createItems(data);
        const businessItems = createBusinessModuleItems(data);
        const finalItems =[
            ...items,
            ...businessItems
        ];

        provider.setData(finalItems);

        await buildFolderStructure(finalItems);

        for (const [path, item] of fileRegistry) {
            const oldItem = oldRegistry.get(path);

            if (oldItem?.lastUpdated) {
                item.lastUpdated = oldItem.lastUpdated;
            }
        }

        // delete files/folders removed from server
        for (const path of oldPaths) {

            if (!serverPaths.has(path)) {

                try {

                    await vscode.workspace.fs.delete(
                        vscode.Uri.file(path),
                        {
                            recursive: true,
                            useTrash: true
                        }
                    );

                } catch (err) {

                    console.error("Delete failed:", path, err);
                }
            }
        }
    }

    setTimeout(loadTree, 300);

    //Refreshing if something update from server and Id's changed for child
        async function refreshMetadataOnly() {

        const data = await getData();
        if (data === null) return;

        // preserve lastUpdated before clear
        const oldRegistry = new Map(fileRegistry);

        fileRegistry.clear();
        saveTemplateMap.clear();

        const items = createItems(data);
        const businessItems = createBusinessModuleItems(data);

        const finalItems = [
            ...items,
            ...businessItems
        ];

        await buildFolderStructure(finalItems);

        // restore lastUpdated after rebuild
        for (const [path, item] of fileRegistry) {

            const oldItem = oldRegistry.get(path);

            if (oldItem?.lastUpdated) {
                item.lastUpdated = oldItem.lastUpdated;
            }
        }
    }
    //Done refresh ids

    // ----------------------------
    // Fetch Entity
    // ----------------------------
    async function getData(): Promise<any[] | null> {
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
    }catch (err: any) {
        console.error("Server connection failed:", err.message);
        vscode.window.setStatusBarMessage(
            "$(warning) JQuiver server offline",
            5000
        );

        return null;
    }
        
    }

    // ----------------------------
    // Tree building
    // ----------------------------
    function createItems(data: any[]): EntityItem[] {
        return data.map(module => {

            const moduleItem = new EntityItem(
                module.module,
                undefined,
                undefined,
                (module.types || []).map((type: any) =>
                    new EntityItem(
                        type.type,
                        undefined,
                        undefined,
                        (type.folders || []).map((folder: any) =>
                            buildFolder(folder, module.module, module.masterModuleId)
                        ),
                        module.module
                    )
                ),
                module.module
            );
            moduleItem.masterModuleId = module.masterModuleId;

            return moduleItem; 
        });
    }

    //For Business Module Structure
    function createBusinessModuleItems(data: any[]): EntityItem[] {

        const bmMap = new Map<string, EntityItem>();

        for (const module of data) {

            const moduleName = module.module;

            for (const type of (module.types || [])) {

                for (const folder of (type.folders || [])) {

                    const bmList = folder.businessModuleList || [];

                    for (const bm of bmList) {

                        const bmName = bm.name;

                        if (!bmMap.has(bmName)) {

                            bmMap.set(bmName, new EntityItem(
                                bmName,
                                undefined,
                                undefined,
                                [],
                                'BusinessModule'
                            ));
                        }

                        const bmNode = bmMap.get(bmName)!;

                        // MODULE NODE under BM
                        let moduleNode = bmNode.children?.find(c => c.label === moduleName);

                        if (!moduleNode) {
                            moduleNode = new EntityItem(
                                moduleName,
                                undefined,
                                undefined,
                                [],
                                moduleName
                            );
                            bmNode.children!.push(moduleNode);
                        }

                        // ENTITY NODE
                        const entityNode = new EntityItem(
                            folder.folderName,
                            folder.entityInfo?.id,
                            folder.entityInfo,
                            [],
                            moduleName
                        );

                        entityNode.businessModuleList = bmList;

                        entityNode.htmlFile = folder.htmlFile;
                        entityNode.selectQueryFile = folder.selectQueryFile;
                        entityNode.htmlColumn = folder.htmlColumn;
                        entityNode.selectQueryColumn = folder.selectQueryColumn;

                        moduleNode.children!.push(entityNode);
                    }
                }
            }
        }

       const businessRoot = new EntityItem(
            'BusinessModule',
            undefined,
            undefined,
            Array.from(bmMap.values()),
            'BusinessModule'
        );

       return [businessRoot];
    }
    //Done Business Module Structure

    // ----------------------------
    // Folder
    // ----------------------------
    function buildFolder(folder: any, module: string, masterModuleId?: number): EntityItem {

        const children = (folder.folders || []).map((f: any) =>
            buildFolder(f, module)
        );

        const item = new EntityItem(
            folder.folderName,
            folder.entityInfo?.id,
            folder.entityInfo,
            children,
            module
        );
        item.masterModuleId = masterModuleId;
        item.htmlFile = folder.htmlFile;
        item.selectQueryFile = folder.selectQueryFile;
        item.htmlColumn = folder.htmlColumn;
        item.selectQueryColumn = folder.selectQueryColumn;
        item.businessModuleList = folder.businessModuleList || [];

        return item;
    }

    // ----------------------------
    // File Structure Building
    // ----------------------------
    async function buildFolderStructure(items: EntityItem[], basePath: string[] = []) {

        const workspaceRoot = vscode.workspace.workspaceFolders?.[0]?.uri;
        if (!workspaceRoot) return;

        for (const item of items) {

            const currentPath = [...basePath, item.label as string];
            //For template special
            const isTemplateModule = basePath[0] === 'Template';

            // ----------------------------
            // TEMPLATE MODULE
            // ----------------------------
            if (isTemplateModule && item.entityId) {

                const templateBase = basePath[1] || 'unknown';

                const templateUri = vscode.Uri.joinPath(
                    workspaceRoot,
                    'src',
                    'Template',
                    templateBase
                );

                await vscode.workspace.fs.createDirectory(templateUri);

                const fileUri = vscode.Uri.joinPath(
                    templateUri,
                    `${item.label}.html`
                );

                try {
                        await vscode.workspace.fs.stat(fileUri);
                    } catch {
                        await vscode.workspace.fs.writeFile(
                            fileUri,
                            Buffer.from("", "utf8")
                        );
                    }

                const normalized = normalizePath(fileUri.fsPath);
                
                //for refresh case
                serverPaths.add(normalized);

                fileRegistry.set(normalized, item);

                if (item.entityId) {
                    saveTemplateMap.set(normalized, { entityId: item.entityId });
                }

                continue;
            }
            //Done template special case structure

            const currentUri = vscode.Uri.joinPath(workspaceRoot, 'src', ...currentPath);

            //For delete/update path which is updated on server on refresh
            serverPaths.add(normalizePath(currentUri.fsPath));

            if (item.children?.length) {
                await vscode.workspace.fs.createDirectory(currentUri);
                await buildFolderStructure(item.children, currentPath);
                continue;
            }

            if (!item.entityId) continue;

            const entityInfo = item.entityInfo;
            if (!entityInfo) continue;

            await vscode.workspace.fs.createDirectory(currentUri);

            const normalizedParent = normalizePath(currentUri.fsPath);

            //Parent 
            const existingParent = fileRegistry.get(normalizedParent);
            fileRegistry.set(normalizedParent, {
                entityId: item.entityId,
                module: item.module,
                masterModuleId: item.masterModuleId,
                entityInfo: item.entityInfo,
                parentInfo: item.entityInfo,
                lastUpdated: existingParent?.lastUpdated || '',
                htmlColumn: item.htmlColumn,
                selectQueryColumn: item.selectQueryColumn
            });

            const files: string[] = [];

            if (entityInfo.name) files.push(entityInfo.name);
            if (item.htmlFile) files.push(item.htmlFile);
            if (item.selectQueryFile) files.push(item.selectQueryFile);

            //Child Saved Query
            if (Array.isArray(entityInfo.savedQueries)) {
                for (const q of entityInfo.savedQueries) {

                    const name = q?.fileName || q?.variableName || q?.name;
                    if (!name) continue;

                    const fileUri = vscode.Uri.joinPath(currentUri, name);

                    try {
                        await vscode.workspace.fs.stat(fileUri);
                    } catch {
                        await vscode.workspace.fs.writeFile(
                            fileUri,
                            Buffer.from("", "utf8")
                        );
                    }

                    const normalized = normalizePath(fileUri.fsPath);
                    serverPaths.add(normalized);

                    const existingChild = fileRegistry.get(normalized);

                    fileRegistry.set(normalized, {
                        entityId: item.entityId,
                        module: item.module,
                        masterModuleId: item.masterModuleId,
                        parentInfo: item.entityInfo,
                        htmlColumn: item.htmlColumn,
                        lastUpdated: existingChild?.lastUpdated || '',
                        selectQueryColumn: item.selectQueryColumn,
                        savedQuery: {
                            id: q.id,
                            innerQuery: q.innerQuery,
                            columnName: q.columnName
                        }
                    });

                    saveTemplateMap.set(normalized, {
                        entityId: item.entityId,
                        savedQueryId: q.id
                    });
                }
            }

            // ----------------------------
            // File Structure
            // ----------------------------
            for (const fileName of files) {
                const fileUri = vscode.Uri.joinPath(currentUri, fileName);
                try {
                    await vscode.workspace.fs.stat(fileUri);
                } catch {
                    await vscode.workspace.fs.writeFile(
                        fileUri,
                        Buffer.from("", "utf8")
                    );
                }
                const normalized = normalizePath(fileUri.fsPath);
                serverPaths.add(normalized);

                if (!fileRegistry.has(normalized)) {
                    const existingFile = fileRegistry.get(normalized);
                    fileRegistry.set(normalized, {
                        entityId: item.entityId,
                        module: item.module,
                        masterModuleId: item.masterModuleId,
                        parentInfo: item.entityInfo,
                        lastUpdated: existingFile?.lastUpdated || '',
                        htmlColumn: item.htmlColumn,
                        selectQueryColumn: item.selectQueryColumn,
                    });
                }
                saveTemplateMap.set(normalized, {
                    entityId: item.entityId
                });
            }
        }
    }

    //Check Updated Date
    async function checkUpdatedDate(
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


    // ----------------------------
    // File click get event
    // ----------------------------
    context.subscriptions.push(
        vscode.window.onDidChangeActiveTextEditor(async (editor) => {
         
            if (!editor) return;

        const filePath = normalizePath(editor.document.uri.fsPath);
        let item = fileRegistry.get(filePath);

            if (!item?.entityId) return;
            
            const config = await readJquiverConfig();
            if (!config?.server) return;

            // CHECK if server changed if lastUpdated exist for the file
            if (item.lastUpdated) {
            const changed = await checkUpdatedDate(
                item,
                config,
                filePath
            );

            // no change on server return
            if (!changed) {
                return;
            }
        }
//            Below method was used to avoid child data id changed from server
//            await refreshMetadataOnly();

            item = fileRegistry.get(filePath);

            if (!item?.entityId) {
                return;
            }

            // DIRTY FILE CASE
            if (editor.document.isDirty) {

                const choice = await vscode.window.showWarningMessage(
                    'This file has unsaved changes. Override with latest server content?',
                    'Yes',
                    'No'
                );

                if (choice !== 'Yes') {
                    return;
                }
            }

            await fetchLatestContent(
                editor,
                item,
                config,
                filePath
            );
  
        })
    );

    //Extracted GED Method
    async function fetchLatestContent(
        editor: vscode.TextEditor,
        item: any,
        config: any,
        filePath: string
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

            //For file bin case
            if (item.savedQuery?.columnName) {
                params.set('columnName', item.savedQuery.columnName);
            }
            //normal case
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
                headers: { 'Content-Type': 'application/x-www-form-urlencoded',
                    't': `${config.token}`
                 },
                body: params.toString()
            });

            if (!res.ok) return;

            let content = "";
            let lastUpdated = "";

            const raw = await res.text();

            try {
                const json = JSON.parse(raw);

                if (json.status === 401) {
                    vscode.window.showErrorMessage(json.message || "Unauthorized");

                    content = `// ${json.message || "Unauthorized"}`;
                    lastUpdated = "";
                }
                else if (json.status === 403) {
                    vscode.window.showErrorMessage(json.message || "Forbidden");

                    content = `// ${json.message || "Forbidden"}`;
                    lastUpdated = "";
                } else {
                    content = json.formData || "";
                    lastUpdated = json.lastUpdated ?? "";
                }

            } catch {
                content = raw;
            }

            if (item && lastUpdated) {
                item.lastUpdated = lastUpdated;
            }

            content = content.replace(/\\n/g, '\n');

            const edit = new vscode.WorkspaceEdit();
            edit.replace(
                editor.document.uri,
                new vscode.Range(0, 0, editor.document.lineCount, 0),
                content
            );

            isLoadingContentFromServer = true;

            try {
                await vscode.workspace.applyEdit(edit);
                await editor.document.save();
            } finally {
                isLoadingContentFromServer = false;
            }
    }
    //Done Extracted GED method

    //Save handler
    context.subscriptions.push(
        vscode.workspace.onDidSaveTextDocument(async (doc) => {

            if (isLoadingContentFromServer) {
                return;
            }
            const filePath = normalizePath(doc.uri.fsPath);

            const item = fileRegistry.get(filePath);

            if (!item?.entityId) {
                console.log("No registry entry");
                return;
            }

            const config = await readJquiverConfig();
            if (!config?.server) return;

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
            
            params.set('data', doc.getText());

            //For file bin case
            if (item.savedQuery?.columnName) {
                params.set('columnName', item.savedQuery.columnName);
            }
            //normal case
            if (filePath.endsWith('.html') && item.htmlColumn) {
                params.set('columnName', item.htmlColumn);
            } else if (item.selectQueryColumn) {
                params.set('columnName', item.selectQueryColumn);
            }


            try {
                const res = await fetch(`${config.server}/api/ued`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                        'Accept': 'application/json',
                        't': `${config.token}`
                    },
                    body: params.toString()
                });
  
                const raw = await res.text();
                let json;
                try {
                    json = JSON.parse(raw);
                                        console.log(
                        "UED lastUpdated from server =",
                        json.lastUpdated
                    );
                    item.lastUpdated = json.lastUpdated || new Date().toISOString();
                } catch {
                    vscode.window.showErrorMessage("Invalid response from server");
                    return;
                }

                // Show message based on success
                if (json.success) {
                    vscode.window.showInformationMessage(json.message);
                } else {
                     const choice = await vscode.window.showWarningMessage(
                        json.message,
                        'Fetch Latest',
                        'Cancel'
                    );

                    if (choice === 'Fetch Latest') {

                        const editor =
                            vscode.window.visibleTextEditors.find(
                                e => normalizePath(e.document.uri.fsPath) === filePath
                            );

                        if (editor) {
                            await fetchLatestContent(
                                editor,
                                item,
                                config,
                                filePath
                            );
                        }
                    }
                }

            } catch (err: any) {
                vscode.window.showErrorMessage(err.message);
            }
        })
    );

    //On config save
    context.subscriptions.push(
        vscode.workspace.onDidSaveTextDocument(async (doc) => {

            const filePath = normalizePath(doc.uri.fsPath);
            // Only react to config file
            if (!filePath.endsWith('config.jquiver')) return;

            await loadTree();
            //For refresh time changes
            await autoRefresh();
        })
    );
    //config save refresh done

    //For fetch command
    const fetchApiCommand = vscode.commands.registerCommand(
        'jquiver.fetchApi',
        async () => {
            vscode.window.showInformationMessage("Fetching latest data...");
            try {
                await loadTree();
                vscode.window.showInformationMessage(
                    "Data fetched successfully"
                );
            } catch (err: any) {
                vscode.window.showErrorMessage(
                    `Fetch failed: ${err.message}`
                );
            }
        }
    );

    context.subscriptions.push(fetchApiCommand);
    //Done fetch Command


    //For auto refresh
    let refreshInterval: NodeJS.Timeout;
    async function autoRefresh() {
        const config = await readJquiverConfig();
        let refreshMinutes = Number(config?.autoRefreshInMinutes);

        if (!Number.isFinite(refreshMinutes) || refreshMinutes < 1 || refreshMinutes > 30) {
            refreshMinutes = 5;
        }
        if (refreshInterval) {
            clearInterval(refreshInterval);
        }

        refreshInterval = setInterval(async () => {
            try {
                await loadTree();
            } catch (err) {
                console.error("Auto refresh failed:", err);
            }
        }, refreshMinutes * 60 * 1000);
    }
    await autoRefresh();
    //Refresh Done

    //For Preventing Rename
    context.subscriptions.push(
        vscode.workspace.onDidRenameFiles(async (event) => {
            for (const file of event.files) {
                const oldUri = file.oldUri;
                const newUri = file.newUri;
                // Revert rename
                try {
                    await vscode.workspace.fs.rename(newUri, oldUri, {
                        overwrite: true
                    });

                    vscode.window.showWarningMessage(
                        "Renaming file is not allowed"
                    );

                } catch (err) {
                    console.error("Failed to revert rename:", err);
                }
            }
        })
    );
    //Preventing rename done


    //Refresh Icon at Bottom................
    const refreshBtn = vscode.window.createStatusBarItem(
    vscode.StatusBarAlignment.Left
    );

    refreshBtn.text = "$(refresh) Reload JQuiver Workspace";
    refreshBtn.tooltip = "Reload JQuiver Workspace";
    refreshBtn.command = "jquiver.fetchApi";

    refreshBtn.show();

    context.subscriptions.push(refreshBtn);
    //Refresh Done.

    }

export function deactivate() { }