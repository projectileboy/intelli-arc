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
import com.bitbakery.plugin.arc.lexer.ArcTokenTypes;
import static com.bitbakery.plugin.arc.lexer.ArcTokenTypes.*;
import com.bitbakery.plugin.arc.psi.ArcElementTypes;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.SyntaxHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines Arc tokens and elements which can have custom font and color to enhance readability
 */
public class ArcSyntaxHighlighter extends SyntaxHighlighterBase {
    private static Map<IElementType, TextAttributesKey> ATTRIBUTES = new HashMap<IElementType, TextAttributesKey>();

    // (1) Define IDs
    // TODO - i18n all these guys...
    @NonNls
    public static final String COMMENT_ID = "Comment";
    @NonNls
    public static final String DOCSTRING_ID = "Docstring";
    @NonNls
    public static final String NUMBER_ID = "Numeric literal";
    @NonNls
    public static final String STRING_ID = "String literal";
    @NonNls
    public static final String CHAR_ID = "Character literal";
    @NonNls
    public static final String BOOLEAN_ID = "Boolean literal";
    @NonNls
    public static final String DEF_ID = "Def";
    @NonNls
    public static final String MAC_ID = "Mac";
    @NonNls
    public static final String SPECIAL_CHARACTER_ID = "Special characters";
    @NonNls
    public static final String MACRO_TEMPLATE_ID = "Macro template characters ( , ,@ ' ` )";
    @NonNls
    public static final String SPECIAL_FORM_ID = "Special forms (if, let, etc.)";
    @NonNls
    public static final String SQUARE_BRACKET_ID = "Square brackets [ ]";
    @NonNls
    public static final String PAREN_ID = "Parens ( )";
    @NonNls
    public static final String BAD_CHARACTER_ID = "Invalid character";


    // (2) Register TextAttributes
    private static void createKey(String id, TextAttributesKey prototype) {
        TextAttributesKey.createTextAttributesKey(id, prototype.getDefaultAttributes());
    }

    static {
        createKey(COMMENT_ID, SyntaxHighlighterColors.LINE_COMMENT);
        createKey(DOCSTRING_ID, SyntaxHighlighterColors.DOC_COMMENT);
        createKey(NUMBER_ID, SyntaxHighlighterColors.NUMBER);
        createKey(STRING_ID, SyntaxHighlighterColors.STRING);
        createKey(CHAR_ID, SyntaxHighlighterColors.STRING);
        createKey(BOOLEAN_ID, SyntaxHighlighterColors.NUMBER);
        createKey(DEF_ID, SyntaxHighlighterColors.KEYWORD);
        createKey(MAC_ID, SyntaxHighlighterColors.KEYWORD);
        createKey(SPECIAL_CHARACTER_ID, SyntaxHighlighterColors.KEYWORD);
        createKey(MACRO_TEMPLATE_ID, SyntaxHighlighterColors.KEYWORD);
        createKey(SPECIAL_FORM_ID, SyntaxHighlighterColors.KEYWORD);
        createKey(SQUARE_BRACKET_ID, SyntaxHighlighterColors.BRACKETS);
        createKey(PAREN_ID, SyntaxHighlighterColors.PARENTHS);
    }


    // (3) Define TextAttributesKeys
    public static TextAttributesKey COMMENT = TextAttributesKey.createTextAttributesKey(COMMENT_ID);
    public static TextAttributesKey DOCSTRING = TextAttributesKey.createTextAttributesKey(DOCSTRING_ID);
    public static TextAttributesKey NUMBER = TextAttributesKey.createTextAttributesKey(NUMBER_ID);
    public static TextAttributesKey STRING = TextAttributesKey.createTextAttributesKey(STRING_ID);
    public static TextAttributesKey CHAR = TextAttributesKey.createTextAttributesKey(CHAR_ID);
    public static TextAttributesKey BOOLEAN = TextAttributesKey.createTextAttributesKey(BOOLEAN_ID);
    public static TextAttributesKey DEF = TextAttributesKey.createTextAttributesKey(DEF_ID);
    public static TextAttributesKey MAC = TextAttributesKey.createTextAttributesKey(MAC_ID);
    public static TextAttributesKey SPECIAL_CHARACTER = TextAttributesKey.createTextAttributesKey(SPECIAL_CHARACTER_ID);
    public static TextAttributesKey MACRO_TEMPLATE_CHARACTER = TextAttributesKey.createTextAttributesKey(MACRO_TEMPLATE_ID);
    public static TextAttributesKey SPECIAL_FORM = TextAttributesKey.createTextAttributesKey(SPECIAL_FORM_ID);
    public static TextAttributesKey PAREN = TextAttributesKey.createTextAttributesKey(PAREN_ID);
    public static TextAttributesKey SQUARE_BRACKET = TextAttributesKey.createTextAttributesKey(SQUARE_BRACKET_ID);

    // (4) Build token --> color map
    static {
        fillMap(ATTRIBUTES, COMMENTS, COMMENT);
        fillMap(ATTRIBUTES, DOCSTRING, ArcElementTypes.DOCSTRING);
        fillMap(ATTRIBUTES, NUMBER, NUMERIC_LITERAL);
        fillMap(ATTRIBUTES, STRING, STRING_LITERAL);
        fillMap(ATTRIBUTES, CHAR, CHAR_LITERAL);
        fillMap(ATTRIBUTES, BOOLEAN_LITERALS, BOOLEAN);
        fillMap(ATTRIBUTES, DEF, ArcTokenTypes.DEF);
        fillMap(ATTRIBUTES, MAC, ArcTokenTypes.MAC);
        fillMap(ATTRIBUTES, SPECIAL_CHARACTER, COMPOSER, DOT, TILDE, EQ);
        fillMap(ATTRIBUTES, MACRO_TEMPLATE_CHARACTER, QUOTE, BACKQUOTE, COMMA, COMMA_AT);
        fillMap(ATTRIBUTES, SPECIAL_FORM, FN, IF, LET, WITH, QUOTE_KEYWORD, DO);
        fillMap(ATTRIBUTES, PAREN, LEFT_PAREN, RIGHT_PAREN);
        fillMap(ATTRIBUTES, SQUARE_BRACKET, LEFT_SQUARE, RIGHT_SQUARE);
    }

    @NotNull
    public Lexer getHighlightingLexer() {
        return new ArcLexer();
    }

    @NotNull
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        return pack(ATTRIBUTES.get(tokenType));
    }
}


