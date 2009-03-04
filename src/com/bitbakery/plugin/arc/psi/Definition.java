package com.bitbakery.plugin.arc.psi;

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

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import static com.bitbakery.plugin.arc.psi.ArcElementTypes.*;

/**
 * Container for stuff shared by Def and Mac
 */
public abstract class Definition extends VariableAssignment {

    public Definition(ASTNode node) {
        super(node);
    }

    public String getDocstring() {
        ASTNode[] children = getNode().getChildren(TokenSet.create(ArcElementTypes.DOCSTRING));
        if (children != null && children.length > 0) {
            return stripQuotes(children[0].getText());
        }
        return null;
    }

    public boolean isValidParameterCount(int actualParamCount) {
        ASTNode[] params = getNode().getChildren(com.bitbakery.plugin.arc.psi.ArcElementTypes.PARAM_LIST_FILTER);

        int lowerBound = params[0].getChildren(com.bitbakery.plugin.arc.psi.ArcElementTypes.PARAM_FILTER).length;
        int optionalParamCount = params[0].getChildren(com.bitbakery.plugin.arc.psi.ArcElementTypes.OPTIONAL_PARAM_FILTER).length;
        int restParamCount = params[0].getChildren(com.bitbakery.plugin.arc.psi.ArcElementTypes.REST_PARAM_FILTER).length;

        int upperBound = restParamCount > 0 ? Integer.MAX_VALUE : lowerBound + optionalParamCount;

        if ("cddr".equals(name)) {
            System.out.println("buh");
        }
        if ("perimeter".equals(name)) {
            System.out.println("buh");
        }
        return actualParamCount >= lowerBound && actualParamCount <= upperBound;
    }

    public int getParameterCount() {
        ASTNode[] params = getNode().getChildren(TokenSet.create(PARAMETER_LIST));
        return params == null ? 0 :
            params[0].getChildren(TokenSet.create(PARAMETER, OPTIONAL_PARAMETER, REST_PARAMETER)).length;
    }
}
