package com.bitbakery.plugin.arc;

import com.bitbakery.plugin.arc.lexer.ArcLexer;
import static com.bitbakery.plugin.arc.lexer.ArcTokenTypes.COMMENTS;
import static com.bitbakery.plugin.arc.lexer.ArcTokenTypes.LITERALS;
import static com.bitbakery.plugin.arc.psi.ArcElementTypes.VARIABLE_ASSIGNMENT_FILTER;
import com.bitbakery.plugin.arc.psi.Def;
import com.bitbakery.plugin.arc.psi.Mac;
import com.bitbakery.plugin.arc.psi.VariableDefinition;
import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Enables the "Find Usages" feature for Arc functions and macros.
 */
public class ArcFindUsagesProvider implements FindUsagesProvider {
    private WordsScanner wordsScanner;

    // TODO - We can't seem to find usages in different files, and yet navigation from reference to definition works. WTF??


    @Nullable
    public WordsScanner getWordsScanner() {
        if (wordsScanner == null) {
            wordsScanner = new DefaultWordsScanner(new ArcLexer(), VARIABLE_ASSIGNMENT_FILTER, COMMENTS, LITERALS);
        }
        return wordsScanner;
    }

    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return psiElement instanceof Def
                || psiElement instanceof Mac
                || psiElement instanceof VariableDefinition;
    }

    @Nullable
    public String getHelpId(@NotNull PsiElement psiElement) {
        return null;  // TODO - Create a JavaHelp file for the Arc plugin
    }

    @NotNull
    public String getType(@NotNull PsiElement element) {
        if (element instanceof Def) {
            return "def";
        } else if (element instanceof Mac) {
            return "mac";
        } else if (element instanceof VariableDefinition) {
            return "var";
        }
        return null;
    }

    public String getDescriptiveName(@NotNull PsiElement element) {
        if (element instanceof PsiNamedElement) {
            return ((PsiNamedElement) element).getName();
        }
        return null;
    }

    public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        if (element instanceof PsiNamedElement) {
            return element.getText();
        }
        return null;
    }
}
