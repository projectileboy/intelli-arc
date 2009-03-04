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

import com.bitbakery.plugin.arc.repl.ReplToolWindow;
import com.bitbakery.plugin.arc.psi.ArcElementType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiWhiteSpace;

/**
 * Event handler for the "Run Selection" action within an Arc code editor - runs the currently selected text within the current REPL.
 */
public class RunSelectedTextAction extends ArcAction {

    public void actionPerformed(AnActionEvent e) {
        Editor ed = e.getData(DataKeys.EDITOR);
        if (ed == null) {
            return;
        }
        String text = ed.getSelectionModel().getSelectedText();
        if (StringUtil.isEmptyOrSpaces(text)) {
            int caret = ed.getSelectionModel().getSelectionStart();
            PsiFile file = e.getData(DataKeys.PSI_FILE);
            if (file != null) {
                for (PsiElement el : file.getChildren()) {
                    if (el.getTextRange().contains(caret)) {
                        if (!(el instanceof PsiWhiteSpace) && !(el instanceof PsiComment)) {
                            writeToRepl(e, el.getText());
                        }
                        return;
                    }
                }
            }
        } else {
            writeToRepl(e, text);
        }

    }

    private void writeToRepl(AnActionEvent e, String text) {
        ReplToolWindow repl = getReplToolWindow(e);
        if (repl != null) {
            repl.writeToCurrentRepl(text);
        }
    }
}
