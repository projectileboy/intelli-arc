package com.bitbakery.plugin.arc.config;

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

import com.bitbakery.plugin.arc.ArcFileTypeLoader;
import com.intellij.application.options.IndentOptionsEditor;
import com.intellij.application.options.SmartIndentOptionsEditor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.FileTypeIndentOptionsProvider;

/**
 * Created by IntelliJ IDEA.
 * User: kurtc
 * Date: Mar 1, 2009
 * Time: 10:13:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class ArcIndentOptionsProvider implements FileTypeIndentOptionsProvider {
    public CodeStyleSettings.IndentOptions createIndentOptions() {
        final CodeStyleSettings.IndentOptions indentOptions = new CodeStyleSettings.IndentOptions();
        indentOptions.INDENT_SIZE = 2;
        indentOptions.TAB_SIZE = 2;
        indentOptions.USE_TAB_CHARACTER = false;
        return indentOptions;
    }

    public FileType getFileType() {
        return ArcFileTypeLoader.ARC;
    }

    public IndentOptionsEditor createOptionsEditor() {
        return new SmartIndentOptionsEditor();
    }

    public String getPreviewText() {
        return "(def memo (f)\n" +
                "  (let cache (table)\n" +
                "    (fn args\n" +
                "      (or (cache args)\n" +
                "        (= (cache args) (apply f args))))))\n" +
                "\n" +
                "; Convenience macro for defining a function\n" +
                ";  that we want to be a memoizing function\n" +
                "(mac defmemo (name parms . body)\n" +
                "  `(safeset ,name (memo (fn ,parms ,@body))))\n";
    }

    public void prepareForReformat(final PsiFile psiFile) {
    }
}
