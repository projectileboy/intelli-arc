package com.bitbakery.plugin.arc;

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

