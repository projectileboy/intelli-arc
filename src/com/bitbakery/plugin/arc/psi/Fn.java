package com.bitbakery.plugin.arc.psi;

import com.bitbakery.plugin.arc.ArcIcons;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * PSI element for anonymous Arc function ("fn") definitions.
 */
public class Fn extends Expression implements PsiNamedElement {
    public Fn(ASTNode node) {
        super(node);
    }

    public String getName() {
        return "anonymous";
    }

    public String getText() {
        return super.getText();
    }

    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
        // TODO - We can't name an anonymous function!!
        return this;
    }

    public Icon getIcon(int flags) {
        return ArcIcons.ARC_DEF_ICON; // TODO - Need an anonymous def icon
    }
}