package com.bitbakery.plugin.arc.psi;

import com.bitbakery.plugin.arc.ArcFileType;
import com.bitbakery.plugin.arc.ArcSupportLoader;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

/**
 * PSI element for an Arc file
 */
public class ArcFile extends PsiFileBase {
    public ArcFile(FileViewProvider viewProvider) {
        super(viewProvider, ArcFileType.ARC);
    }

    @NotNull
    public FileType getFileType() {
        return ArcSupportLoader.ARC;
    }
}
