package com.bitbakery.plugin.arc.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class OptionalParameter extends Parameter {
    public OptionalParameter(@NotNull final ASTNode node) {
        super(node);
    }

    // TODO - Need to wrap the whole parens as "optional parameter", and getName needs to pull from the child "named" element

    @Override
    public String getName() {
        for (PsiElement child : getChildren()) {
            if (child instanceof Parameter) {
                return child.getText();                
            }
        }
        return null;
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