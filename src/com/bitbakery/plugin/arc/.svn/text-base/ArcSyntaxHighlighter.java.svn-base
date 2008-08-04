package com.bitbakery.plugin.arc;

import com.bitbakery.plugin.arc.lexer.ArcLexer;
import com.bitbakery.plugin.arc.lexer.ArcTokenTypes;
import com.bitbakery.plugin.arc.psi.ArcElementTypes;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
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

    public static final TextAttributesKey TILDE = TextAttributesKey.createTextAttributesKey("ARC.TILDE", HighlighterColors.TEXT.getDefaultAttributes());
    public static final TextAttributesKey EQ = TextAttributesKey.createTextAttributesKey("ARC.EQ", HighlighterColors.TEXT.getDefaultAttributes());
    public static final TextAttributesKey BACKQUOTE = TextAttributesKey.createTextAttributesKey("ARC.BACKQUOTE", HighlighterColors.TEXT.getDefaultAttributes());
    public static final TextAttributesKey QUOTE = TextAttributesKey.createTextAttributesKey("ARC.QUOTE", HighlighterColors.TEXT.getDefaultAttributes());
    public static final TextAttributesKey COMMA = TextAttributesKey.createTextAttributesKey("ARC.COMMA", HighlighterColors.TEXT.getDefaultAttributes());
    public static final TextAttributesKey COMMA_AT = TextAttributesKey.createTextAttributesKey("ARC.COMMA_AT", HighlighterColors.TEXT.getDefaultAttributes());
    public static final TextAttributesKey DOT = TextAttributesKey.createTextAttributesKey("ARC.DOT", HighlighterColors.TEXT.getDefaultAttributes());
    public static final TextAttributesKey COMPOSER = TextAttributesKey.createTextAttributesKey("ARC.COMPOSER", HighlighterColors.TEXT.getDefaultAttributes());

    public static final TextAttributesKey TRUE = TextAttributesKey.createTextAttributesKey("ARC.TRUE", HighlighterColors.JAVA_NUMBER.getDefaultAttributes());
    public static final TextAttributesKey NIL = TextAttributesKey.createTextAttributesKey("ARC.NIL", HighlighterColors.JAVA_NUMBER.getDefaultAttributes());

    public static final TextAttributesKey NUMERIC_LITERAL = TextAttributesKey.createTextAttributesKey("ARC.NUMERIC_LITERAL", HighlighterColors.JAVA_NUMBER.getDefaultAttributes());
    public static final TextAttributesKey STRING_LITERAL = TextAttributesKey.createTextAttributesKey("ARC.STRING_LITERAL", HighlighterColors.JAVA_STRING.getDefaultAttributes());
    public static final TextAttributesKey CHAR_LITERAL = TextAttributesKey.createTextAttributesKey("ARC.CHAR_LITERAL", HighlighterColors.JAVA_STRING.getDefaultAttributes());
    public static final TextAttributesKey BAD_CHARACTER = TextAttributesKey.createTextAttributesKey("ARC.BAD_CHARACTER", HighlighterColors.BAD_CHARACTER.getDefaultAttributes());
    public static final TextAttributesKey BLOCK_COMMENT = TextAttributesKey.createTextAttributesKey("ARC.BLOCK_COMMENT", HighlighterColors.JAVA_LINE_COMMENT.getDefaultAttributes());
    public static final TextAttributesKey LINE_COMMENT = TextAttributesKey.createTextAttributesKey("ARC.LINE_COMMENT", HighlighterColors.JAVA_LINE_COMMENT.getDefaultAttributes());
    public static final TextAttributesKey DOCSTRING = TextAttributesKey.createTextAttributesKey("ARC.DOCSTRING", HighlighterColors.JAVA_DOC_COMMENT.getDefaultAttributes());

    public static final TextAttributesKey QUOTE_KEYWORD = TextAttributesKey.createTextAttributesKey("ARC.QUOTE_KEYWORD", HighlighterColors.JAVA_KEYWORD.getDefaultAttributes());
    public static final TextAttributesKey FN = TextAttributesKey.createTextAttributesKey("ARC.FN", HighlighterColors.JAVA_KEYWORD.getDefaultAttributes());
    public static final TextAttributesKey IF = TextAttributesKey.createTextAttributesKey("ARC.IF", HighlighterColors.JAVA_KEYWORD.getDefaultAttributes());
    public static final TextAttributesKey DO = TextAttributesKey.createTextAttributesKey("ARC.DO", HighlighterColors.JAVA_KEYWORD.getDefaultAttributes());
    public static final TextAttributesKey LET = TextAttributesKey.createTextAttributesKey("ARC.LET", HighlighterColors.JAVA_KEYWORD.getDefaultAttributes());
    public static final TextAttributesKey WITH = TextAttributesKey.createTextAttributesKey("ARC.WITH", HighlighterColors.JAVA_KEYWORD.getDefaultAttributes());
    public static final TextAttributesKey MAC = TextAttributesKey.createTextAttributesKey("ARC.MAC", HighlighterColors.JAVA_KEYWORD.getDefaultAttributes());
    public static final TextAttributesKey DEF = TextAttributesKey.createTextAttributesKey("ARC.DEF", HighlighterColors.JAVA_KEYWORD.getDefaultAttributes());

    public static final TextAttributesKey SYMBOL = TextAttributesKey.createTextAttributesKey("ARC.SYMBOL", HighlighterColors.TEXT.getDefaultAttributes());

    static {
        keys = new HashMap<IElementType, TextAttributesKey>();

        keys.put(ArcTokenTypes.TILDE, TILDE);
        keys.put(ArcTokenTypes.EQ, EQ);
        keys.put(ArcTokenTypes.BACKQUOTE, BACKQUOTE);
        keys.put(ArcTokenTypes.QUOTE, QUOTE);
        keys.put(ArcTokenTypes.COMMA, COMMA);
        keys.put(ArcTokenTypes.COMMA_AT, COMMA_AT);
        keys.put(ArcTokenTypes.DOT, DOT);
        keys.put(ArcTokenTypes.COMPOSER, COMPOSER);

        keys.put(ArcTokenTypes.TRUE, TRUE);
        keys.put(ArcTokenTypes.NIL, NIL);

        keys.put(ArcTokenTypes.NUMERIC_LITERAL, NUMERIC_LITERAL);
        keys.put(ArcTokenTypes.STRING_LITERAL, STRING_LITERAL);
        keys.put(ArcTokenTypes.CHAR_LITERAL, CHAR_LITERAL);
        keys.put(ArcTokenTypes.BAD_CHARACTER, BAD_CHARACTER);
        keys.put(ArcTokenTypes.BLOCK_COMMENT, BLOCK_COMMENT);
        keys.put(ArcTokenTypes.LINE_COMMENT, LINE_COMMENT);

        // TODO - This doesn't work - the coloring only works with the lexer output - *not* the parser output
        keys.put(ArcElementTypes.DOCSTRING, DOCSTRING);
        keys.put(ArcTokenTypes.DO, LINE_COMMENT);

        keys.put(ArcTokenTypes.QUOTE_KEYWORD, QUOTE_KEYWORD);
        keys.put(ArcTokenTypes.FN, FN);
        keys.put(ArcTokenTypes.IF, IF);
        keys.put(ArcTokenTypes.DO, DO);
        keys.put(ArcTokenTypes.LET, LET);
        keys.put(ArcTokenTypes.WITH, WITH);
        keys.put(ArcTokenTypes.MAC, MAC);
        keys.put(ArcTokenTypes.DEF, DEF);

        keys.put(ArcTokenTypes.SYMBOL, SYMBOL);
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


