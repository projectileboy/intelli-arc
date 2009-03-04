package com.bitbakery.plugin.arc;

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

import org.junit.Test;
import org.xml.sax.*;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileReader;
import java.io.FileInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: kurtc
 * Date: Dec 23, 2008
 * Time: 4:17:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class SaxTest {
    @Test
    public void testSaxParsing() throws Exception {
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        DefaultHandler saxHandler = new DefaultHandler() {
            @Override
            public void startDocument() throws SAXException {
                System.out.println("START DOC");
            }

            @Override
            public void endDocument() throws SAXException {
                System.out.println("END DOC");
            }

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                System.out.println("START TAG: " + qName);
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                System.out.println("END TAG: " + qName);
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                System.out.println("CHARACTERS: " + String.valueOf(ch, start, length));
            }
            
        };
        parser.parse(new FileInputStream("/Users/kurtc/dev/intelli-arc/buh.xml"), saxHandler);
    }
}
