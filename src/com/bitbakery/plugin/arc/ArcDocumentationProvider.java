package com.bitbakery.plugin.arc;

import com.bitbakery.plugin.arc.psi.Def;
import com.bitbakery.plugin.arc.psi.Mac;
import com.bitbakery.plugin.arc.psi.VariableDefinition;
import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class ArcDocumentationProvider implements DocumentationProvider {
    // TODO - I needs to be configurablzzz
    private static final String DOC_ROOT_URL = "http://www.bitbakery.com/arc-";

    @Nullable
    public String getQuickNavigateInfo(PsiElement element) {
        if (element instanceof Def) {
            return ((Def) element).getDocstring();
        } else if (element instanceof Mac) {
            return ((Mac) element).getDocstring();
        }
        return null;
    }

    @Nullable
    public String getUrlFor(PsiElement element, PsiElement originalElement) {
        // TODO - We need to be able to specify which URL we should go to, based on the file we're in
        if (element instanceof VariableDefinition) {
            return DOC_ROOT_URL + ((VariableDefinition) element).getName();
        }
        return null;
    }

    @Nullable
    public String generateDoc(PsiElement element, PsiElement originalElement) {
        // TODO - Make me real, somehow...? Pull from arcfn, maybe...? Plus parameter info...?
        StringBuffer buf = new StringBuffer();
        BufferedReader in = null;
        try {
            URL url = new URL(DOC_ROOT_URL + ((VariableDefinition) element).getName());
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = in.readLine()) != null) {
                buf.append(str);
            }
            in.close();
            return buf.toString();
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public PsiElement getDocumentationElementForLookupItem(PsiManager psiManager, Object object, PsiElement element) {
        return null; // TODO - What's this guy for??
    }

    @Nullable
    public PsiElement getDocumentationElementForLink(PsiManager psiManager, String link, PsiElement context) {
        return null; // TODO - What's this guy for??
    }
}
