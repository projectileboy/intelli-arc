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
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.VisualPosition;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract base clas for Arc actions. Provides common functionality.
 */
public abstract class ArcAction extends AnAction {

    protected ReplToolWindow getReplToolWindow(AnActionEvent e) {
        Project project = e.getData(DataKeys.PROJECT);
        return (ReplToolWindow) project.getComponent("repl.toolWindow");
    }

    protected String getFilePath(AnActionEvent e) {
        return e.getData(DataKeys.VIRTUAL_FILE).getPath();
    }

    protected Point getCaretPosition(Editor ed) {
        VisualPosition caret = ed.getCaretModel().getVisualPosition();
        Point pt = ed.visualPositionToXY(caret);
        SwingUtilities.convertPointToScreen(pt, ed.getContentComponent());
        return pt;
    }
}
