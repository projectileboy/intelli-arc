package com.bitbakery.plugin.arc.psi;

import com.bitbakery.plugin.arc.ArcFileType;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;


/**
 *
 */
public class VariableReference extends ArcElement {
    private MyPsiReference reference;

    public VariableReference(@NotNull final ASTNode node) {
        super(node);
        reference = new MyPsiReference(this);
    }

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
                // TODO - This works!! Now I just need to incorporate standard Arc source files!

                VirtualFile[] roots = ProjectRootManager.getInstance(myElement.getProject()).getContentRoots();
                return search(roots, myElement.getProject());
            } else if (e instanceof PsiFile) {
                for (PsiElement def : e.getChildren()) {
                    if (nameMatches(def)) {
                        return def;
                    }
                }
            } else if (e instanceof Def || e instanceof Mac || e instanceof Fn) {
                if (nameMatches(e)) {
                    return e;
                }

                ParameterList params = PsiTreeUtil.getChildOfType(e, ParameterList.class);
                if (params != null) {
                    for (PsiElement param : params.getChildren()) {
                        if (nameMatches(param)) {
                            return param;
                        }
                    }
                }
            } else if (e instanceof Let || e instanceof With) {

                // TODO - Check the variables defined by the Let/With
                ParameterList params = PsiTreeUtil.getChildOfType(e, ParameterList.class);
                if (params != null) {
                    for (PsiElement param : params.getChildren()) {
                        if (nameMatches(param)) {
                            return param;
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
            // TODO - Implement me to get code completion working - need to search up the tree and gather every variable def (including def/mac), plus all top-level declarations, including other files
            //return new Object[0];
            return new String[]{"var-one", "var-two", "var-three"};
        }
    }
}