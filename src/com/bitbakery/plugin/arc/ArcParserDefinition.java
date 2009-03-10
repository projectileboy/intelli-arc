package com.bitbakery.plugin.arc;

/*
 * Copyright (c) Kurt Christensen, 2009
 *
 *  Licensed under the Artistic License, Version 2.0 (the "License"); you may not use this
 *  file except in compliance with the License. You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/artistic-license-2.0.php
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under
 *  the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 *  OF ANY KIND, either express or implied. See the License for the specific language
 *  governing permissions and limitations under the License..
 */

import com.bitbakery.plugin.arc.lexer.ArcLexer;
import static com.bitbakery.plugin.arc.lexer.ArcTokenTypes.*;
import com.bitbakery.plugin.arc.psi.*;
import static com.bitbakery.plugin.arc.psi.ArcElementTypes.*;
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
        return FILE;
    }

    @NotNull
    public TokenSet getWhitespaceTokens() {
        return TokenSet.create(WHITESPACE, EOL, EOF);
    }

    @NotNull
    public TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @NotNull
    public TokenSet getStringLiteralElements() {
        return TokenSet.create(STRING_LITERAL); // TODO - Not sure if this is complete
    }

    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        // TODO - Is this right? Are there spacing requirements in Lisp/Scheme/Arc that I'm not aware of?
        if (left.getElementType() == COMMA
                || left.getElementType() == COMMA_AT
                || left.getElementType() == QUOTE
                || left.getElementType() == BACKQUOTE
                || left.getElementType() == COMPOSER
                || right.getElementType() == COMPOSER) {

            return SpaceRequirements.MUST_NOT;

        } else if (left.getElementType() == LEFT_PAREN
                || right.getElementType() == RIGHT_PAREN
                || left.getElementType() == RIGHT_PAREN
                || right.getElementType() == LEFT_PAREN

                || left.getElementType() == LEFT_SQUARE
                || right.getElementType() == RIGHT_SQUARE
                || left.getElementType() == RIGHT_SQUARE
                || right.getElementType() == LEFT_SQUARE) {

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
        if (type == FILE) {
            return new Root(node);
        } else if (type == FUNCTION_DEFINITION) {
            return new Def(node);
        } else if (type == ANONYMOUS_FUNCTION_DEFINITION) {
            return new Fn(node);
        } else if (type == SINGLE_ARG_ANONYMOUS_FUNCTION_DEFINITION) {
            return new SingleArgFn(node);
        } else if (type == MACRO_DEFINITION) {
            return new Mac(node);
        } else if (type == EXPRESSION) {
            return new Expression(node);
        } else if (type == VARIABLE_ASSIGNMENT) {
            return new VariableAssignment(node);
        } else if (type == OPTIONAL_PARAMETER) {
            return new OptionalParameter(node);
        } else if (type == REST_PARAMETER) {
            return new RestParameter(node);
        } else if (type == PARAMETER) {
            return new Parameter(node);
        } else if (type == VARIABLE_DEFINITION) {
            return new VariableDefinition(node);
        } else if (type == VARIABLE_REFERENCE) {
            return new VariableReference(node);
        } else if (type == IF_BLOCK) {
            return new If(node);
        } else if (type == LET_BLOCK) {
            return new Let(node);
        } else if (type == WITH_BLOCK) {
            return new With(node);
        } else if (type == PARAMETER_LIST) {
            return new ParameterList(node);
        } else if (type == DOCSTRING) {
            return new Docstring(node);
        } else if (type == BACKQUOTED_EXPRESSION) {
            return new BackquotedExpression(node);
        } else if (type == QUOTED_EXPRESSION) {
            return new QuotedExpression(node);
        } else if (type == COMMA_AT_EXPRESSION) {
            return new CommaAtExpression(node);
        } else if (type == COMMA_EXPRESSION) {
            return new CommaExpression(node);
        } else if (type == LITERAL) {
            return new Literal(node);
        }

        return new ASTWrapperPsiElement(node);
    }
}
