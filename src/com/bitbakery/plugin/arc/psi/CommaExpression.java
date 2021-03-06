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

import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;

/**
 * e.g., ',x', which turns evaluation back on for x within a backquoted expression
 */
public class CommaExpression extends Expression {
    public CommaExpression(@NotNull final ASTNode node) {
        super(node);
    }
}