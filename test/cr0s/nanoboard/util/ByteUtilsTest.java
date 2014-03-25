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
package cr0s.nanoboard.util;

import java.io.File;
import java.util.Random;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A unit-test for ByteUtils class
 * @author user
 */
public class ByteUtilsTest {  
    private static byte[] testBytes = new byte[1024];
    
    public ByteUtilsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        // Generate a random array of bytes
        Random r = new Random(System.currentTimeMillis());
        
        r.nextBytes(testBytes);
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of readBytesFromFile method, of class ByteUtils.
     */
    @Test
    public void testWriteReadBytesToFile() throws Exception {
        File testFile = new File("byteutils_test.bin");
        testFile.deleteOnExit();
        
        ByteUtils.writeBytesToFile(testFile, testBytes);
        byte[] bytes = ByteUtils.readBytesFromFile(testFile);
        
        assertArrayEquals(testBytes, bytes);
    }

    /**
     * Test of bytesToHexString method, of class ByteUtils.
     */
    @Test
    public void testBytesToHexString() {
        String testHex = ByteUtils.bytesToHexString(testBytes); 
        assertEquals(testHex.length(), testBytes.length * 2);
    }

    /**
     * Test of stringToBytes method, of class ByteUtils.
     */
    @Test
    public void testHexAndBytesTransformation() {
        byte[] bytes = ByteUtils.stringToBytes(ByteUtils.bytesToHexString(testBytes));
        assertArrayEquals(testBytes, bytes);        
    }
}
