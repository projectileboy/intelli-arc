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

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.impl.source.tree.ChameleonElement;
import com.intellij.psi.tree.IChameleonElementType;
import com.intellij.openapi.util.TextRange;
import com.bitbakery.plugin.arc.psi.ArcFile;
import com.bitbakery.plugin.arc.psi.ArcElementTypes;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Formatting block for Arc source code files
 */
public class ArcBlock implements Block {

    final protected ASTNode myNode;
    final protected Alignment myAlignment;
    final protected Indent myIndent;
    final protected Wrap myWrap;
    final protected CodeStyleSettings mySettings;

    protected List<Block> mySubBlocks = null;


    public ArcBlock(@NotNull final ASTNode node, @Nullable final Alignment alignment, @NotNull final Indent indent, @Nullable final Wrap wrap, final CodeStyleSettings settings) {
        myNode = node;
        myAlignment = alignment;
        myIndent = indent;
        myWrap = wrap;
        mySettings = settings;
    }

    @NotNull
    public ASTNode getNode() {
        return myNode;
    }

    @NotNull
    public CodeStyleSettings getSettings() {
        return mySettings;
    }

    @NotNull
    public TextRange getTextRange() {
        return myNode.getTextRange();
    }

    @NotNull
    public List<Block> getSubBlocks() {
        if (mySubBlocks == null) {
            mySubBlocks = ArcBlockGenerator.generateSubBlocks(myNode, myAlignment, myWrap, mySettings, this);
        }
        return mySubBlocks;
    }

    @Nullable
    public Wrap getWrap() {
        return myWrap;
    }

    @Nullable
    public Indent getIndent() {
        return myIndent;
    }

    @Nullable
    public Alignment getAlignment() {
        return myAlignment;
    }

    public Spacing getSpacing(Block child1, Block child2) {
        return ArcSpacingProcessor.getSpacing(child1, child2);
    }

    @NotNull
    public ChildAttributes getChildAttributes(final int newChildIndex) {
        return getAttributesByParent();
    }

    private ChildAttributes getAttributesByParent() {
        ASTNode astNode = getNode();
        final PsiElement psiParent = astNode.getPsi();
        if (psiParent instanceof ArcFile) {
            return new ChildAttributes(Indent.getNoneIndent(), null);
        }
        if (ArcElementTypes.LIST_LIKE_FORMS.contains(astNode.getElementType())) {
            return new ChildAttributes(Indent.getNormalIndent(), null);
        }
        return new ChildAttributes(Indent.getNoneIndent(), null);
    }


    public boolean isIncomplete() {
        return isIncomplete(myNode);
    }

    /**
     * @param node Tree node
     * @return true if node is incomplete
     */
    public boolean isIncomplete(@NotNull final ASTNode node) {
        if (node.getElementType() instanceof IChameleonElementType) return false;
        ASTNode lastChild = node.getLastChildNode();
        while (lastChild != null &&
                !(lastChild.getElementType() instanceof IChameleonElementType) &&
                (lastChild.getPsi() instanceof PsiWhiteSpace || lastChild.getPsi() instanceof PsiComment)) {
            lastChild = lastChild.getTreePrev();
        }
        return lastChild != null && !(lastChild instanceof ChameleonElement) &&
                (lastChild.getPsi() instanceof PsiErrorElement || isIncomplete(lastChild));
    }

    public boolean isLeaf() {
        return myNode.getFirstChildNode() == null;
    }
}
