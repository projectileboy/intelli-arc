package com.bitbakery.plugin.arc.actions;

import com.bitbakery.plugin.arc.ArcIcons;
import com.bitbakery.plugin.arc.ArcStrings;
import com.bitbakery.plugin.arc.ArcFileType;
import com.bitbakery.plugin.arc.templates.ArcTemplatesFactory;
import static com.bitbakery.plugin.arc.ArcStrings.message;
import com.intellij.ide.IdeView;
import com.intellij.ide.actions.CreateElementActionBase;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import com.intellij.CommonBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

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

/**
 * Created by IntelliJ IDEA.
 * User: kurtc
 * Date: Mar 2, 2009
 * Time: 2:14:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewArcFileAction extends CreateElementActionBase {
                            
    public NewArcFileAction() {
        super(message("action.ArcCreateNewFile.text"), message("action.ArcCreateNewFile.description"), ArcIcons.ARC_FILE_ICON);
    }

    @NotNull
    protected final PsiElement[] invokeDialog(final Project project, final PsiDirectory directory) {
        MyInputValidator validator = new MyInputValidator(project, directory);
        Messages.showInputDialog(project, getDialogPrompt(), getDialogTitle(), Messages.getQuestionIcon(), "", validator);

        return validator.getCreatedElements();
    }

    protected String getActionName(PsiDirectory directory, String newName) {
        return message("action.ArcCreateNewFile.text");
    }

    protected String getDialogPrompt() {
        return message("action.ArcCreateNewFile.dlg.prompt");
    }

    protected String getDialogTitle() {
        return message("action.ArcCreateNewFile.dlg.title");
    }

    protected String getCommandName() {
        return message("action.ArcCreateNewFile.text");
    }



    public void update(final AnActionEvent event) {
        super.update(event);
        final Presentation presentation = event.getPresentation();
        presentation.setEnabled(true);
        presentation.setVisible(true);
    }


    @NotNull
    protected PsiElement[] create(String newName, PsiDirectory directory) throws Exception {
        return doCreate(newName, directory);
    }

    protected static PsiFile createFileFromTemplate(final PsiDirectory directory, String name, @NonNls String templateName,
                                                    @NonNls String... parameters) throws IncorrectOperationException {
        return ArcTemplatesFactory.createFromTemplate(
                directory, name, name + "." + ArcFileType.EXTENSION, templateName, parameters);
    }


    protected String getErrorTitle() {
        return CommonBundle.getErrorTitle();
    }

    protected void checkBeforeCreate(String newName, PsiDirectory directory) throws IncorrectOperationException {
        JavaDirectoryService.getInstance().checkCreateClass(directory, newName);
    }

    @NotNull
    protected PsiElement[] doCreate(String newName, PsiDirectory directory) throws Exception {
        return new PsiElement[]{createFileFromTemplate(directory, newName, "ArcFile.arc")};
    }
}
