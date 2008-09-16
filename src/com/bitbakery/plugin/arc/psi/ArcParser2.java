package com.bitbakery.plugin.arc.psi;

import static com.bitbakery.plugin.arc.ArcStrings.message;
import static com.bitbakery.plugin.arc.lexer.ArcTokenTypes.*;
import static com.bitbakery.plugin.arc.psi.ArcElementTypes.*;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Consumes a stream of Arc tokens and generates a PSI tree for an Arc file
 */
public class ArcParser2 implements PsiParser {

    private static ArcParser2 defaultParser = new ArcParser2();
    private static Map<IElementType, ArcParser2> parsers = new HashMap<IElementType, ArcParser2>();
    private static Map<IElementType, IElementType> tokenMap = new HashMap<IElementType, IElementType>();

    static {
        parsers = new HashMap<IElementType, ArcParser2>();
        parsers.put(LET, new LetParser());
        parsers.put(WITH, new WithParser());
        parsers.put(FN, new FnParser());
        parsers.put(DEF, new DefParser());
        parsers.put(MAC, new MacParser());
        parsers.put(EQ, new AssignmentParser());

        tokenMap = new HashMap<IElementType, IElementType>();
        tokenMap.put(QUOTE, QUOTED_EXPRESSION);
        tokenMap.put(BACKQUOTE, BACKQUOTED_EXPRESSION);
        tokenMap.put(COMMA, COMMA_EXPRESSION);
        tokenMap.put(COMMA_AT, COMMA_AT_EXPRESSION);
    }

    @NotNull
    public ASTNode parse(IElementType root, PsiBuilder builder) {
        builder.setDebugMode(true);
        final PsiBuilder.Marker rootMarker = builder.mark();
        try {
            while (!builder.eof()) {
                parseExpression(builder);
            }
        } catch (EofException e) {
            // TODO - So, we need to drop any markers we haven't closed if we hit the eof. But making EofException is... ugly. Plus, we do NOT want to check for EOF every single little time. Especially at the end of a legit eval. Sooo... what??

            //builder.error("Unexpected end-of-file");
            // TODO - Anything...? Presumably an error marker...?
        }
        rootMarker.done(root);
        return builder.getTreeBuilt();
    }


    public IElementType parse(PsiBuilder lexer) {
        while (RIGHT_PAREN != lexer.getTokenType()) {
            parseExpression(lexer);
        }
        advance(lexer);
        return EXPRESSION;
    }

    public void parseName(PsiBuilder lexer) {
        markTokenAndAdvance(lexer, VARIABLE_DEFINITION);
    }

    public void parseDocstring(PsiBuilder lexer) {
        // TODO - We need to get coloring to work for this.
        if (STRING_LITERAL == lexer.getTokenType()) {
            PsiBuilder.Marker marker = lexer.mark();
            advance(lexer);
            marker.done(DOCSTRING);

            // If the string is the *entire* body of a def/mac/fn, then it is *not* a docstring...
            if (RIGHT_PAREN == lexer.getTokenType()) {
                marker.drop(); // TODO - Anything else??
            }
        }
    }

    public void parseExpression(PsiBuilder lexer) {
        if (SYMBOL == lexer.getTokenType()) {
            markTokenAndAdvance(lexer, VARIABLE_REFERENCE); // TODO - Third-pass annotator should check that the symbol actually resolves to something

        } else if (LEFT_PAREN == lexer.getTokenType()) {
            PsiBuilder.Marker m = lexer.mark();
            advance(lexer);

            // TODO - We're not accounting for function/macro calls, or quoted lists, or...
            ArcParser2 parser = parsers.get(lexer.getTokenType());
            if (parser == null) {
                parser = defaultParser;
            } else {
                advance(lexer);
            }
            m.done(parser.parse(lexer));

        } else if (LEFT_SQUARE == lexer.getTokenType()) {
            PsiBuilder.Marker m = lexer.mark();
            advance(lexer);
            while (RIGHT_SQUARE != lexer.getTokenType()) {
                parseExpression(lexer);
            }
            m.done(SINGLE_ARG_ANONYMOUS_FUNCTION_DEFINITION);
        } else {
            IElementType elementType = tokenMap.get(lexer.getTokenType());
            if (elementType != null) {
                PsiBuilder.Marker marker = lexer.mark();
                advance(lexer);
                parseExpression(lexer);
                marker.done(elementType);
            } else {
                // For literals, tilde, composer, and unknown tokens, we simply advance the lexer
                advance(lexer);
            }
        }
    }


