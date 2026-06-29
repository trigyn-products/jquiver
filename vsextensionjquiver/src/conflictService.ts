import * as vscode from 'vscode';
import * as os from 'os';
import * as path from 'path';

export async function showDiff(
    editor: vscode.TextEditor,
    serverContent: string
) {

    const tempFile = path.join(
        os.tmpdir(),
        `server-conflict-${Date.now()}.txt`
    );

    const tempUri = vscode.Uri.file(tempFile);

    await vscode.workspace.fs.writeFile(
        tempUri,
        Buffer.from(serverContent, 'utf8')
    );

    await vscode.commands.executeCommand(
        'vscode.diff',
        tempUri,
        editor.document.uri,
        'Server ↔ Local Conflict'
    );
}