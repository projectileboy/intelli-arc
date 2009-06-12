package com.bitbakery.plugin.arc.structure;

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

import com.bitbakery.plugin.arc.ArcIcons;
import com.bitbakery.plugin.arc.ArcSyntaxHighlighter;
import com.intellij.ide.util.treeView.smartTree.Group;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Groups macro definitions for the structure view
 */
public class MacGroup implements Group {
    private Collection<TreeElement> macs = new ArrayList<TreeElement>();

    public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            public String getPresentableText() {
                return "Macro definitions";
            }

            public String getLocationString() {
                return null;
            }

            public Icon getIcon(boolean open) {
                return ArcIcons.ARC_MAC_ICON;
            }

            public TextAttributesKey getTextAttributesKey() {
                return ArcSyntaxHighlighter.MAC;
            }
        };
    }

    public Collection<TreeElement> getChildren() {
        return macs;
    }

    protected void add(TreeElement el) {
        macs.add(el);
    }
}