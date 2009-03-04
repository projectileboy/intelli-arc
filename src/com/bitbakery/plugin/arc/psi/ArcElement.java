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

import com.bitbakery.plugin.arc.ArcFileType;
import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.stubs.StubElement;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Abstract base class for all Arc PSI element classes.
 */
public abstract class ArcElement extends ASTWrapperPsiElement { // <T extends StubElement> extends StubBasedPsiElementBase<T> {
    public ArcElement(@NotNull final ASTNode node) {
        super(node);
    }

    @NotNull
    public Language getLanguage() {
        return ArcFileType.ARC;
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

    protected boolean isEmpty(ASTNode[] children) {
        return children == null || children.length < 1;
    }
}
