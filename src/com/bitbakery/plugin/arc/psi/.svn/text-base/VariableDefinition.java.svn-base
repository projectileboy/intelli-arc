package com.bitbakery.plugin.arc.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class VariableDefinition extends ArcElement implements PsiNamedElement {
    public VariableDefinition(@NotNull final ASTNode node) {
        super(node);
    }

    public String getName() {
        return getText();
    }

    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
        // TODO - Make me real to support rename refactorings!
        return this;
    }

    /*
    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
        final ASTNode nameElement = createNameIdentifier(getProject(), name);
        getNode().replaceChild(getNode().getFirstChildNode(), nameElement);
        return this;
    }

    private ASTNode createNameIdentier(Project project, String name) {
        ParserDefinition def = ArcSupportLoader.ARC.getLanguage().getParserDefinition();
        final PsiFile dummyFile = def.createFile(project., "dummy." + ArcSupportLoader.ARC.getDefaultExtension(), name + ";");
        final JSExpressionStatement expressionStatement = (JSExpressionStatement) dummyFile.getFirstChild();
        final JSReferenceExpressionImpl refExpression = (JSReferenceExpressionImpl) expressionStatement.getFirstChild();

        return refExpression.getNode().getFirstChildNode();
    }
*/
}
