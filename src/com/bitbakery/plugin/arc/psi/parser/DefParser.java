package com.bitbakery.plugin.arc.psi.parser;

import static com.bitbakery.plugin.arc.ArcStrings.message;
import static com.bitbakery.plugin.arc.lexer.ArcTokenTypes.*;
import static com.bitbakery.plugin.arc.psi.ArcElementTypes.*;
import com.intellij.lang.PsiBuilder;

public class DefParser {

    public void parse(PsiBuilder builder) {

        // lexer --> (

        PsiBuilder.Marker def = builder.mark();

        builder.advanceLexer();

        // lexer --> def

        builder.advanceLexer();

        // lexer --> identifier

        PsiBuilder.Marker name = builder.mark();

        if (!SYMBOL.equals(builder.getTokenType())) {
            name.error(message("parser.error.expectedIdentifier"));
        } else {
            builder.advanceLexer();
            name.done(VARIABLE_DEFINITION);
        }

        // lexer --> formal parameter list : either

        
        if (SYMBOL.equals(builder.getTokenType())) {
            name.error(message("parser.error.expectedIdentifier"));
        } else {
            builder.advanceLexer();
            name.done(VARIABLE_DEFINITION);
        }


        if (!RIGHT_PAREN.equals(builder.getTokenType())) {
            def.error(message("parser.error.expectedRightParen"));
        } else {
            builder.advanceLexer();
            def.done(FUNCTION_DEFINITION);
        }
    }
}