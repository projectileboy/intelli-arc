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
import com.bitbakery.plugin.arc.psi.Def;
import com.bitbakery.plugin.arc.psi.Mac;
import com.bitbakery.plugin.arc.psi.VariableAssignment;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.ide.util.treeView.smartTree.ActionPresentation;
import com.intellij.ide.util.treeView.smartTree.Group;
import com.intellij.ide.util.treeView.smartTree.Grouper;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 */
public class DefMacGrouper implements Grouper {

    // TODO - Note that we could have slightly different grouping mechanisms for top-level nodes vs. child def/mac groups
    @NotNull
    public Collection<Group> group(AbstractTreeNode parent, Collection<TreeElement> children) {
        if (parent.getValue() instanceof ArcStructureViewElement) {
            Collection<Group> groups = new ArrayList<Group>();

            DefGroup defs = new DefGroup();
            MacGroup macs = new MacGroup();
            EqGroup eqs = new EqGroup();

            for (TreeElement el : children) {
                if (el instanceof StructureViewTreeElement) {
                    StructureViewTreeElement svel = (StructureViewTreeElement) el;
                    if (svel.getValue() instanceof Def) {
                        defs.add(el);
                    } else if (svel.getValue() instanceof Mac) {
                        macs.add(el);
                    } else if (svel.getValue() instanceof VariableAssignment) {
                        eqs.add(el);
                    }
                }
            }

            groups.add(defs);
            groups.add(macs);
            groups.add(eqs);

            return groups;
        }
        return new ArrayList<Group>();
    }

    @NotNull
    public ActionPresentation getPresentation() {
        return new ActionPresentation() {
            public String getText() {
                return "def/mac/= grouper";
            }

            public String getDescription() {
                return "def/mac/= grouper";
            }

            public Icon getIcon() {
                return ArcIcons.ARC_FILE_ICON;
            }
        };
    }

    @NotNull
    public String getName() {
        return "def/mac/= grouper";
    }
}