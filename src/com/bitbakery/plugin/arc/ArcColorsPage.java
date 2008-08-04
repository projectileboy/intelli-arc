package com.bitbakery.plugin.arc;

import static com.bitbakery.plugin.arc.ArcStrings.message;
import static com.bitbakery.plugin.arc.ArcSyntaxHighlighter.*;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class ArcColorsPage implements ColorSettingsPage {

    private static final String DEMO_TEXT =
            ";  Sample comment\n\n" +

                    "(= v \"Some global variable\")\n\n" +

                    "(= one-var-function [+ 1 _])\n\n" +

                    "(def function-name (param . rest)\n" +
                    "   \"Documentation string\"\n" +
                    "   (function-body param rest))\n\n\n" +

                    "(def another-function rest\n" +
                    "   (function-body rest))\n\n\n" +

                    "(mac macro-name (param . rest)\n" +
                    "   `(,param ,@rest))\n";

    private static final AttributesDescriptor[] ATTRS = new AttributesDescriptor[]{
            new AttributesDescriptor(message("color.settings.docstring"), DOCSTRING),
            new AttributesDescriptor(message("color.settings.comment"), LINE_COMMENT),
            new AttributesDescriptor(message("color.settings.number"), NUMERIC_LITERAL),
            new AttributesDescriptor(message("color.settings.string"), STRING_LITERAL),
            new AttributesDescriptor(message("color.settings.bad_character"), BAD_CHARACTER),
            new AttributesDescriptor(message("color.settings.def"), DEF),
            new AttributesDescriptor(message("color.settings.mac"), MAC),
            new AttributesDescriptor(message("color.settings.backquote"), BACKQUOTE),
            new AttributesDescriptor(message("color.settings.comma"), COMMA),
            new AttributesDescriptor(message("color.settings.comma_at"), COMMA_AT),
    };


    private static final ColorDescriptor[] COLORS = new ColorDescriptor[0];

    @Nullable
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @NotNull
    public String getDisplayName() {
        return message("color.settings.name");
    }

    @NotNull
    public Icon getIcon() {
        return ArcIcons.ARC_FILE_ICON;
    }

    @NotNull
    public AttributesDescriptor[] getAttributeDescriptors() {
        return ATTRS;
    }

    @NotNull
    public ColorDescriptor[] getColorDescriptors() {
        return COLORS;
    }

    @NotNull
    public SyntaxHighlighter getHighlighter() {
        return new ArcSyntaxHighlighter();
    }

    @NotNull
    public String getDemoText() {
        return DEMO_TEXT;
    }
}
