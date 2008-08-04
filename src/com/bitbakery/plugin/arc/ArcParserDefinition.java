package com.bitbakery.plugin.arc;

import com.bitbakery.plugin.arc.lexer.ArcLexer;
import com.bitbakery.plugin.arc.lexer.ArcTokenTypes;
import com.bitbakery.plugin.arc.psi.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

/**
 * Defines the implementation of our Arc file parser. Note that the real parsing guts are in ArcParser.
 */
public class ArcParserDefinition implements ParserDefinition {
    @NotNull
    public Lexer createLexer(Project project) {
        return new ArcLexer();
    }

    public PsiParser createParser(Project project) {
        return new ArcParser();
    }

    public IFileElementType getFileNodeType() {
        return ArcElementTypes.FILE;
    }

    @NotNull
    public TokenSet getWhitespaceTokens() {
        return TokenSet.create(ArcTokenTypes.WHITESPACE, ArcTokenTypes.EOL, ArcTokenTypes.EOF);
    }

    @NotNull
    public TokenSet getCommentTokens() {
        return ArcTokenTypes.COMMENTS;
    }

    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        // TODO - Is this right? Are there spacing requirements in Lisp/Scheme/Arc that I'm not aware of?
        if (left.getElementType() == ArcTokenTypes.COMMA
                || left.getElementType() == ArcTokenTypes.COMMA_AT
                || left.getElementType() == ArcTokenTypes.QUOTE
                || left.getElementType() == ArcTokenTypes.BACKQUOTE
                || left.getElementType() == ArcTokenTypes.COMPOSER
                || right.getElementType() == ArcTokenTypes.COMPOSER) {

            return SpaceRequirements.MUST_NOT;

        } else if (left.getElementType() == ArcTokenTypes.LEFT_PAREN
                || right.getElementType() == ArcTokenTypes.RIGHT_PAREN
                || left.getElementType() == ArcTokenTypes.RIGHT_PAREN
                || right.getElementType() == ArcTokenTypes.LEFT_PAREN

                || left.getElementType() == ArcTokenTypes.LEFT_SQUARE
                || right.getElementType() == ArcTokenTypes.RIGHT_SQUARE
                || left.getElementType() == ArcTokenTypes.RIGHT_SQUARE
                || right.getElementType() == ArcTokenTypes.LEFT_SQUARE) {

            return SpaceRequirements.MAY;
        }
        return SpaceRequirements.MUST;
    }

    public PsiFile createFile(FileViewProvider viewProvider) {
        return new ArcFile(viewProvider);
    }

    @NotNull
    public PsiElement createElement(ASTNode node) {
        final IElementType type = node.getElementType();
        if (type == ArcElementTypes.FILE) {
            return new Root(node);
        } else if (type == ArcElementTypes.FUNCTION_DEFINITION) {
            return new Def(node);
        } else if (type == ArcElementTypes.ANONYMOUS_FUNCTION_DEFINITION) {
            return new Fn(node);
        } else if (type == ArcElementTypes.SINGLE_ARG_ANONYMOUS_FUNCTION_DEFINITION) {
            return new SingleArgFn(node);
        } else if (type == ArcElementTypes.MACRO_DEFINITION) {
            return new Mac(node);
        } else if (type == ArcElementTypes.EXPRESSION) {
            return new Expression(node);
        } else if (type == ArcElementTypes.VARIABLE_ASSIGNMENT) {
            return new VariableAssignment(node);
        } else if (type == ArcElementTypes.OPTIONAL_PARAMETER) {
            return new OptionalParameter(node);
        } else if (type == ArcElementTypes.REST_PARAMETER) {
            return new RestParameter(node);
        } else if (type == ArcElementTypes.PARAMETER) {
            return new Parameter(node);
        } else if (type == ArcElementTypes.VARIABLE_DEFINITION) {
            return new VariableDefinition(node);
        } else if (type == ArcElementTypes.VARIABLE_REFERENCE) {
            return new VariableReference(node);
        } else if (type == ArcElementTypes.LET_BLOCK) {
            return new Let(node);
        } else if (type == ArcElementTypes.WITH_BLOCK) {
            return new With(node);
        } else if (type == ArcElementTypes.PARAMETER_LIST) {
            return new ParameterList(node);
        } else if (type == ArcElementTypes.DOCSTRING) {
            return new Docstring(node);
        }

        return new ASTWrapperPsiElement(node);
    }
}
