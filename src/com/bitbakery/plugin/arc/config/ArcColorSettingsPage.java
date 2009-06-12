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

import com.bitbakery.plugin.arc.ArcFileType;
import com.bitbakery.plugin.arc.ArcIcons;
import com.bitbakery.plugin.arc.ArcSyntaxHighlighter;
import static com.bitbakery.plugin.arc.ArcSyntaxHighlighter.*;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ArcColorSettingsPage implements ColorSettingsPage {

    private static final AttributesDescriptor[] ATTRS = new AttributesDescriptor[]{
            new AttributesDescriptor(COMMENT_ID, COMMENT),
            new AttributesDescriptor(DOCSTRING_ID, DOCSTRING),
            new AttributesDescriptor(STRING_ID, STRING),
            new AttributesDescriptor(NUMBER_ID, NUMBER),
            new AttributesDescriptor(CHAR_ID, CHAR),
            new AttributesDescriptor(BOOLEAN_ID, BOOLEAN),
            new AttributesDescriptor(DEF_ID, DEF),
            new AttributesDescriptor(MAC_ID, MAC),
            new AttributesDescriptor(SPECIAL_CHARACTER_ID, SPECIAL_CHARACTER),
            new AttributesDescriptor(MACRO_TEMPLATE_ID, MACRO_TEMPLATE_CHARACTER),
            new AttributesDescriptor(SPECIAL_FORM_ID, SPECIAL_FORM),
            new AttributesDescriptor(SQUARE_BRACKET_ID, SQUARE_BRACKET),
            new AttributesDescriptor(PAREN_ID, PAREN),
    };

    @NotNull
    public String getDisplayName() {
        return "Arc";  // TODO - i18n me!
    }

    @Nullable
    public Icon getIcon() {
        return ArcIcons.ARC_FILE_ICON;
    }

    @NotNull
    public AttributesDescriptor[] getAttributeDescriptors() {
        return ATTRS;
    }

    @NotNull
    public ColorDescriptor[] getColorDescriptors() {
        return new ColorDescriptor[0];
    }

    @NotNull
    public SyntaxHighlighter getHighlighter() {
        return SyntaxHighlighterFactory.getSyntaxHighlighter(ArcFileType.ARC, null, null);
    }

    @Nullable
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        Map<String, TextAttributesKey> map = new HashMap<String, TextAttributesKey>();
        map.put("doc", ArcSyntaxHighlighter.DOCSTRING);
        return map;
    }

    @NonNls
    @NotNull
    public String getDemoText() { // TODO - i18n me!
        return ";  Sample comment\n\n" +

                "(= v \"Some global variable\")\n\n" +

                "(= one-var-function [+ 1 _])\n\n" +

                "(def function-name (param . rest)\n" +
                "   <doc>\"Documentation string\"</doc>\n" +
                "   (function-body param rest))\n\n\n" +

                "(def another-function rest\n" +
                "   (function-body rest))\n\n\n" +

                "(mac macro-name (param . rest)\n" +
                "   `(,param ,@rest))\n";
    }
}
