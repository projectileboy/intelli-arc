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

import static com.bitbakery.plugin.arc.psi.ArcElementTypes.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;

/**
 * Container for stuff shared by Def and Mac
 */
public abstract class Definition extends VariableAssignment {

    private final String quickDocType;

    public Definition(ASTNode node, String quickDocType) {
        super(node);
        this.quickDocType = quickDocType;
    }

    public String getDocstring() {
        ASTNode[] children = getNode().getChildren(TokenSet.create(ArcElementTypes.DOCSTRING));
        if (children != null && children.length > 0) {
            return stripQuotes(children[0].getText());
        }
        return null;
    }

    public String getQuickDoc() {
        StringBuffer buf = new StringBuffer();
        buf.append(quickDocType).append(" ").append(getName());

        String docstring = getDocstring();
        buf.append(docstring == null ? "" : "\r\n" + docstring);

        return buf.toString();
    }

    public int getParameterCount() {
        ASTNode[] params = getParams();
        return params == null ? 0 :
                params[0].getChildren(TokenSet.create(PARAMETER, OPTIONAL_PARAMETER, REST_PARAMETER)).length;
    }

    public int getMinParameterCount() {
        ASTNode[] params = getParams();
        return params.length > 0 ? params[0].getChildren(PARAM_FILTER).length : 0;
    }

    public int getMaxParameterCount() {
        ASTNode[] params = getParams();

        if (params.length > 0) {
            int optionalParamCount = params[0].getChildren(OPTIONAL_PARAM_FILTER).length;
            int restParamCount = params[0].getChildren(REST_PARAM_FILTER).length;

            return restParamCount > 0 ? Integer.MAX_VALUE : (getMinParameterCount() + optionalParamCount);
        }
        return 0;
    }

    public String getParameterString() {
        ASTNode[] params = getParams();
        if (params.length > 0) {
            return params[0].getText();
        }
        return "";
    }

    public boolean isValidParameterCount(int actualParamCount) {
        return actualParamCount >= getMinParameterCount()
                && actualParamCount <= getMaxParameterCount();
    }

    private ASTNode[] getParams() {
        return getNode().getChildren(PARAM_LIST_FILTER);
    }

    private String stripQuotes(String s) {
        s = s.trim();
        if (s.length() > 2) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }
}
