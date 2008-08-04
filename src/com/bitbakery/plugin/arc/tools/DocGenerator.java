package com.bitbakery.plugin.arc.tools;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for taking info from Arc files and generating HTML documentation
 */
public class DocGenerator {
    private File arcFile;

    public void parse(File arcFile) {
        try {
            // TODO - Make sure the file is legit, blah blah blah
            this.arcFile = arcFile;

            ArcTokenizer in = new ArcTokenizer(new FileReader(arcFile));
            List l = parse(in);

            generateDocs(l);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List parse(ArcTokenizer in) {
        List l = new ArrayList();
        String next;
        while ((next = in.nextToken()) != null) {
            if ("(".equals(next)) {
                l.add(parse(in));
            } else if (")".equals(next)) {
                return l;
            } else {
                l.add(next.trim());
            }
        }
        return l;
    }

    private void generateDocs(List l) {
        for (Object o : l) {
            if (o instanceof List) {
                List s = (List) o;
                if (isDefOrMac(s)) {
                    generateDoc(s);
                }
            }
        }
    }

    private void generateDoc(List s) {
        try {
            String type = getType(s);
            String name = getName(s);
            List params = getParams(s);
            String doc = getDocstring(s);

            File parentDir = new File(arcFile.getParent() + File.separator + arcFile.getName().replaceFirst(".arc", ""));
            parentDir.mkdir();
            File f = new File(parentDir, name.replace('/', '_') + ".html");

            FileWriter out = new FileWriter(f);

            out.write("<html><head><title>");
            out.write("Arc Documentation - ");
            out.write(name);
            out.write("</title></head>");

            out.write("<body><h4>Type: ");
            out.write(type);
            out.write("</h4><br/>");

            out.write("<h2>File: ");
            out.write(arcFile.getName());
            out.write("</h2><br/>");

            out.write("<h2>Params: ");
            out.write(params.toString());
            out.write("</h2><br/>");

            out.write("<h2>Name: ");
            out.write(name);
            out.write("</h2><br/>");

            if (doc != null) {
                out.write("Docstring: ");
                out.write(doc);
                out.write("<br/>");
            }

            out.write("</body></html>");

            out.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private boolean isDefOrMac(List l) {
        return "def".equals(l.get(0)) || "mac".equals(l.get(0));
    }

    private String getType(List l) {
        return (String) l.get(0);
    }

    private String getName(List l) {
        return (String) l.get(1);
    }

    private List getParams(List l) {
        Object o = l.get(2);
        if (o instanceof String) {
            l = new ArrayList();
            l.add(o);
            return l;
        } else if (o instanceof List) {
            return (List) o;
        }
        return null;
    }

    private String getDocstring(List l) {
        Object o = l.get(3);
        if (o instanceof String) {
            return (String) o;
        }
        return null;
    }
}
