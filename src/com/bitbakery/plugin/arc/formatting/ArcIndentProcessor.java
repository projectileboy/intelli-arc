package com.bitbakery.plugin.arc.formatting;

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

import com.intellij.formatting.Indent;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.bitbakery.plugin.arc.psi.ArcFile;
import static com.bitbakery.plugin.arc.psi.ArcElementTypes.*;
import static com.bitbakery.plugin.arc.lexer.ArcTokenTypes.*;

/**
 * Created by IntelliJ IDEA.
 * User: kurtc
 * Date: Mar 1, 2009
 * Time: 10:01:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class ArcIndentProcessor {
    public static Indent getChildIndent(ArcBlock parent, ASTNode prevChildNode, ASTNode child) {
        ASTNode astNode = parent.getNode();
        final PsiElement psiParent = astNode.getPsi();

        if (psiParent instanceof ArcFile) {
            return Indent.getNoneIndent();
        }

        ASTNode node = parent.getNode();
        if (EXPRESSIONS.contains(node.getElementType())) {
            if (!BRACES.contains(child.getElementType())) {
                return Indent.getNormalIndent();
            }
        }
        return Indent.getNoneIndent();
    }
}
