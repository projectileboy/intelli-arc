package com.bitbakery.plugin.arc.nav;

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
import com.bitbakery.plugin.arc.config.ArcSettings;
import com.bitbakery.plugin.arc.ArcFileType;
import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.roots.ProjectRootManager;
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
    private Map<String, List<Definition>> map;
    private VirtualFileSystem fs = VirtualFileManager.getInstance().getFileSystem("file");


    public String[] getNames(Project project, boolean includeNonProjectItems) {
        map = new HashMap<String, List<Definition>>();

        List<String> names = new ArrayList<String>();

        // First search for the element in other files within the project...
        for (VirtualFile contentRoot : ProjectRootManager.getInstance(project).getContentSourceRoots()) {
            findNames(project, contentRoot, names);
        }

        // TODO - Include the notion of project libraries

        // ...if specified, we also want to include defs and macs in the standard Arc library files
        if (includeNonProjectItems) {
            findNames(project, fs.findFileByPath(ArcSettings.getInstance().arcHome), names);
        }


        return names.toArray(new String[names.size()]);
    }

    public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems) {
        return getItemsByName(name, project, includeNonProjectItems);
    }

    public NavigationItem[] getItemsByName(String name, Project project, boolean includeNonProjectItems) {

        // TODO - Not sure what I do here to handle defs/macs from the standard Arc library files...???
        //    TODO - ...right now Arc library defs/macs are bing included in te symbol chooser, even when includeNonProjectItems is false. WTF???
        
        List navItems = map.get(name);
        return (NavigationItem[]) navItems.toArray(new NavigationItem[navItems.size()]);
    }


    private void findNames(Project project, VirtualFile baseDir, List<String> names) {
        if (baseDir != null) {
            for (VirtualFile file : baseDir.getChildren()) {
                if (file.isDirectory()) {
                    findNames(project, file, names);
                } else if (ArcFileType.EXTENSION.equalsIgnoreCase(file.getExtension())) {
                    findNamesInFile(project, file, names);
                }
            }
        }
    }

    private void findNamesInFile(Project project, VirtualFile file, List<String> names) {
        PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
        if (psiFile != null) {
            for (PsiElement child : psiFile.getChildren()) {
                if (child instanceof Definition) {
                    String name = ((Definition) child).getName();
                    List<Definition> navItems = map.get(name);
                    if (navItems == null) {
                        navItems = new ArrayList<Definition>();
                        map.put(name, navItems);
                    }
                    navItems.add((Definition) child);
                    names.add(name);
                }
            }
        }
    }
}
