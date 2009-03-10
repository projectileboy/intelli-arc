package com.bitbakery.plugin.arc;

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

import com.bitbakery.plugin.arc.psi.Definition;
import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

public class ArcDocumentationProvider implements DocumentationProvider {

    // TODO - I needs to be configurablzzz
    private static final String DOC_ROOT_URL = "http://practical-scheme.net/wiliki/arcxref?";

    @Nullable
    public String getQuickNavigateInfo(PsiElement element) {
        if (element instanceof Definition) {
            Definition d = (Definition) element;
            return d.getQuickDoc();
        }
        return null;
    }

    @Nullable
    public String getUrlFor(PsiElement element, PsiElement originalElement) {
        // TODO - We need to be able to specify which URL we should go to, based on the file we're in
        if (element instanceof Definition) {
            try {
                return DOC_ROOT_URL + URLEncoder.encode(((Definition) element).getName(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace(); // Pretty sure UTF-8 is supported... stupid Java...
            }
        }
        return null;
    }

    @Nullable
    public String generateDoc(PsiElement element, PsiElement originalElement) {

        StringBuffer buf = new StringBuffer();
        BufferedReader in = null;
        try {
            // Screen-scrape the Arc xref wiki...  this is all (obviously) super brittle
            URL url = new URL(getUrlFor(element, originalElement));
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            boolean isInCode = false;
            while ((str = in.readLine()) != null) {
                if (str.contains("<pre")) {
                    isInCode = true;
                } else if (str.contains("</pre")) {
                    isInCode = false;
                }
                buf.append(str);
                if (isInCode) buf.append("<br>");
            }
            String s = buf.toString();
            return s.substring(s.indexOf("<h1>"), s.indexOf("</td><td class=\"menu-strip\""));
        } catch (Exception e) {
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
