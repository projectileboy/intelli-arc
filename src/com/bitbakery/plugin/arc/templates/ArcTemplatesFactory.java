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

import com.bitbakery.plugin.arc.ArcFileTypeLoader;
import static com.bitbakery.plugin.arc.ArcIcons.ARC_FILE_ICON;
import static com.bitbakery.plugin.arc.ArcStrings.message;
import com.intellij.ide.fileTemplates.*;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

/**
 * Enables users to create Arc files from a template, which is both
 * a time-saver and a way for beginners to learn the language.
 */
public class ArcTemplatesFactory implements FileTemplateGroupDescriptorFactory {
    public static final ArcTemplatesFactory instance = new ArcTemplatesFactory();

    private static final String[] TEMPLATES = {"ArcFile.arc"};
    private static final String NAME_TEMPLATE_PROPERTY = "NAME";

    private ArrayList<String> customTemplates = new ArrayList<String>();


    public FileTemplateGroupDescriptor getFileTemplatesDescriptor() {
        FileTemplateGroupDescriptor group = new FileTemplateGroupDescriptor(message("file.template.group.title"), ARC_FILE_ICON);

        addTemplates(group, TEMPLATES);
        addTemplates(group, getCustomTemplates());

        return group;
    }

    public static PsiFile createFromTemplate(final PsiDirectory directory,
                                             final String name,
                                             String fileName,
                                             String templateName,
                                             @NonNls String... parameters) throws IncorrectOperationException {
        FileTemplateManager tMgr = FileTemplateManager.getInstance();
        try {
            FileTemplate template = tMgr.getCodeTemplate(templateName);
            String text = template.getText(defineTemplateProperties(name, parameters));
            PsiFile file = PsiFileFactory.getInstance(directory.getProject()).createFileFromText(fileName, text);
            return (PsiFile) directory.add(file);
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to load template for " + tMgr.internalTemplateToSubject(templateName), e);
        }
    }

    public void registerCustomTemplates(String... templates) {
        customTemplates.addAll(Arrays.asList(templates));
    }

    public String[] getCustomTemplates() {
        return customTemplates.toArray(new String[customTemplates.size()]);
    }


    private void addTemplates(FileTemplateGroupDescriptor group, String[] templates) {
        for (String template : templates) {
            group.addTemplate(new FileTemplateDescriptor(template, ArcFileTypeLoader.ARC.getIcon()));
        }
    }

    private static Properties defineTemplateProperties(String name, String... parameters) {
        Properties properties = new Properties(FileTemplateManager.getInstance().getDefaultProperties());
        properties.setProperty(NAME_TEMPLATE_PROPERTY, name);
        for (int i = 0; i < parameters.length; i += 2) {
            properties.setProperty(parameters[i], parameters[i + 1]);
        }
        return properties;
    }
}