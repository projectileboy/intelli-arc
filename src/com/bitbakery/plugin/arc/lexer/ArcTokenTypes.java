package com.bitbakery.plugin.arc.lexer;

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

import com.bitbakery.plugin.arc.ArcFileType;
import com.bitbakery.plugin.arc.ArcLanguage;
import com.bitbakery.plugin.arc.psi.ArcElementType;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * Identifies all of the token types (at least, the ones we'll care about) in Arc.
 * Used by the lexer to break an Arc source file down into tokens.
 */
public interface ArcTokenTypes {

    static ArcLanguage language = ArcFileType.ARC;


    // Special characters
    IElementType LEFT_PAREN = new ArcElementType("(");
    IElementType RIGHT_PAREN = new ArcElementType(")");

    IElementType LEFT_SQUARE = new ArcElementType("[");
    IElementType RIGHT_SQUARE = new ArcElementType("]");

    TokenSet BRACES = TokenSet.create(LEFT_PAREN, LEFT_SQUARE, RIGHT_PAREN, RIGHT_SQUARE);

    IElementType TILDE = new ArcElementType("~");

    IElementType COMPOSER = new ArcElementType(":");
    IElementType DOT = new ArcElementType(".");
    IElementType EQ = new ArcElementType("=");

    IElementType QUOTE = new ArcElementType("'");
    IElementType BACKQUOTE = new ArcElementType("`");
    IElementType COMMA = new ArcElementType(",");
    IElementType COMMA_AT = new ArcElementType(",@");
    TokenSet SPECIAL_CHARACTERS = TokenSet.create(TILDE, COMPOSER, DOT, EQ, BACKQUOTE, QUOTE, COMMA, COMMA_AT);
    TokenSet MACRO_MODIFIERS = TokenSet.create(COMMA, COMMA_AT, QUOTE, BACKQUOTE);

    // This guy is a little special, at least within single-var anonymous fn definitions
    IElementType UNDERSCORE = new ArcElementType("_");


    // Keywords and special forms
    IElementType DEF = new ArcElementType("def");
    IElementType MAC = new ArcElementType("mac");


    IElementType QUOTE_KEYWORD = new ArcElementType("quote");
    IElementType FN = new ArcElementType("fn");
    IElementType IF = new ArcElementType("if");


    // Library stuff from arc.arc
    IElementType DO = new ArcElementType("do");
    IElementType LET = new ArcElementType("let");
    IElementType WITH = new ArcElementType("with");

    TokenSet KEYWORDS = TokenSet.create(DEF, MAC, FN, IF, DO, LET, WITH);

    // Comments
    IElementType BLOCK_COMMENT = new ArcElementType("block comment");
    IElementType LINE_COMMENT = new ArcElementType("line comment");
    IElementType MULTILINE_COMMENT = new ArcElementType("multiline comment");
    TokenSet COMMENTS = TokenSet.create(BLOCK_COMMENT, LINE_COMMENT, MULTILINE_COMMENT);


    // Literals
    IElementType STRING_LITERAL = new ArcElementType("string literal");
    IElementType NUMERIC_LITERAL = new ArcElementType("numeric literal");
    IElementType CHAR_LITERAL = new ArcElementType("character literal");

    IElementType TRUE = new ArcElementType("t");
    IElementType NIL = new ArcElementType("nil");
    TokenSet BOOLEAN_LITERAL = TokenSet.create(TRUE, NIL);

    TokenSet LITERALS = TokenSet.create(STRING_LITERAL, NUMERIC_LITERAL, CHAR_LITERAL, TRUE, NIL);
    TokenSet READABLE_TEXT = TokenSet.create(STRING_LITERAL, BLOCK_COMMENT, LINE_COMMENT);


    IElementType SYMBOL = new ArcElementType("symbol");
    TokenSet SYMBOL_FILTER = TokenSet.create(ArcTokenTypes.SYMBOL);


    // Control characters
    IElementType EOL = new ArcElementType("end of line");
    IElementType EOF = new ArcElementType("end of file");
    IElementType WHITESPACE = TokenType.WHITE_SPACE;
    IElementType BAD_CHARACTER = TokenType.BAD_CHARACTER;

    TokenSet WHITESPACE_SET = TokenSet.create(EOL, EOF, WHITESPACE);
    // TODO - Not tokens, but we should know what library functions are available depending on the CL implementation we're using??
    // TODO - We should understand the syntax of common macros, like do, print format synatx, etc.
    // TODO - Should we distinguish between macros and functions that are destructive?

}