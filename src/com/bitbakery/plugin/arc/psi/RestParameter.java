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

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class RestParameter extends Parameter {
    public RestParameter(@NotNull final ASTNode node) {
        super(node);
    }

    /*
    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
        final ASTNode nameElement = createNameIdentifier(getProject(), name);
        getNode().replaceChild(getNode().getFirstChildNode(), nameElement);
        return this;
    }

    private ASTNode createNameIdentier(Project project, String name) {
        ParserDefinition def = ArcSupportLoader.ARC.getLanguage().getParserDefinition();
        final PsiFile dummyFile = def.createFile(project., "dummy." + ArcSupportLoader.ARC.getDefaultExtension(), name + ";");
        final JSExpressionStatement expressionStatement = (JSExpressionStatement) dummyFile.getFirstChild();
        final JSReferenceExpressionImpl refExpression = (JSReferenceExpressionImpl) expressionStatement.getFirstChild();

        return refExpression.getNode().getFirstChildNode();
    }
*/
}