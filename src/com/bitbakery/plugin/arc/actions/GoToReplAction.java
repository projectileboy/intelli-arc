package com.bitbakery.plugin.arc.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * Event handler for the "Run Selection" action within an Arc code editor - runs the currently selected text within the current REPL.
 */
public class GoToReplAction extends ArcAction {

    public void actionPerformed(AnActionEvent e) {
        getRepl(e).requestFocus();
    }
}