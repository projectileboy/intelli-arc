package com.bitbakery.plugin.arc.psi;

import com.bitbakery.plugin.arc.ArcFileType;
import com.bitbakery.plugin.arc.lexer.ArcLexer;
import com.intellij.lang.ASTNode;
import com.intellij.lang.impl.PsiBuilderImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests that ArcParser is building PSI element trees correctly.
 */
public class ArcParserTest {

    @Test
    public void testEmptyExpression() {
        ASTNode root = parse("()");
        Assert.assertEquals(ArcElementTypes.FILE, root.getElementType());
    }

    private ASTNode parse(String testExpression) {
        ArcParser parser = new ArcParser();
        return parser.parse(ArcElementTypes.FILE,
                new PsiBuilderImpl(ArcFileType.ARC, new ArcLexer(), null, null, testExpression));
    }
}


