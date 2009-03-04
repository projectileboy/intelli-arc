package com.bitbakery.plugin.arc;

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
import com.bitbakery.plugin.arc.lexer.ArcTokenTypes;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.TokenSet;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.HighlighterColors;

import java.awt.*;

/**
 * The annotator can identify errors in how a function or macro is being called
 */
public class ArcAnnotator implements Annotator {
    public void annotate(PsiElement psiElement, AnnotationHolder holder) {
        if (psiElement instanceof VariableReference) {
            VariableReference ref = (VariableReference) psiElement;
            PsiReference r = ref.getReference();
            if (r != null) {
                PsiElement expr = ref.getParent();
                if (expr != null) {
                    PsiElement el = r.resolve();
                    if (el instanceof Definition) {
                        // Here's a little hack to determine the actual param count - number of expression children, minus the fn/mac name
                        int actualParamCount = expr.getChildren().length - 1;

                        Definition def = (Definition) el;
                        if (!def.isValidParameterCount(actualParamCount)) {
                            int min = def.getMinParameterCount();
                            int max = def.getMaxParameterCount();
                            holder.createWarningAnnotation(expr, getParamCountErrorMessage(min, max));
                        }
                    }
                }
            }
        } else if (psiElement instanceof Docstring) {
            Annotation a = holder.createInfoAnnotation(psiElement, null);
            a.setTextAttributes(ArcSyntaxHighlighter.getTextAttrs(ArcElementTypes.DOCSTRING));
        }
    }

    private String getParamCountErrorMessage(int min, int max) {
        if (max == Integer.MAX_VALUE) {
            return "Expected " + min + " or more parameters";
        } else if (min < max) {
            return "Expected between " + min + " and " + max + " parameter" + pluralize(max); // TODO - i18n me
        }
        return "Expected " + min + " parameter" + pluralize(min); // TODO - i18n me!
    }

    private String pluralize(int num) {
        return (num != 1 && num != -1) ? "s" : "";
    }
}
