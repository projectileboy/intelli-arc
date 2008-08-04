package com.bitbakery.plugin.arc;

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
    final Icon ARC_DEF_ICON = IconLoader.findIcon(DATA_PATH + "arc-def.png");
    final Icon ARC_MAC_ICON = IconLoader.findIcon(DATA_PATH + "arc-mac.png");
}
