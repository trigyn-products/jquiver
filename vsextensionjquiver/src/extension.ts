import * as vscode from 'vscode';
import {
    readJquiverConfig,
    checkUpdatedDate,
    getLatestServerContent
} from './fetchService';

import { loadTree,normalizePath,
    fileRegistry,
    saveTemplateMap } from './treeService';
import { showDiff } from './conflictService';

const fetch = async (...args: [string, any?]) => {
    const mod = await import('node-fetch');
    return mod.default(...args);
};

// ----------------------------
// ACTIVATE
// ----------------------------
export async function activate(context: vscode.ExtensionContext) {

    let isLoadingContentFromServer = false;
    const conflictFiles = new Set<string>();
    const latestServerTimestampMap = new Map<string, string>();
    // ----------------------------
    // File click get event
    // ----------------------------
    context.subscriptions.push(
        vscode.window.onDidChangeActiveTextEditor(async (editor) => {
            if (!editor) return;
            if (editor.document.isUntitled) {
                return;
            }

            if (editor.document.uri.scheme !== 'file') {
                return;
            }

            const filePath = normalizePath(editor.document.uri.fsPath);
            if (conflictFiles.has(filePath)) {
                console.log("Skipping refresh because file has unresolved conflict");
                return;
            }
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

    const result = await getLatestServerContent(
        item,
        config,
        filePath,
        saveTemplateMap
    );

    if (!result) {
        return;
    }

    const { content, lastUpdated } = result;

    if (item && lastUpdated) {
        item.lastUpdated = lastUpdated;
    }

    const edit = new vscode.WorkspaceEdit();

    edit.replace(
        editor.document.uri,
        new vscode.Range(
            0,
            0,
            editor.document.lineCount,
            0
        ),
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

            //in case of conflict viewer save
            const latest =
                latestServerTimestampMap.get(filePath);

            if (latest) {
                item.lastUpdated = latest;
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
                    item.lastUpdated = json.lastUpdated || new Date().toISOString();
                } catch {
                    vscode.window.showErrorMessage("Invalid response from server");
                    return;
                }

                // Show message based on success
                if (json.success) {
                    latestServerTimestampMap.delete(filePath);
                    conflictFiles.delete(filePath);
                    vscode.window.showInformationMessage(json.message);
                } else {
                    //do not change dirty state until file is saved on server
                    conflictFiles.add(filePath);
                    const choice = await vscode.window.showWarningMessage(
                        json.message,
                        'Show Diff',
                        'Fetch Latest',
                        'Cancel'
                    );

                    if (choice === 'Show Diff') {
                        console.log("inside show diff");

                        const localContent = doc.getText();

                        const result = await getLatestServerContent(
                            item,
                            config,
                            filePath,
                            saveTemplateMap
                        );

                        if (!result) {
                            vscode.window.showErrorMessage(
                                "Failed to fetch latest server content"
                            );
                            return;
                        }

                        latestServerTimestampMap.set(
                            filePath,
                            result.lastUpdated
                        );

                        const editor =
                        vscode.window.visibleTextEditors.find(
                            e => normalizePath(e.document.uri.fsPath) === filePath
                        );

                    if (editor) {
                        await showDiff(
                            editor,
                            result.content
                        );
                    }
                    }
                    else if (choice === 'Fetch Latest') {

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
                        latestServerTimestampMap.delete(filePath);
                        conflictFiles.delete(filePath);
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