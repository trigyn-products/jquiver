import * as vscode from 'vscode';
import { EntityItem } from './entityItem';
// ----------------------------
// TREE PROVIDER
// ----------------------------
export class EntityProvider implements vscode.TreeDataProvider<EntityItem> {

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