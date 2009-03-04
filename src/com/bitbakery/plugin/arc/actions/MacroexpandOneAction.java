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

import com.bitbakery.plugin.arc.psi.Mac;
import com.bitbakery.plugin.arc.repl.ReplToolWindow;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

/**
 * Event handler for the "Macroexpand-1" action within a Clojure code editor
 */
public class MacroexpandOneAction extends ArcAction {

    public void actionPerformed(final AnActionEvent e) {

        final Editor ed = e.getData(DataKeys.EDITOR);
        if (ed == null)
            return;

        Mac mac = getCurrentMac(ed, e);
        if (mac != null) {
            ReplToolWindow repl = getReplToolWindow(e);
            if (repl != null) {
                repl.writeToCurrentRepl(mac.getText(), false);

                // TODO - We should also try to load any other functions or macros that we depend on...?

                String macex = collectMacroexpansions(e, mac);
                if (!StringUtil.isEmptyOrSpaces(macex)) {
                    new MacroexpandDialog(macex)
                            .showDialog(this, getCaretPosition(ed));
                }
            }
        }
    }


    private String collectMacroexpansions(AnActionEvent e, Mac mac) {
        StringBuffer macroexpansions = new StringBuffer();
        int paramCount = mac.getParameterCount();

        String macex = getReplToolWindow(e).writeToCurrentRepl(
                "(macex1 '(" + mac.getName() + " " +
                        generateDummyParams(paramCount) + "))", false);

        if (macex != null) {
            macroexpansions.append(macex);
            macroexpansions.append("\r\n");
        }

        return macroexpansions.toString();
    }

    private StringBuffer generateDummyParams(int paramCount) {
        StringBuffer params = new StringBuffer();

        for (int j = 0; j < paramCount; j++) {
            params.append("\"param").append(j).append("\" ");
        }
        return params;
    }

    private Mac getCurrentMac(Editor ed, AnActionEvent e) {
        final PsiFile psiFile = e.getData(DataKeys.PSI_FILE);
        if (psiFile == null)
            return null;

        int offset = ed.getCaretModel().getOffset();
        PsiElement el = psiFile.findElementAt(offset);
        if (el == null)
            return null;

        while (!(el instanceof Mac)) {
            el = el.getParent();
            if (el == null) {
                return null;
            }
        }
        return (Mac) el;
    }
}