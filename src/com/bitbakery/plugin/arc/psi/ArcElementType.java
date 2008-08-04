package com.bitbakery.plugin.arc.psi;

import com.bitbakery.plugin.arc.ArcFileType;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;

/**
 * Simple wrapper for IElementType which enables Arc PSI elements to own theirown parsing
 */
public class ArcElementType extends IElementType {

    public ArcElementType(@NonNls String debugName) {
        super(debugName, ArcFileType.ARC);
    }

    @SuppressWarnings({"HardCodedStringLiteral"})
    public String toString() {
        return "Arc:" + super.toString();
    }

    /**
     * Concrete subclasses tell ArcParser how they should be broken down into PSI elements.
     */
    public PsiBuilder.Marker parse(PsiBuilder builder) {
        // By default, we do nothing...
        advanceLexer(builder);
        return null;
    }

    /**
     * Advances the lexer if we haven't fallen off the end of the token stream
     */
    protected void advanceLexer(PsiBuilder builder) {
        if (builder.getTokenType() != null) {
            builder.advanceLexer();
        }
    }
}
