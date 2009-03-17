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

import com.intellij.ide.util.treeView.smartTree.Group;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.ide.util.treeView.smartTree.Filter;
import com.intellij.ide.util.treeView.smartTree.ActionPresentation;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.navigation.ItemPresentation;
import com.bitbakery.plugin.arc.psi.Mac;
import com.bitbakery.plugin.arc.psi.VariableAssignment;
import com.bitbakery.plugin.arc.ArcIcons;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: kurtc
 * Date: Mar 8, 2009
 * Time: 1:11:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class EqFilter implements Filter {
    public boolean isVisible(TreeElement treeNode) {
        if (treeNode instanceof StructureViewTreeElement) {
            StructureViewTreeElement el = (StructureViewTreeElement) treeNode;
            return el.getValue() instanceof VariableAssignment;
        }
        return false;
    }

    public boolean isReverted() {
        return false;
    }

    @NotNull
    public ActionPresentation getPresentation() {
        return new ActionPresentation() {
            public String getText() {
                return "= filter";
            }

            public String getDescription() {
                return "= filter";
            }

            public Icon getIcon() {
                return ArcIcons.ARC_EQ_ICON;
            }
        };
    }

    @NotNull
    public String getName() {
        return "= filter";
    }
}