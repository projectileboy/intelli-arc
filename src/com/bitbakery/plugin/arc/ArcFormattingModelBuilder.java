package com.bitbakery.plugin.arc;

import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.openapi.util.TextRange;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

/**
 * TODO: Describe the role(s) and responsibilit(ies)
 */
public class ArcFormattingModelBuilder implements FormattingModelBuilder {
    @NotNull
    public FormattingModel createModel(PsiElement element, CodeStyleSettings settings) {
        // TODO - Look at the Javascript plugin to see how this works...
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset) {
        // TODO - Look at the Javascript plugin to see how this works...
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
