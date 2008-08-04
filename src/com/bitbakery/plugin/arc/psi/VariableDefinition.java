package com.bitbakery.plugin.arc.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * A variable definition element defines a variable withot actually assigning a value to that variable.
 *   Essentially, this breaks down to function and macro formal parameters, as well as let and with variables. 
 */
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

    // TODO - This is duplicated in VariableAssignment... 
    public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            public String getPresentableText() {
                return getName();
            }

            @Nullable
            public String getLocationString() {
                return getNode().getPsi().getContainingFile().getName();
            }

            @Nullable
            public Icon getIcon(boolean open) {
                return null; // VariableAssignment.this.getIcon(0);
            }

            @Nullable
            public TextAttributesKey getTextAttributesKey() {
                return null;  // TODO - Investigate different fonts/colors/etc.for def vs. mac...?
            }
        };
    }
}
