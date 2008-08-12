package com.bitbakery.plugin.arc;


import com.bitbakery.plugin.arc.lexer.ArcTokenTypes;
import com.intellij.lang.Language;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;


public class ArcLanguage extends Language {
    public ArcLanguage() {
        super("Arc");
    }

    @NotNull
    public TokenSet getReadableTextContainerElements() {
        return ArcTokenTypes.READABLE_TEXT;
    }
}

