package com.bitbakery.plugin.arc.psi;

import com.intellij.lang.ASTNode;

/**
 * PSI element for Arc "with" blocks
 */
public abstract class If extends ArcElement {
    public If(ASTNode node) {
        super(node);
    }
}