package com.bitbakery.plugin.arc.psi;

import static com.bitbakery.plugin.arc.ArcStrings.message;
import static com.bitbakery.plugin.arc.lexer.ArcTokenTypes.*;
import static com.bitbakery.plugin.arc.psi.ArcElementTypes.*;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

/**
 * Consumes a stream of Arc tokens and generates a PSI tree for an Arc file
 */
public class ArcParser implements PsiParser {

    @NotNull
    public ASTNode parse(IElementType root, PsiBuilder builder) {
        final PsiBuilder.Marker rootMarker = builder.mark();
        while (!builder.eof()) {
            IElementType token = builder.getTokenType();
            if (RIGHT_PAREN == token) {
                builder.error(message("parser.error.expectedLeftParen"));
                builder.advanceLexer();
            } else if (LEFT_PAREN == token) {
                parseParens(builder);
            } else if (LEFT_SQUARE == token) {
                parseBrackets(builder);
            } else if (SYMBOL == token) {
                markAndAdvance(builder, VARIABLE_REFERENCE);
            } else {
                builder.advanceLexer();

            }
        }
        rootMarker.done(root);
        return builder.getTreeBuilt();
    }

    /**
     * Enter: Lexer is pointed at the opening left square bracket
     * Exit: Lexer is pointed immediately after the closing right square bracket, or at the end-of-file
     */
    private void parseBrackets(PsiBuilder builder) {
        PsiBuilder.Marker marker = markAndAdvance(builder);

        while (!builder.eof()) {
            IElementType token = builder.getTokenType();
            if (RIGHT_SQUARE == token) {
                builder.advanceLexer();
                marker.done(SINGLE_ARG_ANONYMOUS_FUNCTION_DEFINITION);
                return;
            } else if (LEFT_PAREN == token) {
                parseParens(builder);
            } else if (LEFT_SQUARE == token) {
                parseBrackets(builder);
            } else if (SYMBOL == token) {
                markAndAdvance(builder, VARIABLE_REFERENCE);
            } else {
                // TODO - Need some special handling around the underscore
                builder.advanceLexer();
            }
        }

        marker.error(message("parser.error.expectedRightBracket"));
    }

    /**
     * Enter: Lexer is pointed at the opening left paren
     * Exit: Lexer is pointed immediately after the closing right paren, or at the end-of-file
     */
    private void parseParens(PsiBuilder builder) {
        PsiBuilder.Marker marker = markAndAdvance(builder);

        IElementType token = builder.getTokenType();
        if (DEF == token) {
            parseDef(builder, marker);
        } else if (FN == token) {
            parseFn(builder, marker);
        } else if (MAC == token) {
            parseMac(builder, marker);
        } else if (EQ == token) {
            parseAssignment(builder, marker);
        } else if (LET == token) {
            parseLet(builder, marker);
        } else if (WITH == token) {
            parseWith(builder, marker);
        } else if (LITERALS.contains(token)) {
            // TODO - But this is OK if we're building a list, so we need to handle that situation...
            builder.error(message("parser.error.expectedFunctionOrMacro"));
            parseExpression(builder, marker, EXPRESSION);
        } else {
            parseExpression(builder, marker, EXPRESSION);
        }
    }

    /**
     * Enter: Lexer is pointed at the "let" token
     * Exit: Lexer is pointed immediately after the closing right paren, or at the end-of-file
     */
    private void parseLet(PsiBuilder builder, PsiBuilder.Marker marker) {
        builder.advanceLexer();
        markAndAdvance(builder, VARIABLE_DEFINITION);

        IElementType token = builder.getTokenType();
        if (token == RIGHT_PAREN) {
            builder.advanceLexer();
        } else if (token == LEFT_PAREN) {
            builder.advanceLexer();
            parseExpression(builder, builder.mark(), EXPRESSION);
        } else if (token == LEFT_SQUARE) {
            parseBrackets(builder);
        } else {
            builder.advanceLexer();
        }

        parseExpression(builder, marker, LET_BLOCK);
    }

    /**
     * Enter: Lexer is pointed at the "with" token
     * Exit: Lexer is pointed immediately after the closing right paren, or at the end-of-file
     */
    private void parseWith(PsiBuilder builder, PsiBuilder.Marker marker) {
        builder.advanceLexer();
        if (parseParameterList(builder, marker))
            return; // TODO - This isn't correct - the var initialization expressions shouldn't be tagged as parameters!
        parseExpression(builder, marker, WITH_BLOCK);
    }

    /**
     * Enter: Lexer is pointed at the "def" token
     * Exit: Lexer is pointed immediately after the closing right paren, or at the end-of-file
     */
    private void parseDef(PsiBuilder builder, PsiBuilder.Marker marker) {
        if (parseIdentifier(builder, marker)) return;
        if (parseParameterList(builder, marker)) return;

        // TODO - We need to get coloring to work for this.
        // TODO - We *could* just have a single string for the body, in which case this is *not* a docstring...
        if (STRING_LITERAL == builder.getTokenType()) {
            markAndAdvance(builder, DOCSTRING);
        }
        parseExpression(builder, marker, FUNCTION_DEFINITION);
    }

    /**
     * Enter: Lexer is pointed at the "fn" token
     * Exit: Lexer is pointed immediately after the closing right paren, or at the end-of-file
     */
    private void parseFn(PsiBuilder builder, PsiBuilder.Marker marker) {
        builder.advanceLexer();
        if (parseParameterList(builder, marker)) return;
        parseExpression(builder, marker, ANONYMOUS_FUNCTION_DEFINITION);
    }

