package com.bitbakery.plugin.arc.structure;

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

import com.bitbakery.plugin.arc.psi.*;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Wraps certain Arc elements to be displayed in the "structure view"
 */
public class ArcStructureViewElement implements StructureViewTreeElement {
    private PsiElement myElement;

    public ArcStructureViewElement(PsiElement element) {
        myElement = element;
    }

    public PsiElement getValue() {
        return myElement;
    }

    public void navigate(boolean requestFocus) {
        ((NavigationItem) myElement).navigate(requestFocus);
    }

    public boolean canNavigate() {
        return ((NavigationItem) myElement).canNavigate();
    }

    public boolean canNavigateToSource() {
        return ((NavigationItem) myElement).canNavigateToSource();
    }

    public StructureViewTreeElement[] getChildren() {
        final List<ArcElement> childrenElements = new ArrayList<ArcElement>();
        myElement.acceptChildren(new ArcElementVisitor() {
            public void visitElement(PsiElement element) {
                if (isBrowsableElement(element)) {
                    childrenElements.add((ArcElement) element);
                } else {
                    element.acceptChildren(this);
                }
            }
        });

        StructureViewTreeElement[] children = new StructureViewTreeElement[childrenElements.size()];
        for (int i = 0; i < children.length; i++) {
            children[i] = new ArcStructureViewElement(childrenElements.get(i));
        }

        return children;
    }

    private boolean isBrowsableElement(PsiElement element) {
        return element instanceof Def
                || element instanceof Mac
                || element instanceof SingleArgFn
                || element instanceof Fn;
    }

    public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            public String getPresentableText() {
                return ((PsiNamedElement) myElement).getName();
            }

            public TextAttributesKey getTextAttributesKey() {
                return null;
            }

            public String getLocationString() {
                return null;
            }

            public Icon getIcon(boolean open) {
                return myElement.getIcon(Iconable.ICON_FLAG_OPEN);
            }
        };
    }
}