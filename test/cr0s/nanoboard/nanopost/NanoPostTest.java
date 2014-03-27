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
package cr0s.nanoboard.nanopost;

import cr0s.nanoboard.stegano.EncryptionProvider;
import cr0s.nanoboard.util.ByteUtils;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for NanoPost class
 * @author Cr0s
 */
public class NanoPostTest {
    
    private static NanoPost np1;
    private static NanoPost np2WithAttach;
    
    private static NanoPost opNp1;
    private static NanoPost opNp1Child;
    
    private static final String np1Text = "NanoPost #1 test. \" testing ' / \\ \\n \" escape { } ( ) [ ] characters";
    private static final String opNp1Text = "OP-post #1 test.";
    private static final String opNp1ChildText = "Child of opNp1 test NanoPost.";
    private static final String np2WithAttachText = "NanoPost #2 test with attach";
    public NanoPostTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        np1 = NanoPostFactory.createNanoPost(np1Text, EncryptionProvider.sha256(new byte[] { 1, 2, 3, 4, 5 }), null);
        
        opNp1 = NanoPostFactory.createNanoPost(opNp1Text, EncryptionProvider.EMPTY_HASH_SHA256, null);
        
        opNp1Child = NanoPostFactory.createNanoPost(opNp1ChildText, ByteUtils.stringToBytes(opNp1.getPostHash()), null);
        opNp1.addChild(opNp1Child);
        
        NanoPostAttach npa = new NanoPostAttach(new byte[] { 1, 2, 3, 4, 5, 6 }, "testfile", null);
        np2WithAttach = NanoPostFactory.createNanoPost(np2WithAttachText, EncryptionProvider.sha256(new byte[] { 1, 2, 3, 4, 5 }), null);
        np2WithAttach.setAttach(npa);
    }
    
    @AfterClass
    public static void tearDownClass() {
        np1 = null;
        
        opNp1 = null;
        opNp1Child = null;
        
        np2WithAttach = null;
    }

    /**
     * Test of getPostHash method, of class NanoPost.
     */
    @Test
    public void testGetPostHash() {

    }

    /**
     * Test of getParentHash method, of class NanoPost.
     */
    @Test
    public void testGetParentHash() {
        
    }

    /**
     * Test of getPostText method, of class NanoPost.
     */
    @Test
    public void testGetPostText() {
        assertEquals(np1Text, np1.getPostText());
        assertEquals(opNp1Text, opNp1.getPostText());
        assertEquals(opNp1ChildText, opNp1Child.getPostText());
        assertEquals(np2WithAttachText, np2WithAttach.getPostText());        
    }

    /**
     * Test of isOpPost method, of class NanoPost.
     */
    @Test
    public void testIsOpPost() {
        assertTrue(opNp1.isOpPost());
        assertFalse(opNp1Child.isOpPost());
    }

    /**
     * Test of getAttach method, of class NanoPost.
     */
    @Test
    public void testGetAttach() {
        assertNull(np1.getAttach());
        assertNotNull(np2WithAttach.getAttach());        
    }

    /**
     * Test of setAttachPost method, of class NanoPost.
     */
    @Test
    public void testSetAttachPost() {
        np2WithAttach.setAttachPost();
        
        assertTrue(np2WithAttach.getAttach().getPost() == np2WithAttach);
    }

    
// TODO: finish unit-tests
//    /**
//     * Test of getAsBytes method, of class NanoPost.
//     */
//    @Test
//    public void testGetAsBytes() {
//    }
//
//    /**
//     * Test of saveToFile method, of class NanoPost.
//     */
//    @Test
//    public void testSaveToFile() throws Exception {
//    }
//
//    /**
//     * Test of isAlreadyDownloaded method, of class NanoPost.
//     */
//    @Test
//    public void testIsAlreadyDownloaded() {
//    }
//
//    /**
//     * Test of getNanoPostFile method, of class NanoPost.
//     */
//    @Test
//    public void testGetNanoPostFile() {
//    }
//
//    /**
//     * Test of getShortHash method, of class NanoPost.
//     */
//    @Test
//    public void testGetShortHash() {
//    }
//
//    /**
//     * Test of getPostTimestamp method, of class NanoPost.
//     */
//    @Test
//    public void testGetPostTimestamp() {
//    }
//
//    /**
//     * Test of postDateToString method, of class NanoPost.
//     */
//    @Test
//    public void testPostDateToString() {
//    }
//
//    /**
//     * Test of addChild method, of class NanoPost.
//     */
//    @Test
//    public void testAddChild() {
//    }
//
    /**
     * Test of getChilds method, of class NanoPost.
     */
    @Test
    public void testGetChilds() {
        assertTrue(np1.getChilds().isEmpty());
        
        assertFalse(opNp1.getChilds().isEmpty());
        assertTrue(opNp1.getChilds().size() == 1);
    }
//
    /**
     * Test of isParentOf method, of class NanoPost.
     */
    @Test
    public void testIsParentOf() {
        assertTrue(opNp1.isParentOf(opNp1Child));
        assertFalse(opNp1.isParentOf(np1));
        assertFalse(np2WithAttach.isParentOf(np1));
    }
//
//    /**
//     * Test of toString method, of class NanoPost.
//     */
//    @Test
//    public void testToString() {
//    }
//
    /**
     * Test of getLastChildPost method, of class NanoPost.
     */
    @Test
    public void testGetLastChildPost() {
        assertNull(np1.getLastChildPost());
        
        assertSame(opNp1.getLastChildPost(), opNp1Child);
    }
//
//    /**
//     * Test of clearAllBinaryData method, of class NanoPost.
//     */
//    @Test
//    public void testClearAllBinaryData() {
//    }
//
//    /**
//     * Test of sortNanoPostsOpsByTimestamp method, of class NanoPost.
//     */
//    @Test
//    public void testSortNanoPostsOpsByTimestamp() {
//    }
//
//    /**
//     * Test of sortNanoPostsChildsByTimestamp method, of class NanoPost.
//     */
//    @Test
//    public void testSortNanoPostsChildsByTimestamp() {
//    }
//
//    /**
//     * Test of addChildsToNanoPostsInList method, of class NanoPost.
//     */
//    @Test
//    public void testAddChildsToNanoPostsInList() {
//    }*/
}
