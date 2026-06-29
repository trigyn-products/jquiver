import * as vscode from 'vscode';
// ----------------------------
// TREE ITEM
// ----------------------------
export class EntityItem extends vscode.TreeItem {
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