package com.bitbakery.plugin.arc.psi;

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
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;

import javax.swing.*;

/**
 * PSI element for Arc function ("def") definitions.
 */
public class Def extends Definition {

    public Def(ASTNode node) {
        super(node, "def");
        ASTNode[] children = node.getChildren(TokenSet.create(ArcElementTypes.VARIABLE_DEFINITION));
        name = isEmpty(children) ? "def" : children[0].getText();
    }

    public Icon getIcon(int flags) {
        return ArcIcons.ARC_DEF_ICON;
    }
}
