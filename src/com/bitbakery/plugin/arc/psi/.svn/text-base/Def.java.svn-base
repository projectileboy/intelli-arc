package com.bitbakery.plugin.arc.psi;

import com.bitbakery.plugin.arc.ArcIcons;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;

import javax.swing.*;

/**
 * PSI element for Arc function ("def") definitions.
 */
public class Def extends VariableAssignment {

    public Def(ASTNode node) {
        super(node);
        ASTNode[] children = node.getChildren(TokenSet.create(ArcElementTypes.VARIABLE_DEFINITION));
        name = isEmpty(children) ? "def" : children[0].getText();
    }

    public Icon getIcon(int flags) {
        return ArcIcons.ARC_DEF_ICON;
    }

    public String getDocstring() {
        ASTNode[] children = getNode().getChildren(TokenSet.create(ArcElementTypes.DOCSTRING));
        if (children != null && children.length > 0) {
            return stripQuotes(children[0].getText());
        }
        return null;
    }
}
