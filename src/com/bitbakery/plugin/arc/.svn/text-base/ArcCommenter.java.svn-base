package com.bitbakery.plugin.arc;

import com.bitbakery.plugin.arc.lexer.ArcTokenTypes;
import com.intellij.lang.CodeDocumentationAwareCommenter;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;

public class ArcCommenter implements CodeDocumentationAwareCommenter {

    public String getLineCommentPrefix() {
        return ";";
    }

    public String getBlockCommentPrefix() {
        return "#|";
    }

    public String getBlockCommentSuffix() {
        return "|#";
    }

    @Nullable
    public IElementType getLineCommentTokenType() {
        return ArcTokenTypes.LINE_COMMENT;
    }

    @Nullable
    public IElementType getBlockCommentTokenType() {
        return ArcTokenTypes.BLOCK_COMMENT;
    }

    public String getDocumentationCommentPrefix() {
        return "#|#";
    }

    public String getDocumentationCommentLinePrefix() {
        return "#";
    }

    public String getDocumentationCommentSuffix() {
        return "|#";
    }

    @Nullable
    public IElementType getDocumentationCommentTokenType() {
        return ArcTokenTypes.BLOCK_COMMENT;
    }
}
