package com.bitbakery.plugin.arc.nav;

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
import static com.bitbakery.plugin.arc.lexer.ArcTokenTypes.COMMENTS;
import static com.bitbakery.plugin.arc.lexer.ArcTokenTypes.LITERALS;
import static com.bitbakery.plugin.arc.psi.ArcElementTypes.VARIABLE_REFERENCE_FILTER;
import com.bitbakery.plugin.arc.psi.*;
import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Enables the "Find Usages" feature for Arc functions and macros.
 *
 *
 * TODO - <sigh>... OK... here's everything we need to fix around navigation and find usages:
 * TODO     - I need to consistently differentiate between VariableAssignment (global var def - i.e., an = expression) and VariableDefinition
 * TODO     - I need to be able to find usages for a VariableAssignment, a Def, and a Mac
 * TODO     - I need to be able to find usages for a def/mac/fn/[_] parameter
 * TODO     - I need to be able to navigate to a parameter definition from a parameter reference
 * TODO     - If a variable is defined more than once within scope, then I need to present these as multiple target options when I'm navigating
 * TODO     - Parameters are showing up as variable assignments, and hence being found globally - we need to do find usages - and do reference navigations - appropriately for the scope
 * TODO     - Related to all this is the need to properly handle (or properly ignore?) macro template stuff
 */
public class ArcFindUsagesProvider implements FindUsagesProvider {
    private WordsScanner wordsScanner;

    // TODO - We can't seem to find usages in different files, and yet navigation from reference to definition works. WTF??


    @Nullable
    public WordsScanner getWordsScanner() {
        if (wordsScanner == null) {
            wordsScanner = new DefaultWordsScanner(new ArcLexer(), VARIABLE_REFERENCE_FILTER, COMMENTS, LITERALS);
        }
        return wordsScanner;
    }

    public boolean canFindUsagesFor(@NotNull PsiElement el) {
        return el instanceof Def || el.getParent() instanceof Def
                || el instanceof Mac || el.getParent() instanceof Mac
                || el instanceof VariableAssignment || el.getParent() instanceof VariableAssignment
                || el instanceof Parameter;
    }

    @Nullable
    public String getHelpId(@NotNull PsiElement psiElement) {
        return null;  // TODO - Create a JavaHelp file for the Arc plugin
    }

    @NotNull
    public String getType(@NotNull PsiElement element) {
        if (element instanceof Def || element.getParent() instanceof Def) {
            return "Function";
        } else if (element instanceof Mac || element.getParent() instanceof Mac) {
            return "Macro";
        } else if (element instanceof VariableAssignment || element.getParent() instanceof VariableAssignment) {
            return "Global variable";
        } else if (element instanceof Parameter) {
            return "Parameter";
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
