package com.bitbakery.plugin.arc.config;

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

import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.psi.tree.IElementType;
import com.bitbakery.plugin.arc.ArcIcons;
import com.bitbakery.plugin.arc.ArcSyntaxHighlighter;
import com.bitbakery.plugin.arc.psi.ArcElementTypes;
import static com.bitbakery.plugin.arc.ArcSyntaxHighlighter.getTextAttrs;
import static com.bitbakery.plugin.arc.lexer.ArcTokenTypes.*;
import static com.bitbakery.plugin.arc.ArcStrings.message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;
import java.util.HashMap;

public class ArcColorSettingsPage implements ColorSettingsPage {

    private static final ColorDescriptor[] COLORS = new ColorDescriptor[0];

    private static final String DEMO_TEXT =
            ";  Sample comment\n\n" +

                    "(= v \"Some global variable\")\n\n" +

                    "(= one-var-function [+ 1 _])\n\n" +

                    "(def function-name (param . rest)\n" +
                    "   <doc>\"Documentation string\"</doc>\n" +
                    "   (function-body param rest))\n\n\n" +

                    "(def another-function rest\n" +
                    "   (function-body rest))\n\n\n" +

                    "(mac macro-name (param . rest)\n" +
                    "   `(,param ,@rest))\n";

    private static AttributesDescriptor createDescriptor(String msgKey, IElementType token) {
        return new AttributesDescriptor(message(msgKey), getTextAttrs(token));
    }

    private static final AttributesDescriptor[] ATTRS = new AttributesDescriptor[]{
            createDescriptor("color.settings.comment", LINE_COMMENT),
            createDescriptor("color.settings.docstring", ArcElementTypes.DOCSTRING),
            createDescriptor("color.settings.number", NUMERIC_LITERAL),
            createDescriptor("color.settings.string", STRING_LITERAL),
            createDescriptor("color.settings.bad_character", BAD_CHARACTER),
            createDescriptor("color.settings.def", DEF),
            createDescriptor("color.settings.mac", MAC),
            createDescriptor("color.settings.backquote", BACKQUOTE),
            createDescriptor("color.settings.comma", COMMA),
            createDescriptor("color.settings.comma_at", COMMA_AT),
    };

    private HashMap<String,TextAttributesKey> annotatedElementMap;
    private ArcSyntaxHighlighter highlighter = new ArcSyntaxHighlighter();

    public ArcColorSettingsPage() {
        annotatedElementMap = new HashMap<String, TextAttributesKey>();
        annotatedElementMap.put("doc", getTextAttrs(ArcElementTypes.DOCSTRING));
    }
    
    @Nullable
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return annotatedElementMap;
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
        return highlighter;
    }

    @NotNull
    public String getDemoText() {
        return DEMO_TEXT;
    }
}
