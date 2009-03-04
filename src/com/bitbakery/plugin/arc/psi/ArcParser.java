package com.bitbakery.plugin.arc.psi;

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

import static com.bitbakery.plugin.arc.lexer.ArcTokenTypes.*;
import static com.bitbakery.plugin.arc.psi.ArcElementTypes.*;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.Stack;
import org.jetbrains.annotations.NotNull;

/**
 * Walk through the token stream of a Arc source file and generate the appropriate PSI tree.
 */
public class ArcParser implements PsiParser {

    private Stack<PsiBuilder.Marker> markers = new Stack<PsiBuilder.Marker>();
    private PsiBuilder builder;

    @NotNull
    public ASTNode parse(IElementType root, PsiBuilder builder) {
        this.builder = builder;

        // TODO - Delete me!
        builder.setDebugMode(true);

        
        final PsiBuilder.Marker rootMarker = builder.mark();

        try {
            while (!builder.eof()) {
                parseNext();
            }
        } catch (EofException e) {
            while (!markers.empty()) {
                drop();
            }
        }

        rootMarker.done(root);
        return builder.getTreeBuilt();
    }

    private void parseNext() {
        if (isAt(LEFT_PAREN)) {
            markAndAdvance();
            IElementType type = getExpressionType();
            if (isAt(DEF) || isAt(MAC)) {
                advance(); // Advance past def/mac token
                parseName();
                parseParameters();
                parseDocstring();
            } else if (isAt(EQ)) {
                advance(); // Advance past = token
                parseVarName();
            }
            parseBody(RIGHT_PAREN);
            done(type);

        } else if (isAt(LEFT_SQUARE)) {
            markAndAdvance();
            parseBody(RIGHT_SQUARE);
            done(SINGLE_ARG_ANONYMOUS_FUNCTION_DEFINITION);

        } else if (isAt(SYMBOL)) {
            markAndAdvance(VARIABLE_REFERENCE);

        } else {
            advance();
        }
    }

    private void parseBody(IElementType teminator) {
        while (!isAt(teminator)) {
            parseNext();
        }
        advance();
    }

    private void parseVarName() {
        if (isAt (SYMBOL)) {
            markAndAdvance(VARIABLE_DEFINITION);
        }
    }

    private void parseName() {
        if (isAt (SYMBOL)) {
            markAndAdvance(VARIABLE_DEFINITION);
        } else {
            builder.error("Expected identifier"); // TODO - Prop-ify me!!
        }
    }

    private void parseParameters() {
        mark();
        if (isAt(SYMBOL)) {
            markAndAdvance(REST_PARAMETER);
        } else if (isAt(LEFT_PAREN)) {
            advance();
            while (!isAt(RIGHT_PAREN)) {
                parseParameter();
            }
            advance();
        } else {
            advance();
            builder.error("Expected parameter");
        }
        done(PARAMETER_LIST);
    }

    private void parseParameter() {
        if (isAt(SYMBOL)) {
            markAndAdvance(PARAMETER);
        } else if (isAt(LEFT_PAREN)) {
            parseOptionalParameter();
        } else if (isAt(DOT)) {
            advance();
            markAndAdvance(REST_PARAMETER);
        } else {
            advance();
            builder.error("Expected parameter");
        }
    }

    private void parseOptionalParameter() {
        advance(); // Advance past left paren
        advance(); // Advance past 'o' token
        parseName();
        parseBody(RIGHT_PAREN);
    }

    private void parseDocstring() {
        if (isAt(STRING_LITERAL)) {
            markAndAdvance();

            // If the string is the *entire* body of a def/mac/fn, then it is *not* a docstring...
            if (isAt(RIGHT_PAREN)) {
                drop();
            } else {
                done(DOCSTRING);
            }
        }
    }

    private void done(IElementType type) {
        try {
            markers.pop().done(type);
        } catch (RuntimeException e) {
            throw new RuntimeException(builder.getTokenText(), e);
        }
    }

    private void drop() {
        try {
            markers.pop().drop();
        } catch (RuntimeException e) {
            throw new RuntimeException(builder.getTokenText(), e);
        }
    }

    private void mark() {
        markers.push(builder.mark());
    }

    private void advance() {
        if (builder.eof()) {
            throw new EofException();
        }
        builder.advanceLexer();
    }

    private void markAndAdvance(IElementType type) {
        markAndAdvance();
        done(type);
    }

    private void markAndAdvance() {
        mark();
        advance();
    }

    private boolean isAt(IElementType token) {
        return builder.getTokenType() == token;
    }

    private IElementType getExpressionType() {
        if (isAt(DEF)) {
            return FUNCTION_DEFINITION;
        } else if (isAt(MAC)) {
            return MACRO_DEFINITION;
        } else if (isAt(FN)) {
            return ANONYMOUS_FUNCTION_DEFINITION;
        } else if (isAt(EQ)) {
            return VARIABLE_ASSIGNMENT;
        }
        return EXPRESSION;
    }

    private class EofException extends RuntimeException {
    }
}