    public void parseFormalParameters(PsiBuilder lexer) throws EofException {
        PsiBuilder.Marker paramList = lexer.mark();
        try {
            if (SYMBOL == lexer.getTokenType()) {
                markTokenAndAdvance(lexer, REST_PARAMETER);
                paramList.done(PARAMETER_LIST);

            } else if (LEFT_PAREN == lexer.getTokenType()) {
                advance(lexer);
                while (RIGHT_PAREN != lexer.getTokenType()) {
                    parseFormalParameter(lexer);
                }
                advance(lexer);
                paramList.done(PARAMETER_LIST);

            } else {
                advance(lexer);
                paramList.error(message("parser.error.expectedParameterList"));
            }
        } catch (EofException e) {
            paramList.error("parser.error.unexpectedEof");
            throw e;
        }
    }

    public void parseFormalParameter(PsiBuilder lexer) {
        if (SYMBOL == lexer.getTokenType()) {
            markTokenAndAdvance(lexer, PARAMETER);

        } else if (DOT == lexer.getTokenType()) {
            lexer.advanceLexer();
            if (SYMBOL == lexer.getTokenType()) {
                markTokenAndAdvance(lexer, REST_PARAMETER);
            } else {
                lexer.error(message("parser.error.expectedIdentifier"));
            }

        } else if (LEFT_PAREN == lexer.getTokenType()) {
            // TODO - Parse optional parameter
//            advance(builder);
//            while (RIGHT_PAREN != builder.getTokenType()) {
//                parseFormalParameter(builder);
//                advance(builder);
//            }
            advance(lexer);

        } else {
            advance(lexer);
        }
    }

    private void advance(PsiBuilder builder) {
        if (builder.eof()) throw new EofException();
        builder.advanceLexer();
    }

    private void markTokenAndAdvance(PsiBuilder lexer, IElementType type) {
        PsiBuilder.Marker marker = lexer.mark();
        advance(lexer);
        marker.done(type);
    }

    private static class EofException extends RuntimeException {
    }


    private static class AssignmentParser extends ArcParser2 {
        public IElementType parse(PsiBuilder builder) {
            parseName(builder);
            super.parse(builder); // TODO - This is incorrect here... We should enforce only one expression
            return VARIABLE_ASSIGNMENT;
        }
    }

    private static class LetParser extends ArcParser2 {
        public IElementType parse(PsiBuilder builder) {
            parseLocalVariable(builder);
            super.parse(builder);
            return LET_BLOCK;
        }

        public void parseLocalVariable(PsiBuilder builder) {
            parseName(builder);
            parseExpression(builder);
        }
    }

    private static class WithParser extends LetParser {
        public void parseLocalVariable(PsiBuilder builder) {
            while (RIGHT_PAREN != builder.getTokenType()) {
                super.parseLocalVariable(builder);
            }
        }
    }

    private static class FnParser extends ArcParser2 {
        public IElementType parse(PsiBuilder builder) {
            parseFormalParameters(builder);
            super.parse(builder);
            return ANONYMOUS_FUNCTION_DEFINITION;
        }
    }

    private static class DefParser extends ArcParser2 {
        public IElementType parse(PsiBuilder builder) {
            parseName(builder);
            parseFormalParameters(builder);
            parseDocstring(builder);
            super.parse(builder);
            return FUNCTION_DEFINITION;
        }
    }

    private static class MacParser extends DefParser {
        // TODO - This guy is a little different, in that we can have macro-y stuff in the body (e.g., backquoted expressions)
    }
}