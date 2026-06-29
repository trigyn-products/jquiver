import * as vscode from 'vscode';
import { EntityItem } from './entityItem';
import { EntityProvider } from './entityProvider';
import { getData } from './fetchService';


export const saveTemplateMap = new Map<string, {
    entityId: string;
    savedQueryId?: string;
}>();

export const fileRegistry = new Map<string, any>();

export const serverPaths = new Set<string>();

const provider = new EntityProvider();

// ----------------------------
// Helper
// ----------------------------
export function normalizePath(p: string) {
    return p.replace(/\\/g, '/');
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

// ----------------------------
// LOAD TREE
// ----------------------------
export async function loadTree() {
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
    const finalItems = [
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


