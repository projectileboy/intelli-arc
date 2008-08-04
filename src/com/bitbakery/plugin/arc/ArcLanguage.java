package com.bitbakery.plugin.arc;


import com.bitbakery.plugin.arc.lexer.ArcTokenTypes;
import com.bitbakery.plugin.arc.structure.ArcStructureViewModel;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder;
import com.intellij.lang.*;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.ExternalAnnotator;
import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.parameterInfo.ParameterInfoHandler;
import com.intellij.lang.refactoring.NamesValidator;
import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.lang.surroundWith.SurroundDescriptor;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class ArcLanguage extends Language {
    private static final Annotator ARC_ANNOTATOR = new ArcAnnotator();

    public ArcLanguage() {
        super("Arc");
    }

    @NotNull
    public ParserDefinition getParserDefinition() {
        return new ArcParserDefinition();
    }

    @Nullable
    protected DocumentationProvider createDocumentationProvider() {
        return new ArcDocumentationProvider();
    }

    @NotNull
    public TokenSet getReadableTextContainerElements() {
        return ArcTokenTypes.READABLE_TEXT;
    }

    @NotNull
    public PairedBraceMatcher getPairedBraceMatcher() {
        return new ArcBraceMatcher();
    }

    @NotNull
    public SyntaxHighlighter getSyntaxHighlighter(Project project, final VirtualFile virtualFile) {
        return new ArcSyntaxHighlighter();
    }

    @NotNull
    public Commenter getCommenter() {
        return new ArcCommenter();
    }

    public FoldingBuilder getFoldingBuilder() {
        return new ArcFoldingBuilder();
    }

    @Nullable
    public StructureViewBuilder getStructureViewBuilder(final PsiFile psiFile) {
        return new TreeBasedStructureViewBuilder() {
            @NotNull
            public StructureViewModel createStructureViewModel() {
                return new ArcStructureViewModel(psiFile);
            }

            public boolean isRootNodeShown() {
                return false;
            }
        };
    }

/*
    @Nullable
    public FormattingModelBuilder getFormattingModelBuilder() {
        return new ArcFormattingModelBuilder();
    }
*/

    public Annotator getAnnotator() {
        return ARC_ANNOTATOR;
    }

    @NotNull
    public FindUsagesProvider getFindUsagesProvider() {
        return new ArcFindUsagesProvider();
    }

}

