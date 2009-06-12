package com.bitbakery.plugin.arc.config;

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

import static com.bitbakery.plugin.arc.ArcStrings.message;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import static java.awt.Cursor.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ArcConfigPanel {
    private JComponent rootPanel;
    private TextFieldWithBrowseButton arcHomeField;
    private TextFieldWithBrowseButton mzSchemeHomeField;
    private TextFieldWithBrowseButton arcInitializationFileField;
    private JLabel arcUrl;
    private JLabel mzSchemeUrl;
    private JLabel arcTutorialUrl;


    public ArcConfigPanel() {
        String msg = message("plugin.name");

        arcHomeField.addBrowseFolderListener(msg, msg, null, new Chooser(false, true));
        mzSchemeHomeField.addBrowseFolderListener(msg, msg, null, new Chooser(false, true));
        arcInitializationFileField.addBrowseFolderListener(msg, msg, null, new Chooser(false, true));

        enableHyperlink(arcUrl);
        enableHyperlink(mzSchemeUrl);
        enableHyperlink(arcTutorialUrl);
    }

    private void enableHyperlink(final JLabel label) {
        label.addMouseListener(new MouseAdapter() {
            private Color textColor;

            {
                label.setForeground(Color.BLUE);
                textColor = label.getForeground();
            }

            public void mouseEntered(MouseEvent mouseEvent) {
                label.setCursor(getPredefinedCursor(HAND_CURSOR));
                label.setForeground(Color.DARK_GRAY);
            }

            public void mouseExited(MouseEvent mouseEvent) {
                label.setForeground(textColor);
                label.setCursor(getDefaultCursor());
            }

            public void mouseClicked(MouseEvent mouseEvent) {
                BrowserUtil.launchBrowser(label.getText());
            }
        });
    }

    public JComponent getPanel() {
        return rootPanel;
    }

    public void load(@NotNull ArcSettings settings) {
        arcHomeField.setText(settings.arcHome);
        mzSchemeHomeField.setText(settings.mzSchemeHome);
        arcInitializationFileField.setText(settings.arcInitializationFile);
    }

    public boolean isModified(@NotNull ArcSettings settings) {
        return !settings.arcHome.equals(arcHomeField.getText())
                || !settings.mzSchemeHome.equals(mzSchemeHomeField.getText())
                || !settings.arcInitializationFile.equals(arcInitializationFileField.getText());
    }

    public void save(@NotNull ArcSettings settings) {
        settings.arcHome = arcHomeField.getText();
        settings.mzSchemeHome = mzSchemeHomeField.getText();
        settings.arcInitializationFile = arcInitializationFileField.getText();
    }

    private static class Chooser extends FileChooserDescriptor {
        public Chooser(boolean chooseFiles, boolean chooseFolders) {
            super(chooseFiles, chooseFolders, false, false, false, false);
        }
    }
}
