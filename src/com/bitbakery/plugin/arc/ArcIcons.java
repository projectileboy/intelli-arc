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

import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NonNls;

import javax.swing.*;

/**
 * Contains the paths for the various Arc icons
 */
public interface ArcIcons {
    @NonNls
    final String DATA_PATH = "/icons/";

    final Icon ARC_LARGE_ICON = IconLoader.findIcon(DATA_PATH + "arc-shiny-128.png");

    final Icon ARC_MODULE_TYPE_ICON = IconLoader.findIcon(DATA_PATH + "arc-shiny-24.png");

    final Icon ARC_CONFIG_ICON = IconLoader.findIcon(DATA_PATH + "arc-shiny-32.png");

    final Icon ARC_REPL_ICON = IconLoader.findIcon(DATA_PATH + "arc-shiny-16.png");
    final Icon ARC_FILE_ICON = IconLoader.findIcon(DATA_PATH + "arc-shiny-16.png");
    
    final Icon ARC_DEF_ICON = IconLoader.findIcon(DATA_PATH + "ArcDef.png");
    final Icon ARC_MAC_ICON = IconLoader.findIcon(DATA_PATH + "ArcMac.png");
    final Icon ARC_EQ_ICON = IconLoader.findIcon(DATA_PATH + "ArcEq.png");
}
