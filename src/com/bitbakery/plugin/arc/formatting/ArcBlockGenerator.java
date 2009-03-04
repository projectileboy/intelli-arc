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

import com.bitbakery.plugin.arc.psi.SingleArgFn;
import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kurtc
 * Date: Mar 1, 2009
 * Time: 9:58:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ArcBlockGenerator {
    private static ASTNode myNode;
    private static Alignment myAlignment;
    private static Wrap myWrap;
    private static CodeStyleSettings mySettings;
    private static ArcBlock myBlock;

    public static List<Block> generateSubBlocks(ASTNode node, Alignment alignment, Wrap wrap, CodeStyleSettings settings, ArcBlock block) {
        myNode = node;
        myWrap = wrap;
        mySettings = settings;
        myAlignment = alignment;
        myBlock = block;

        PsiElement blockPsi = myBlock.getNode().getPsi();

        final ArrayList<Block> subBlocks = new ArrayList<Block>();
        ASTNode children[] = myNode.getChildren(null);
        ASTNode prevChildNode = null;

        final Alignment align = mustAlign(blockPsi) ? Alignment.createAlignment() : null;
        for (ASTNode childNode : children) {
            if (canBeCorrectBlock(childNode)) {
                final Indent indent = ArcIndentProcessor.getChildIndent(myBlock, prevChildNode, childNode);
                subBlocks.add(new ArcBlock(childNode, align, indent, myWrap, mySettings));
                prevChildNode = childNode;
            }
        }
        return subBlocks;
    }

    private static boolean mustAlign(PsiElement blockPsi) {
        return blockPsi instanceof SingleArgFn;
    }

    private static boolean canBeCorrectBlock(final ASTNode node) {
        return (node.getText().trim().length() > 0);
    }
}
