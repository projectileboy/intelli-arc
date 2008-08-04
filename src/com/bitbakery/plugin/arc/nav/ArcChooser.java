package com.bitbakery.plugin.arc.nav;

import com.bitbakery.plugin.arc.psi.VariableAssignment;
import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Enables quick navigation to variable definitions in project and library files
 */
public class ArcChooser implements ChooseByNameContributor {
    private Map<String, List<VariableAssignment>> map;

    public String[] getNames(Project project, boolean includeNonProjectItems) {
        map = new HashMap<String, List<VariableAssignment>>();

        List<String> names = new ArrayList<String>();

        // TODO - Distinguish between project and non-project Arc files
        findNames(project, project.getBaseDir(), names);

        return names.toArray(new String[names.size()]);
    }

    private void findNames(Project project, VirtualFile baseDir, List<String> names) {
        if (baseDir != null) {
            for (VirtualFile file : baseDir.getChildren()) {
                if (file.isDirectory()) {
                    findNames(project, file, names);
                } else if ("arc".equalsIgnoreCase(file.getExtension())) {
                    findNamesInFile(project, file, names);
                }
            }
        }
    }

    private void findNamesInFile(Project project, VirtualFile file, List<String> names) {
        PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
        if (psiFile != null) {
            for (PsiElement child : psiFile.getChildren()) {
                if (child instanceof VariableAssignment) {
                    String name = ((VariableAssignment) child).getName();
                    List<VariableAssignment> navItems = map.get(name);
                    if (navItems == null) {
                        navItems = new ArrayList<VariableAssignment>();
                        map.put(name, navItems);
                    }
                    navItems.add((VariableAssignment) child);
                    names.add(name);
                }
            }
        }
    }

    public NavigationItem[] getItemsByName(String name, Project project, boolean includeNonProjectItems) {
        // TODO - Distinguish between project and non-project Arc files
        List navItems = map.get(name);
        return (NavigationItem[]) navItems.toArray(new NavigationItem[navItems.size()]);
    }
}
