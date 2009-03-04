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

import com.intellij.ide.fileTemplates.*;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.util.IncorrectOperationException;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.bitbakery.plugin.arc.ArcIcons;
import com.bitbakery.plugin.arc.ArcStrings;
import org.jetbrains.annotations.NonNls;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Enables users to create Arc files from a template, which is both
 * a time-saver and a way for beginners to learn the language.
 */
public class ArcTemplatesFactory implements FileTemplateGroupDescriptorFactory {
    @NonNls
    public static final String[] TEMPLATES = {"ArcFile.arc"};

    private static ArcTemplatesFactory instance = null;

    public static ArcTemplatesFactory getInstance() {
        if (instance == null) {
            instance = new ArcTemplatesFactory();
        }
        return instance;
    }

    private ArrayList<String> customTemplates = new ArrayList<String>();

    @NonNls
    static final String NAME_TEMPLATE_PROPERTY = "NAME";
    static final String LOW_CASE_NAME_TEMPLATE_PROPERTY = "lowCaseName";

    public FileTemplateGroupDescriptor getFileTemplatesDescriptor() {
        final FileTemplateGroupDescriptor group =
                new FileTemplateGroupDescriptor(ArcStrings.message("file.template.group.title"), ArcIcons.ARC_FILE_ICON);
        final FileTypeManager fileTypeManager = FileTypeManager.getInstance();
        for (String template : TEMPLATES) {
            group.addTemplate(new FileTemplateDescriptor(template, fileTypeManager.getFileTypeByFileName(template).getIcon()));
        }

        // register custom templates
        for (String template : getInstance().getCustomTemplates()) {
            group.addTemplate(new FileTemplateDescriptor(template, fileTypeManager.getFileTypeByFileName(template).getIcon()));
        }
        return group;
    }

    public static PsiFile createFromTemplate(final PsiDirectory directory,
                                             final String name,
                                             String fileName,
                                             String templateName,
                                             @NonNls String... parameters) throws IncorrectOperationException {

        final FileTemplate template = FileTemplateManager.getInstance().getInternalTemplate(templateName);

        Properties properties = new Properties(FileTemplateManager.getInstance().getDefaultProperties());

        properties.setProperty(NAME_TEMPLATE_PROPERTY, name);
        properties.setProperty(LOW_CASE_NAME_TEMPLATE_PROPERTY, name.substring(0, 1).toLowerCase() + name.substring(1));
        for (int i = 0; i < parameters.length; i += 2) {
            properties.setProperty(parameters[i], parameters[i + 1]);
        }
        String text;
        try {
            text = template.getText(properties);
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to load template for " +
                    FileTemplateManager.getInstance().internalTemplateToSubject(templateName),
                    e);
        }

        final PsiFile file = PsiFileFactory.getInstance(directory.getProject()).createFileFromText(fileName, text);

        return (PsiFile) directory.add(file);
    }

    public void registerCustomTemplates(String... templates) {
        for (String template : templates) {
            customTemplates.add(template);
        }
    }

    public String[] getCustomTemplates() {
        return customTemplates.toArray(new String[customTemplates.size()]);
    }
}