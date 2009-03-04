package com.bitbakery.plugin.arc.actions;

/*
 * Copyright (c) Kurt Christensen, 2009
 *
 *  Licensed under the Artistic License, Version 2.0 (the "License"); you may not use this
 *  file except in compliance with the License. You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/artistic-license-2.0.php
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under
 *  the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 *  OF ANY KIND, either express or implied. See the License for the specific language
 *  governing permissions and limitations under the License..
 */

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.util.IconLoader;
import com.bitbakery.plugin.arc.repl.ReplToolWindow;

/**
 * Removes the current REPL from the ReplToolWindow.
 */
public class RemoveReplAction extends ArcAction {
    public RemoveReplAction() {
        getTemplatePresentation().setIcon(IconLoader.getIcon("/actions/cancel.png"));
    }

    public void actionPerformed(AnActionEvent e) {
        ReplToolWindow repl = getReplToolWindow(e);
        if (repl != null) {
            repl.removeCurrentRepl();
        }
    }
}