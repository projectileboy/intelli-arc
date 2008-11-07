package com.bitbakery.plugin.arc;


import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;

public class ArcSupportLoader extends FileTypeFactory implements ApplicationComponent { //, InspectionToolProvider, FileTypeIndentOptionsProvider, IconProvider {

    public static final LanguageFileType ARC = new ArcFileType();


    public void initComponent() {
    }

    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return "Arc support loader";
    }

/*
TODO - See Javascript plugin for good examples of inspection goodies
    public Class[] getInspectionClasses() {
        return new Class[]{
        };
    }
*/

    //public CodeStyleSettings.IndentOptions createIndentOptions() {
    //    return new CodeStyleSettings.IndentOptions();
    //}

    public FileType getFileType() {

        return ARC;
    }

//    public void createFileTypes(final @NotNull PairConsumer<FileType, String> consumer) {
//        consumer.consume(ARC, "arc");
//    }

    //public Icon getIcon(@NotNull final PsiElement element, final int flags) {
    //    return null;
    //}

    public void createFileTypes(@NotNull FileTypeConsumer consumer) {
        // TODO - Is this correct?? The Javadoc is non-existent!!
        consumer.consume(ARC, "arc");
    }
}
