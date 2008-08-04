package com.bitbakery.plugin.arc;

import com.bitbakery.plugin.arc.lexer.ArcTokenTypes;
import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Defines paired braces for Arc code
 */
public class ArcBraceMatcher implements PairedBraceMatcher {
    private static final BracePair[] PAIRS = new BracePair[]{
            new BracePair(ArcTokenTypes.LEFT_PAREN, ArcTokenTypes.RIGHT_PAREN, true),
            new BracePair(ArcTokenTypes.LEFT_SQUARE, ArcTokenTypes.RIGHT_SQUARE, false)
    };

    public BracePair[] getPairs() {
        return PAIRS;
    }

    public boolean isPairedBracesAllowedBeforeType(@NotNull final IElementType lbraceType, @Nullable final IElementType tokenType) {
        return true;
    }

    public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
        return 0;  // TODO - This could be handy for non-Lispy languages; I'm not sure we actually need it (see parent Javadoc)
    }
}
