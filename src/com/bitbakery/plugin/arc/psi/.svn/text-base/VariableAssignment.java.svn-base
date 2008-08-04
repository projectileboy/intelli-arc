package com.bitbakery.plugin.arc.psi;

import com.bitbakery.plugin.arc.ArcIcons;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * TODO - Describe roles and responsibilities
 */
public class VariableAssignment extends Expression implements PsiNamedElement {
    protected String name;

    public VariableAssignment(ASTNode node) {
        super(node);
        ASTNode[] children = node.getChildren(TokenSet.create(ArcElementTypes.VARIABLE_ASSIGNMENT));
        name = isEmpty(children) ? "=" : children[0].getText();
    }

    public String getName() {
        return name;
    }

    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
        // TODO - Need to actually set the text in the symbol child element that defines the name
        this.name = name;
        return this;
    }

    public Icon getIcon(int flags) {
        return ArcIcons.ARC_DEF_ICON; // TODO - Create an icon for variable assignment
    }

    // TODO - Figure out where this should really live... ArcElement, maybe??
    public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            public String getPresentableText() {
                return getName();
            }

            @Nullable
            public String getLocationString() {
                return getNode().getPsi().getContainingFile().getName();
            }

            @Nullable
            public Icon getIcon(boolean open) {
                return VariableAssignment.this.getIcon(0);
            }

            @Nullable
            public TextAttributesKey getTextAttributesKey() {
                return null;  // TODO - Investigate different fonts/colors/etc.for def vs. mac...?
            }
        };
    }

    /**
     * Strips quote marks from a string - utility method for Def and Mac.
     */
    protected String stripQuotes(String s) {
        s = s.trim();
        if (s.length() > 2) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }
}
