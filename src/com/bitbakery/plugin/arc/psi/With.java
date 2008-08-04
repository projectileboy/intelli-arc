package com.bitbakery.plugin.arc.psi;

import com.intellij.lang.ASTNode;

/**
 * PSI element for Arc "with" blocks
 */
public class With extends ArcElement {
    public With(ASTNode node) {
        super(node);
    }
}