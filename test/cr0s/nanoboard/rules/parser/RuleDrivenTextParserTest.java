/*
 The MIT License (MIT)

 Copyright (c) 2014 Cr0s

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */
package cr0s.nanoboard.rules.parser;

import cr0s.nanoboard.rules.Rule;
import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for text parser by rules
 * @author user
 */
public class RuleDrivenTextParserTest {
    
    private static Rule testRule1;
    private final static String RULE1_TEST_REGEXP = "src=\"([a-z\\/]+[0-9]+\\.png)\"";
    
    private final static String RULE1_TEST_TEXT = "<html><head></head><body>\n"
            + "<img src=\"/img/656346565.png\"><br>\n"
            + "<img src=\"/icons/icon.ico\"><br>\n"
            + "<img src=\"/img/photo.png\"><br>\n"
            + "<img src=\"/img/0101010101.png\"><br>\n"
            + "<img src=\"http://google.com/favicon.ico\"><br>\n"
            + "</body></html>";
    
    private final static String[] RULE1_TEST_MATCHES = {
        "/img/656346565.png",
        "/img/0101010101.png",
    };
    
    private static RuleDrivenTextParser rdtpTestRule1;
    
    public RuleDrivenTextParserTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        testRule1 = new Rule("test rule", "http://localhost/", RULE1_TEST_REGEXP, true);
        rdtpTestRule1 = new RuleDrivenTextParser(testRule1);
    }
    
    @AfterClass
    public static void tearDownClass() {
        testRule1 = null;
    }

    /**
     * Test of parseTextByRule method, of class RuleDrivenTextParser.
     */
    @Test
    public void testParseTextByRule_rule1_match() {
        ArrayList<String> matches = rdtpTestRule1.parseTextByRule(RULE1_TEST_TEXT);
        String[] matchesArray = new String[matches.size()];
        matchesArray = matches.toArray(matchesArray);

        assertArrayEquals(RULE1_TEST_MATCHES, matchesArray);
    }


}
