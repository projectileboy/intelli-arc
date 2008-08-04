package com.bitbakery.plugin.arc.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for concrete flavors of S-expressions
 */
public class Expression extends ArcElement {
    public Expression(@NotNull final ASTNode node) {
        super(node);
    }

    protected boolean isEmpty(ASTNode[] children) {
        return children == null || children.length < 1;
    }
}
