package com.bitbakery.plugin.arc.actions;

import com.bitbakery.plugin.arc.psi.Mac;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.psi.PsiElement;

/**
 * Displays macroexpansion ("macex") for the currently selected macro definition
 */
public class MacexAction extends AnAction {

    public void actionPerformed(AnActionEvent e) {

        PsiElement mac = e.getData(DataKeys.PSI_ELEMENT);
        String s = e.getPlace();
        Presentation p = e.getPresentation();

        // TODO: We need to identify if the current location lives within a mac element
        // TODO: If we aren't within a macro definition, then we should intercept the popup and disable this action
        if (mac instanceof Mac) {
            // TODO: Display tooltip window...?
            System.out.println("mac = " + mac.getText());
        }
    }
}
