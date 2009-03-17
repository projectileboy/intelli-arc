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

import com.bitbakery.plugin.arc.lexer.ArcTokenTypes;
import static com.bitbakery.plugin.arc.lexer.ArcTokenTypes.*;
import com.bitbakery.plugin.arc.psi.ArcElementTypes;
import com.intellij.formatting.Block;
import com.intellij.formatting.Spacing;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;

/**
 * Created by IntelliJ IDEA.
 * User: kurtc
 * Date: Mar 1, 2009
 * Time: 10:03:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ArcSpacingProcessor {

    private static final Spacing NO_SPACING = Spacing.createSpacing(0, 0, 0, false, 0);
    private static final Spacing NO_SPACING_WITH_NEWLINE = Spacing.createSpacing(0, 0, 0, true, 1);
    private static final Spacing COMMON_SPACING = Spacing.createSpacing(1, 1, 0, true, 100);

    public static Spacing getSpacing(Block left, Block right) {
        if (!(left instanceof ArcBlock) || !(right instanceof ArcBlock)) return null;

        IElementType leftType = ((ArcBlock) left).getNode().getElementType();
        IElementType rightType = ((ArcBlock) right).getNode().getElementType();

        if (MACRO_MODIFIERS.contains(leftType)) {
            return NO_SPACING;
        }

        String leftText = ((ArcBlock) left).getNode().getText();
        String rightText = ((ArcBlock) right).getNode().getText();

        if (leftText.trim().startsWith(",") || rightText.trim().startsWith(",")) {
            return null;
        }

        if (BRACES.contains(leftType) || BRACES.contains(rightType)) {
            return NO_SPACING_WITH_NEWLINE;
        }

        return COMMON_SPACING;
    }
}
