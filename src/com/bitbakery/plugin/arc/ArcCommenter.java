package com.bitbakery.plugin.arc;

import com.bitbakery.plugin.arc.lexer.ArcTokenTypes;
import com.intellij.lang.CodeDocumentationAwareCommenter;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiComment;
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

    public boolean isDocumentationComment(PsiComment element) {
        // TODO - Whah? Hah??
        return false;
    }

    @Nullable
    public IElementType getDocumentationCommentTokenType() {
        return ArcTokenTypes.BLOCK_COMMENT;
    }
}
