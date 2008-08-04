package com.bitbakery.plugin.arc.tools;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

public class ArcTokenizer {
    private static final char LEFT_PAREN = '(';
    private static final char RIGHT_PAREN = ')';
    private static final char COMMA = ',';
    private static final char SPACE = ' ';
    private static final char BACKSLASH = '\\';
    private static final char QUOTE = '"';
    private static final char PIPE = '|';
    private static final char SEMICOLON = ';';
    private static final int EOL = 10;
    private static final int EOF = -1;

    private PushbackReader in;
    private StringBuffer buf;
    private boolean inComment;
    private boolean inString;
    private boolean inDoc;
    private int prevChar;

    public ArcTokenizer(Reader in) {
        this.in = new PushbackReader(in);
        buf = new StringBuffer();
        inComment = false;
        inString = false;
        inDoc = false;
        prevChar = EOF;
    }

    public String nextToken() {
        try {
            buf.setLength(0);
            int c;
            while ((c = in.read()) != EOF) {
                if (isCommentDelimiter(c)) {
                    inComment = !inComment;
                } else if (isDocDelimiter(c)) {
                    inDoc = !inDoc;
                } else if (isQuote(c)) {
                    inString = !inString;
                } else if (isObjectDelimiter(c)) {
                    if (buf.length() > 0) {
                        in.unread(c);
                        return buf.toString();
                    }
                    return String.valueOf((char) c);
                } else if (isWhitespace(c)) {
                    if (buf.length() > 0) {
                        return buf.toString();
                    }
                } else {
                    if (!inComment) {
                        buf.append((char) c);
                    }
                }
                prevChar = c;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isCommentDelimiter(int c) {
        return (c == SEMICOLON && !inString) || (inComment && c == EOL);
    }

    private boolean isDocDelimiter(int c) {
        return !inComment && ((c == PIPE && !inDoc) || (inDoc && c == EOL));
    }

    private boolean isQuote(int c) {
        return !inComment && !inDoc && ((c == QUOTE && !inString) || (inString && c == QUOTE && prevChar != BACKSLASH));
    }

    private boolean isWhitespace(int c) {
        return !inComment && !inDoc && !inString && (c == SPACE || c == EOL || c == COMMA);
    }

    private boolean isObjectDelimiter(int c) {
        return !inComment && !inDoc && !inString && (c == LEFT_PAREN || c == RIGHT_PAREN);
    }

}