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

import static com.bitbakery.plugin.arc.ArcStrings.message;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ArcFileType extends LanguageFileType {

    public static final String EXTENSION = "arc";
    public static final ArcLanguage ARC = new ArcLanguage();

    public ArcFileType() {
        super(ARC);
    }

    @NotNull
    public String getName() {
        return "Arc";
    }

    @NotNull
    public String getDescription() {
        return message("arc.filetype.description");
    }

    @NotNull
    public String getDefaultExtension() {
        return EXTENSION;
    }

    public Icon getIcon() {
        return ArcIcons.ARC_FILE_ICON;
    }
}

