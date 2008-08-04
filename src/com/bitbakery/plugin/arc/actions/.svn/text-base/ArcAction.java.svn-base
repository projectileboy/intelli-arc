package com.bitbakery.plugin.arc.actions;

import com.bitbakery.plugin.arc.repl.ReplToolWindow;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;

/**
 * Abstract base clas for Arc actions. Provides common functionality.
 */
public abstract class ArcAction extends AnAction {

    protected ReplToolWindow getRepl(AnActionEvent e) {
        Project project = e.getData(DataKeys.PROJECT);
        return (ReplToolWindow) project.getComponent("ReplToolWindow.ArcPlugin");
    }

    protected String getFilePath(AnActionEvent e) {
        return e.getData(DataKeys.VIRTUAL_FILE).getPath();
    }
}
