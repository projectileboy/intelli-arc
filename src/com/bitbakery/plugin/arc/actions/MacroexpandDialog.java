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

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

/**
 * Displays the macroexpansion within a simple dialog
 */
public class MacroexpandDialog extends JDialog {
    private JTextArea text;
    private MacroexpandOneAction action;

    public MacroexpandDialog(String macroexpansion) throws HeadlessException {
        setUndecorated(true);
        setAlwaysOnTop(true);
        add(buildTextArea(macroexpansion));
        pack();
    }

    private JTextArea buildTextArea(final String macroexpansion) {
        text = new JTextArea(macroexpansion);
        final Border b = LineBorder.createGrayLineBorder();
        text.setBorder(new Border() {
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                b.paintBorder(c, g, x, y, width, height);
            }

            public Insets getBorderInsets(Component c) {
                return new Insets(3, 5, 3, 5);
            }

            public boolean isBorderOpaque() {
                return b.isBorderOpaque();
            }
        });

        text.setEditable(false);

        text.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent event) {
                close(event);
            }
        });
        text.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                close(event);
            }
        });
        text.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent event) {
                setVisible(false);
                dispose();
            }
        });
        text.requestFocus();
        return text;
    }

    private void close(InputEvent event) {
        setVisible(false);
        event.consume();
        dispose();
    }

    public void showDialog(MacroexpandOneAction action, Point location) {
        this.action = action;
        setLocation(location);
        setVisible(true);
    }
}