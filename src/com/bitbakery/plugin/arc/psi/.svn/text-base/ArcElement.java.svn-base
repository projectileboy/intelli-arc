package com.bitbakery.plugin.arc.psi;

import com.bitbakery.plugin.arc.ArcFileType;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Abstract base class for all Arc PSI element classes.
 */
public abstract class ArcElement extends ASTWrapperPsiElement {
    public ArcElement(@NotNull final ASTNode node) {
        super(node);
    }

    @NotNull
    public Language getLanguage() {
        return ArcFileType.ARC;
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        //if (visitor instanceof ArcElementVisitor) {
        //    ((ArcElementVisitor) visitor).visitArcExpression(null);
        //} else {
        super.accept(visitor);
        //}
    }

    @NotNull
    public SearchScope getUseScope() {
        // This is true as long as we have no inter-file references
        return new LocalSearchScope(getContainingFile());
    }

    public <T> T getUserData(Key<T> key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public <T> void putUserData(Key<T> key, T value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Icon getIcon(int flags) {
        return null;
    }
}
