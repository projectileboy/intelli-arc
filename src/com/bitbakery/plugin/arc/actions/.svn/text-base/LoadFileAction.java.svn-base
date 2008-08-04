package com.bitbakery.plugin.arc.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * Event handler for the "Load File" action within an Arc code editor - loads the current Arc file into the current REPL.
 */
public class LoadFileAction extends ArcAction {

    public void actionPerformed(AnActionEvent e) {
        getRepl(e).writeToRepl("(load \"" + getFilePath(e) + "\")");
    }
}
