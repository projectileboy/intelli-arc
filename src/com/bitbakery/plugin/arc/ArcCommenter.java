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

import com.bitbakery.plugin.arc.lexer.ArcTokenTypes;
import static com.bitbakery.plugin.arc.lexer.ArcTokenTypes.*;
import com.intellij.lang.CodeDocumentationAwareCommenter;
import com.intellij.lang.Commenter;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiComment;
import org.jetbrains.annotations.Nullable;

/**
 * Defines the support for "Comment with Line Comment" and "Comment with Block Comment" actions for Arc source files.
 */
public class ArcCommenter implements Commenter {

    public String getLineCommentPrefix() {
        return ";";
    }

    public String getBlockCommentPrefix() {
        return "#|";
    }

    public String getBlockCommentSuffix() {
        return "|#";
    }
}
