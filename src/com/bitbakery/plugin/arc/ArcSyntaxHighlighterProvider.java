package com.bitbakery.plugin.arc;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

public class ArcSyntaxHighlighterProvider implements SyntaxHighlighterProvider {
    public SyntaxHighlighter create(FileType fileType, @Nullable Project project, @Nullable VirtualFile file) {
        return new ArcSyntaxHighlighter();
    }
}
