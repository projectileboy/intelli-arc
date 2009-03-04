package com.bitbakery.plugin.arc.templates;

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

import com.intellij.codeInsight.template.FileTypeBasedContextType;
import com.bitbakery.plugin.arc.ArcSupportLoader;
import com.bitbakery.plugin.arc.ArcFileTypeLoader;


/**
 * Defines an "Arc" context for built-in and user-defined "live templates"
 */
public class ArcTemplateContextType extends FileTypeBasedContextType {

    public ArcTemplateContextType() {
        super("ARC", "Arc", ArcFileTypeLoader.ARC);
    }
}