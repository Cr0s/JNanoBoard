/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cr0s.nanoboard.html;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author user
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
