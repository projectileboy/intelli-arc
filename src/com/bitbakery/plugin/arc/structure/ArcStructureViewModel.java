package com.bitbakery.plugin.arc.structure;

import com.bitbakery.plugin.arc.psi.Def;
import com.bitbakery.plugin.arc.psi.Fn;
import com.bitbakery.plugin.arc.psi.Mac;
import com.bitbakery.plugin.arc.psi.SingleArgFn;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.TextEditorBasedStructureViewModel;
import com.intellij.ide.util.treeView.smartTree.Filter;
import com.intellij.ide.util.treeView.smartTree.Grouper;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

/**
 * Enables "structure view" for Arc source files.
 */
public class ArcStructureViewModel extends TextEditorBasedStructureViewModel {
    private PsiFile myFile;

    public ArcStructureViewModel(final PsiFile file) {
        super(file);
        myFile = file;
    }

    @NotNull
    public StructureViewTreeElement getRoot() {
        return new ArcStructureViewElement(myFile);
    }

    @NotNull
    public Grouper[] getGroupers() {
        // TODO - Enable grouping based on defs, macs, fns, []s, etc...
        return Grouper.EMPTY_ARRAY;
    }

    @NotNull
    public Sorter[] getSorters() {
        // TODO - Enable sorting based on defs, macs, fns, []s, etc...
        return new Sorter[]{Sorter.ALPHA_SORTER};
    }

    @NotNull
    public Filter[] getFilters() {
        // TODO - Enable filtering based on defs, macs, fns, []s, etc...
        return Filter.EMPTY_ARRAY;
    }

    protected PsiFile getPsiFile() {
        return myFile;
    }

    @NotNull
    protected Class[] getSuitableClasses() {
        return new Class[]{Def.class, Mac.class, SingleArgFn.class, Fn.class};
    }
}
