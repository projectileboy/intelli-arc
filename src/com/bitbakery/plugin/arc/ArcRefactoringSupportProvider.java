package com.bitbakery.plugin.arc;

import com.intellij.lang.refactoring.InlineHandler;
import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import com.intellij.refactoring.RefactoringActionHandler;
import org.jetbrains.annotations.Nullable;

/**
 * TODO - Describe roles and responsibilities
 */
public class ArcRefactoringSupportProvider implements RefactoringSupportProvider {
    public boolean isSafeDeleteAvailable(PsiElement element) {
        return true;
    }

    @Nullable
    public RefactoringActionHandler getIntroduceVariableHandler() {
        return null;  // TODO - Do something!
    }

    @Nullable
    public RefactoringActionHandler getExtractMethodHandler() {
        return null;  // TODO - Do something!
    }

    @Nullable
    public InlineHandler getInlineHandler() {
        return null;  // TODO - Do something!
    }
}
