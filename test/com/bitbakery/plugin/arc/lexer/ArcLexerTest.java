package com.bitbakery.plugin.arc.lexer;

import static com.bitbakery.plugin.arc.lexer.ArcTokenTypes.*;
import com.intellij.psi.tree.IElementType;
import org.junit.Assert;
import org.junit.Test;

public class ArcLexerTest {

    @Test
    public void testSimpleDefTokenization() {
        testTokenization("(def hello () (prn \"Hello, world!\"))",
                new IElementType[]{
                        LEFT_PAREN,
                        DEF,
                        WHITESPACE,
                        SYMBOL,
                        WHITESPACE,
                        LEFT_PAREN,
                        RIGHT_PAREN,
                        WHITESPACE,
                        LEFT_PAREN,
                        SYMBOL,
                        WHITESPACE,
                        STRING_LITERAL,
                        RIGHT_PAREN,
                        RIGHT_PAREN
                });
    }

    @Test
    public void testOneLineBlockCommentTokenization() {
        testTokenization("#| This is a block comment |# (+ 1 2)",
                new IElementType[]{
                        BLOCK_COMMENT,
                        WHITESPACE,
                        LEFT_PAREN,
                        SYMBOL,
                        WHITESPACE,
                        NUMERIC_LITERAL,
                        WHITESPACE,
                        NUMERIC_LITERAL,
                        RIGHT_PAREN
                });
    }

    @Test
    public void testMultiLineBlockCommentTokenization() {
        testTokenization("#| This \r\n is a block \r\n comment |# (+ 1 2)",
                new IElementType[]{
                        BLOCK_COMMENT,
                        WHITESPACE,
                        LEFT_PAREN,
                        SYMBOL,
                        WHITESPACE,
                        NUMERIC_LITERAL,
                        WHITESPACE,
                        NUMERIC_LITERAL,
                        RIGHT_PAREN
                });
    }

    @Test
    public void testMultipleBlockCommentTokenization() {
        testTokenization("#| This \r\n is a block \r\n comment |# (+ 1 2)", null);
    }

    @Test
    public void testSimpleMacroTokenization() {
        testTokenization("(mac rev (a b c) `(c b a))",
                new IElementType[]{
                        LEFT_PAREN,
                        MAC,
                        WHITESPACE,
                        SYMBOL,
                        WHITESPACE,

                        LEFT_PAREN,
                        SYMBOL,
                        WHITESPACE,
                        SYMBOL,
                        WHITESPACE,
                        SYMBOL,
                        RIGHT_PAREN,

                        WHITESPACE,

                        BACKQUOTE,
                        LEFT_PAREN,
                        SYMBOL,
                        WHITESPACE,
                        SYMBOL,
                        WHITESPACE,
                        SYMBOL,
                        RIGHT_PAREN,
                        RIGHT_PAREN
                });
    }

    @Test
    public void testSquareBracketTokenization() {

    }

    @Test
    public void testComposedFunctionTokenization() {
        testTokenization("(map f:g:h '(a b c d e))",
                new IElementType[]{
                        LEFT_PAREN,
                        SYMBOL,

                        WHITESPACE,

                        SYMBOL,
                        COMPOSER,
                        SYMBOL,
                        COMPOSER,
                        SYMBOL,

                        WHITESPACE,

                        QUOTE,
                        LEFT_PAREN,
                        SYMBOL,
                        WHITESPACE,
                        SYMBOL,
                        WHITESPACE,
                        SYMBOL,
                        WHITESPACE,
                        SYMBOL,
                        WHITESPACE,
                        SYMBOL,
                        RIGHT_PAREN,
                        RIGHT_PAREN
                });
    }

    private void testTokenization(String code, IElementType[] expectedTokens) {
        ArcLexer lexer = new ArcLexer();
        lexer.start(code.toCharArray());

        for (IElementType expectedToken : expectedTokens) {
            IElementType tokenType = lexer.getTokenType();
            Assert.assertEquals(expectedToken, tokenType);
            lexer.advance();
        }
    }
}
