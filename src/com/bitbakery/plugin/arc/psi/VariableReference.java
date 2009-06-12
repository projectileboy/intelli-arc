package com.bitbakery.plugin.arc.psi;

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
import com.bitbakery.plugin.arc.ArcFileTypeLoader;
import com.bitbakery.plugin.arc.config.ArcSettings;
import com.intellij.codeInsight.TailType;
import com.intellij.codeInsight.lookup.LookupItem;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

import java.util.SortedSet;
import java.util.TreeSet;


/**
 *
 */
public class VariableReference extends ArcElement {
    private MyPsiReference reference;
    private VirtualFileSystem fs;

    public VariableReference(@NotNull final ASTNode node) {
        super(node);
        reference = new MyPsiReference(this);
        fs = VirtualFileManager.getInstance().getFileSystem("file");
    }


    // todo - we need to handle multiple def/mac definitions
    public PsiReference getReference() {
        return reference;
    }


    private class MyPsiReference extends PsiReferenceBase<VariableReference> {
        public MyPsiReference(VariableReference element) {
            super(element);
        }

        public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
            return super.handleElementRename(newElementName);    // TODO - Do something!
        }

        public PsiElement resolve() {
            return walkTree(myElement.getParent());
        }

        private PsiElement walkTree(PsiElement e) {
            if (e == null) {
                // First search for the element in other files within the project...
                VirtualFile[] roots = ProjectRootManager.getInstance(myElement.getProject()).getContentSourceRoots();
                PsiElement el = search(roots, myElement.getProject());
                if (el != null) {
                    return el;
                }

                // TODO - This is pretty expensive... we pr'y need to index these files ahead of time; re-dexing whenever we change Arc home
                // ...if not there, then search through the standard Arc library files
                VirtualFile home = fs.findFileByPath(ArcSettings.getInstance().arcHome);
                return home == null ? null : search(home.getChildren(), myElement.getProject());

            } else if (e instanceof PsiFile) {
                // TODO - Move this logic to the element class
                for (PsiElement def : e.getChildren()) {
                    if (nameMatches(def)) {
                        return def;
                    }
                }
            } else if (e instanceof Def || e instanceof Mac || e instanceof Fn) {
                if (nameMatches(e)) {
                    return e;
                }

                // TODO - Move this logic to the element class
                ParameterList params = PsiTreeUtil.getChildOfType(e, ParameterList.class);
                if (params != null) {
                    for (PsiElement param : params.getChildren()) {
                        if (nameMatches(param)) {
                            return param;
                        }
                    }
                }
            } else if (e instanceof VariableAssignment) {
                if (nameMatches(e)) {
                    return e;
                }
            } else if (e instanceof Let) {
                VariableDefinition var = PsiTreeUtil.getChildOfType(e, VariableDefinition.class);
                // TODO - Move this logic to the element class
                if (var != null) {
                    if (nameMatches(var)) {
                        return var;
                    }
                }
            } else if (e instanceof With) {
                // TODO - Move this logic to the element class
                for (PsiElement el : e.getChildren()) {
                    if (el instanceof VariableDefinition) {
                        if (nameMatches(el)) {
                            return el;
                        }
                    }
                }
            }

            return walkTree(e.getParent());
        }

        private PsiElement search(VirtualFile[] children, Project p) {
            for (VirtualFile file : children) {
                PsiElement e = file.isDirectory() ? search(file.getChildren(), p) : search(file, p);
                if (e != null) return e;
            }
            return null;
        }

        private PsiElement search(VirtualFile file, Project project) {
            if (!ArcFileType.EXTENSION.equalsIgnoreCase(file.getExtension())) {
                return null;
            }
            PsiDocumentManager.getInstance(project).commitAllDocuments();
            return search(PsiManager.getInstance(project).findFile(file).getChildren());
        }

        private PsiElement search(PsiElement[] elements) {
            for (PsiElement e : elements) {
                if (nameMatches(e)) return e;
                PsiElement el = search(e.getChildren());
                if (el != null) return el;
            }
            return null;
        }

        private boolean nameMatches(PsiElement e) {
            return e instanceof PsiNamedElement
                    && ((PsiNamedElement) e).getName() != null
                    && ((PsiNamedElement) e).getName().equals(myElement.getText());
        }

        public TextRange getRangeInElement() {
            return new TextRange(0, myElement.getTextLength());
        }

        public Object[] getVariants() {
            SortedSet<LookupItem> names = new TreeSet<LookupItem>();

            // TODO - This is a hack, a first cut at trying out code completion.
            // TODO - We need two things:
            // TODO - (1) We need to gen a stub index, so that we can get this list speedy speedy
            // TODO - (2) We need to do a short walk up the tree to add any variables in scope (e.g., function params, or let/with variables)
            VirtualFile home = fs.findFileByPath(ArcSettings.getInstance().arcHome);
            for (VirtualFile file : home.getChildren()) {
                if (ArcFileTypeLoader.ARC.equals(file.getFileType())) {
                    PsiElement[] elements = PsiManager.getInstance(myElement.getProject()).findFile(file).getChildren();
                    for (PsiElement e : elements) {
                        if (e instanceof Definition) {
                            LookupItem item = new LookupItem(e, ((PsiNamedElement) e).getName());
                            Definition d = (Definition) e;
                            item.setTailType(TailType.SPACE);
                            item.setAttribute(LookupItem.TAIL_TEXT_ATTR, " " + d.getParameterString());
                            item.setAttribute(LookupItem.TYPE_TEXT_ATTR, "[" + file.getName() + "]");
                            names.add(item);
                        } else if (e instanceof VariableAssignment) {
                            LookupItem item = new LookupItem(e, ((PsiNamedElement) e).getName());
                            item.setTailType(TailType.SPACE);
                            item.setAttribute(LookupItem.TYPE_TEXT_ATTR, "[" + file.getName() + "]");
                            names.add(item);
                        }
                    }
                }
            }

/*
            VirtualFile[] roots = ProjectRootManager.getInstance(myElement.getProject()).getContentSourceRoots();
            for (VirtualFile file : roots) {
                System.out.println(file.toString());
            }
*/

            return names.toArray();
        }

        private void prep(StringBuffer buf) {
            final int SPACE = 25;
            if (buf.length() < SPACE) {
                buf.append("                           ");
                buf.setLength(SPACE);
            } else if (buf.length() >= SPACE) {
                buf.replace(SPACE - 3, SPACE - 1, "...");
                buf.setLength(SPACE);
            }
        }
    }
}