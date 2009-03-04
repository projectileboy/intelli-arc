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

import com.bitbakery.plugin.arc.lexer.ArcLexer;
import static com.bitbakery.plugin.arc.lexer.ArcTokenTypes.*;
import com.bitbakery.plugin.arc.psi.ArcElementTypes;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.SyntaxHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import static com.intellij.openapi.editor.colors.TextAttributesKey.*;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines Arc tokens and elements which can have custom font and color to enhance readability
 */
public class ArcSyntaxHighlighter extends SyntaxHighlighterBase {
    private static Map<IElementType, TextAttributesKey> keys;

    static {
        keys = new HashMap<IElementType, TextAttributesKey>();

        keys.put(TILDE, createAttrs("ARC.TILDE", HighlighterColors.TEXT));
        keys.put(EQ, createAttrs("ARC.EQ", HighlighterColors.TEXT));
        keys.put(BACKQUOTE, createAttrs("ARC.BACKQUOTE", HighlighterColors.TEXT));
        keys.put(QUOTE, createAttrs("ARC.QUOTE", HighlighterColors.TEXT));
        keys.put(COMMA, createAttrs("ARC.COMMA", HighlighterColors.TEXT));
        keys.put(COMMA_AT, createAttrs("ARC.COMMA_AT", HighlighterColors.TEXT));
        keys.put(DOT, createAttrs("ARC.DOT", HighlighterColors.TEXT));
        keys.put(COMPOSER, createAttrs("ARC.COMPOSER", HighlighterColors.TEXT));

        keys.put(TRUE, createAttrs("ARC.TRUE", SyntaxHighlighterColors.NUMBER));
        keys.put(NIL, createAttrs("ARC.NIL", SyntaxHighlighterColors.NUMBER));

        keys.put(NUMERIC_LITERAL, createAttrs("ARC.NUMERIC_LITERAL", SyntaxHighlighterColors.NUMBER));
        keys.put(STRING_LITERAL, createAttrs("ARC.STRING_LITERAL", SyntaxHighlighterColors.STRING));
        keys.put(CHAR_LITERAL, createAttrs("ARC.CHAR_LITERAL", SyntaxHighlighterColors.STRING));
        keys.put(BAD_CHARACTER, createAttrs("ARC.BAD_CHARACTER", HighlighterColors.BAD_CHARACTER));
        keys.put(BLOCK_COMMENT, createAttrs("ARC.BLOCK_COMMENT", SyntaxHighlighterColors.LINE_COMMENT));
        keys.put(MULTILINE_COMMENT, createAttrs("ARC.MULTILINE_COMMENT", SyntaxHighlighterColors.LINE_COMMENT));
        keys.put(LINE_COMMENT, createAttrs("ARC.LINE_COMMENT", SyntaxHighlighterColors.LINE_COMMENT));
        keys.put(ArcElementTypes.DOCSTRING, createAttrs("ARC.DOCSTRING", SyntaxHighlighterColors.LINE_COMMENT));

        keys.put(DO, createAttrs("ARC.LINE_COMMENT", SyntaxHighlighterColors.LINE_COMMENT));

        keys.put(QUOTE_KEYWORD, createAttrs("ARC.QUOTE_KEYWORD", SyntaxHighlighterColors.KEYWORD));
        keys.put(FN, createAttrs("ARC.FN", SyntaxHighlighterColors.KEYWORD));
        keys.put(IF, createAttrs("ARC.IF", SyntaxHighlighterColors.KEYWORD));
        keys.put(DO, createAttrs("ARC.DO", SyntaxHighlighterColors.KEYWORD));
        keys.put(LET, createAttrs("ARC.LET", SyntaxHighlighterColors.KEYWORD));
        keys.put(WITH, createAttrs("ARC.WITH", SyntaxHighlighterColors.KEYWORD));
        keys.put(MAC, createAttrs("ARC.MAC", SyntaxHighlighterColors.KEYWORD));
        keys.put(DEF, createAttrs("ARC.DEF", SyntaxHighlighterColors.KEYWORD));

        keys.put(SYMBOL, createAttrs("ARC.SYMBOL", HighlighterColors.TEXT));
    }

    public static TextAttributesKey getTextAttrs(IElementType key) {
        return keys.get(key);
    }

    private static TextAttributesKey createAttrs(String name, TextAttributesKey defaultKey) {
        return createTextAttributesKey(name, defaultKey.getDefaultAttributes());
    }

    @NotNull
    public Lexer getHighlightingLexer() {
        return new ArcLexer();
    }

    @NotNull
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        return pack(keys.get(tokenType));
    }
}


