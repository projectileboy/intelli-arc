package com.bitbakery.plugin.arc.config;

import com.intellij.openapi.options.Configurable;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider;
import org.jetbrains.annotations.NotNull;

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

/**
 * Created by IntelliJ IDEA.
 * User: kurtc
 * Date: Jun 12, 2009
 * Time: 9:45:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class ArcCodeStyleSettingsProvider extends CodeStyleSettingsProvider {
    @NotNull
    public Configurable createSettingsPage(CodeStyleSettings settings, CodeStyleSettings originalSettings) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
