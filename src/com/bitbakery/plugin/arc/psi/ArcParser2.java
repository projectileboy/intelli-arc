package com.bitbakery.plugin.arc.psi;

import static com.bitbakery.plugin.arc.ArcStrings.message;
import static com.bitbakery.plugin.arc.lexer.ArcTokenTypes.*;
import static com.bitbakery.plugin.arc.psi.ArcElementTypes.*;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private List<PsiBuilder.Marker> markerStack = new ArrayList<PsiBuilder.Marker>();


    @NotNull
    public ASTNode parse(IElementType root, PsiBuilder builder) {
        final PsiBuilder.Marker rootMarker = builder.mark();
        try {
            while (!builder.eof()) {
                parseExpression(builder);
            }
        } catch (EofException e) {
            for (PsiBuilder.Marker m : markerStack) {
                System.out.println(m.toString());
                m.drop();
            }
            markerStack.clear();
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
            PsiBuilder.Marker m = mark(lexer);
            advance(lexer);
            done(m, DOCSTRING);

            // If the string is the *entire* body of a def/mac/fn, then it is *not* a docstring...
            if (RIGHT_PAREN == lexer.getTokenType()) {
                drop(m); // TODO - Anything else??
            }
        }
    }

    public void parseExpression(PsiBuilder lexer) {
        if (SYMBOL == lexer.getTokenType()) {
            markTokenAndAdvance(lexer, VARIABLE_REFERENCE); // TODO - Third-pass annotator should check that the symbol actually resolves to something

        } else if (LEFT_PAREN == lexer.getTokenType()) {
            PsiBuilder.Marker m = mark(lexer);
            advance(lexer);

            ArcParser2 parser = parsers.get(lexer.getTokenType());
            if (parser == null) {
                parser = defaultParser;
            } else {
                advance(lexer);
            }
            done(m, parser.parse(lexer));

        } else if (LEFT_SQUARE == lexer.getTokenType()) {
            PsiBuilder.Marker m = mark(lexer);
            advance(lexer);
            while (RIGHT_SQUARE != lexer.getTokenType()) {
                parseExpression(lexer);
            }
            done(m, SINGLE_ARG_ANONYMOUS_FUNCTION_DEFINITION);

        } else {
            // TODO - All of this tomfoolery is a first attempt at handling macro template stuff (, ,@ ' `)
            IElementType elementType = tokenMap.get(lexer.getTokenType());
            if (elementType != null) {
                PsiBuilder.Marker marker = mark(lexer);
                advance(lexer);
                parseExpression(lexer);
                done(marker, elementType);
            } else {
                // For literals, tilde, composer, and unknown tokens, we simply advance the lexer
                advance(lexer);
            }
        }
    }


    public void parseFormalParameters(PsiBuilder lexer) {
        PsiBuilder.Marker m = mark(lexer);
        if (SYMBOL == lexer.getTokenType()) {
            markTokenAndAdvance(lexer, REST_PARAMETER);
            done(m, PARAMETER_LIST);

        } else if (LEFT_PAREN == lexer.getTokenType()) {
            advance(lexer);
            while (RIGHT_PAREN != lexer.getTokenType()) {
                parseFormalParameter(lexer);
            }
            advance(lexer);
            done(m, PARAMETER_LIST);

        } else {
            advance(lexer);
            error(m, message("parser.error.expectedParameterList"));
        }
    }

    public void parseFormalParameter(PsiBuilder lexer) {
        if (SYMBOL == lexer.getTokenType()) {
            markTokenAndAdvance(lexer, PARAMETER);

        } else if (DOT == lexer.getTokenType()) {
            parseTrailingRestParameter(lexer);

        } else if (LEFT_PAREN == lexer.getTokenType()) {
            parseOptionalParameter(lexer);

        } else {
            // TODO - We should be flagging an error here of some sort...
            advance(lexer);
        }
    }

    private void parseTrailingRestParameter(PsiBuilder lexer) {
        advance(lexer);
        if (SYMBOL == lexer.getTokenType()) {
            markTokenAndAdvance(lexer, REST_PARAMETER);
        } else {
            lexer.error(message("parser.error.expectedIdentifier"));
        }
    }

    private void parseOptionalParameter(PsiBuilder lexer) {

        PsiBuilder.Marker m = mark(lexer);

        advance(lexer); // Move past the left paren
        advance(lexer); // Move past the 'o'

        markTokenAndAdvance(lexer, PARAMETER);

        if (RIGHT_PAREN != lexer.getTokenType()) {
            parseExpression(lexer);
        }

        // TODO - Verify that we're at a right paren; flag it as an error if we're not!
        advance(lexer);

        done(m, OPTIONAL_PARAMETER);
    }

    public void parseLocalVariable(PsiBuilder builder) {
        parseName(builder);
        parseExpression(builder);
    }

    private void advance(PsiBuilder builder) {
        if (builder.eof()) throw new EofException();
        builder.advanceLexer();
    }

    private void markTokenAndAdvance(PsiBuilder lexer, IElementType type) {
        PsiBuilder.Marker m = mark(lexer);
        advance(lexer);
        done(m, type);
    }

    public PsiBuilder.Marker mark(PsiBuilder builder) {
        PsiBuilder.Marker m = builder.mark();
        markerStack.add(m);
        return m;
    }

    public void error(PsiBuilder.Marker m, String s) {
        m.error(s);
        markerStack.remove(m);
    }

    public void done(PsiBuilder.Marker m, IElementType e) {
        m.done(e);
        markerStack.remove(m);
    }

    public void drop(PsiBuilder.Marker m) {
        m.drop();
        markerStack.remove(m);
    }

    private static class EofException extends RuntimeException {
    }


    private static class AssignmentParser extends ArcParser2 {
        /* TODO - AssignmentParser is broken, because this is legit:

        arc> (= s "foo")
        "foo"
        arc> (= (s 0) #\m)
        #\m

        */

        public IElementType parse(PsiBuilder builder) {
            parseName(builder);
            super.parse(builder); // TODO - This is incorrect here... We should enforce only one expression
            return VARIABLE_ASSIGNMENT; // TODO - This is only true if the element after = is a symbol!

//            while (RIGHT_PAREN != builder.getTokenType()) {
//                super.parseLocalVariable(builder);
//            }
//            return VARIABLE_ASSIGNMENT; // TODO - This is only true if the element after = is a symbol!
        }
    }

    private static class LetParser extends ArcParser2 {
        public IElementType parse(PsiBuilder builder) {
            parseLocalVariable(builder);
            super.parse(builder);
            return LET_BLOCK;
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
        public IElementType parse(PsiBuilder builder) {
            super.parse(builder);
            return MACRO_DEFINITION;
        }

        // TODO - Override parseExpression to do the right thing within macro bodies, which are... weird... (` ' , ,@) 
    }
}