    /**
     * Enter: Lexer is pointing at the token immediately preceding the identifier
     * Exit: Lexer is pointed immediately after the identifer, or at the end-of-file
     */
    private boolean parseIdentifier(PsiBuilder builder, PsiBuilder.Marker marker) {
        builder.advanceLexer();
        if (builder.eof()) {
            marker.error(message("parser.error.expectedIdentifier"));
            return true;
        }
        IElementType token = builder.getTokenType();
        PsiBuilder.Marker name = markAndAdvance(builder);

        // TODO - We should have the annotator warn you if a non-arcN.tar file redefines a special char or keyword
        if (SYMBOL != token && !KEYWORDS.contains(token) && !SPECIAL_CHARACTERS.contains(token)) {
            name.error(message("parser.error.expectedIdentifier"));
        } else {
            name.done(VARIABLE_DEFINITION);
        }
        return false;
    }

    /**
     * Enter : Lexer is pointed at first element of the expression, which will often be a left paren
     * Exit: Lexer will be pointed immediately after the closing right paren (or constant, or whatever) of the body, or at the end-of-file
     */
    private void parseExpression(PsiBuilder builder, PsiBuilder.Marker marker, IElementType type) {
        while (!builder.eof()) {
            IElementType token = builder.getTokenType();
            if (RIGHT_PAREN == token) {
                builder.advanceLexer();
                marker.done(type);
                return;
            } else if (LEFT_PAREN == token) {
                parseParens(builder);
            } else if (LEFT_SQUARE == token) {
                parseBrackets(builder);
            } else if (SYMBOL == token) {
                markAndAdvance(builder, VARIABLE_REFERENCE);
            } else {
                builder.advanceLexer();
            }
        }

        marker.error(message("parser.error.expectedRightParen"));
    }

    /**
     * Enter: Lexer is pointing at either the opening left paren of the parameter list, or else at the single "rest" parameter
     * Exit: Lexer is pointing immediately after the parameter list, or at the end-of-file
     */
    private boolean parseParameterList(PsiBuilder builder, PsiBuilder.Marker marker) {
        if (builder.eof()) {
            marker.error(message("parser.error.expectedParameterList"));
            return true;
        }

        PsiBuilder.Marker paramList = builder.mark();
        IElementType token = builder.getTokenType();
        if (RIGHT_PAREN == token) {
            builder.advanceLexer();
            paramList.error(message("parser.error.expectedParameterList"));
        } else if (LEFT_PAREN == token) {
            builder.advanceLexer();
            parseParameters(builder, paramList);
        } else if (LEFT_SQUARE == token) {
            parseBrackets(builder);
            paramList.error(message("parser.error.expectedParameterList"));
        } else {
            markAndAdvance(builder, REST_PARAMETER);
            paramList.done(PARAMETER_LIST);
        }
        return false;
    }

    /**
     * Enter: Lexer is pointed at the first token of the expression - in general, should be a function or macro call
     * Exit: Lexer is pointed immediatelytely after the closing right paren, or at the end-of-file
     */
    private void parseParameters(PsiBuilder builder, PsiBuilder.Marker marker) {
        while (!builder.eof()) {
            IElementType token = builder.getTokenType();
            if (RIGHT_PAREN == token) {
                builder.advanceLexer();
                marker.done(PARAMETER_LIST);
                return;
            } else if (LEFT_PAREN == token) { // TODO - This is an optional parameter - parse it correctly!
                parseParens(builder);
            } else if (LEFT_SQUARE == token) { // TODO - Isn't this an error?
                parseBrackets(builder);
            } else if (DOT == token) {
                builder.advanceLexer();
                markAndAdvance(builder, REST_PARAMETER); // TODO - THIS IS NOT WORKING!!!
            } else if (SYMBOL == token) {
                markAndAdvance(builder, PARAMETER);
            } else {
                builder.advanceLexer(); // TODO - Isn't this an error??
            }
        }

        marker.error(message("parser.error.expectedRightParen"));
    }

    /**
     * Enter: Lexer is pointed at the "mac" token
     * Exit: Lexer is pointed immediatelytely after the closing right paren, or at the end-of-file
     */
    private void parseMac(PsiBuilder builder, PsiBuilder.Marker marker) {
        if (parseIdentifier(builder, marker)) return;
        if (parseParameterList(builder, marker)) return;

        // TODO - We need to get coloring to work for this.
        // TODO - We *could* just have a single string for the body, in which case this is *not* a docstring...
        if (STRING_LITERAL == builder.getTokenType()) {
            markAndAdvance(builder, DOCSTRING);
        }

        parseExpression(builder, marker, MACRO_DEFINITION);
    }

    /**
     * Enter: Lexer is pointed at the "=" token
     * Exit: Lexer is pointed immediatelytely after the closing right paren, or at the end-of-file
     */
    private void parseAssignment(PsiBuilder builder, PsiBuilder.Marker marker) {
        builder.advanceLexer();
        if (builder.eof()) {
            marker.error(message("parser.error.expectedIdentifier"));
            return;
        }
        IElementType token = builder.getTokenType();
        PsiBuilder.Marker name = markAndAdvance(builder);
        if (LEFT_PAREN == token) {
            builder.advanceLexer();
            parseExpression(builder, name, EXPRESSION);
        } else if (SYMBOL == token) {
            name.done(VARIABLE_DEFINITION);
        } else {
            name.error(message("parser.error.expectedIdentifier"));
        }

        parseExpression(builder, marker, VARIABLE_ASSIGNMENT);
    }

    private PsiBuilder.Marker markAndAdvance(PsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        builder.advanceLexer();
        return marker;
    }

    private void markAndAdvance(PsiBuilder builder, IElementType type) {
        markAndAdvance(builder).done(type);
    }
}
