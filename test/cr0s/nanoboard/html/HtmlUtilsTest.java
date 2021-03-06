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
package cr0s.nanoboard.html;

import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for HTML and makrdown class
 * @author Cr0s
 */
public class HtmlUtilsTest {
    
    public HtmlUtilsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of stripHtmlTags method, of class HtmlUtils.
     */
    @Test
    public void testStripHtmlTags() {
        assertEquals(HtmlUtils.stripHtmlTags("<b>test text<br>>test lines<</b>"), "&lt;b&gt;test text\n&gt;test lines&lt;&lt;/b&gt;");
    }

    /**
     * Test of markdownToHtml method, of class HtmlUtils.
     */
    @Test
    public void testMarkdownToHtml() {
        assertEquals("<em>italic</em>", HtmlUtils.markdownToHtml("*italic*"));
        
        assertEquals("<strong>bold</strong>", HtmlUtils.markdownToHtml("**bold**"));
        
        assertEquals("<em>italic</em> <strong>bold</strong>", HtmlUtils.markdownToHtml("*italic* **bold**"));
        
        assertEquals("<font color=\"green\">&gt;quote text</font>\n", HtmlUtils.markdownToHtml(HtmlUtils.stripHtmlTags(">quote text")));
        assertEquals("<font color=\"green\">&gt;quote text</font>\n\n<font color=\"green\">&gt;multiline quote text</font>\n", HtmlUtils.markdownToHtml(HtmlUtils.stripHtmlTags(">quote text\n>multiline quote text")));
    }
}
