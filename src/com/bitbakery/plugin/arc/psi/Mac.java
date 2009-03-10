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
import static com.bitbakery.plugin.arc.psi.ArcElementTypes.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * PSI element for Arc macro ("mac") definitions.
 */
public class Mac extends Definition {

    public Mac(ASTNode node) {
        super(node, "mac");
        ASTNode[] children = node.getChildren(TokenSet.create(VARIABLE_DEFINITION));
        name = isEmpty(children) ? "mac" : children[0].getText();
    }

    public Icon getIcon(int flags) {
        return ArcIcons.ARC_MAC_ICON;
    }
}

