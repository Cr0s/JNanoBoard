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
package cr0s.nanoboard.stegano;

import cr0s.nanoboard.util.ByteUtils;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Encryption unit tests
 * @author Cr0s
 */
public class EncryptionProviderTest {
    
    private static final String ENCRYPTED_TEST_MESSAGE = "14355ff21d73bfdb9e06fde27598c06524f90912f2e8c10cbfdb710e7212f659e1293cde2d7003e061b463915ed6e8e310d397f94f10db1feaa5a2a8e05d4e076817f02fb4b4124f7f6f2793d2a6bb8c";
    private static final String DECRYPTED_TEST_MESSAGE = "This is a test message that will be decrypted from encrypted one.";
    
    private static final String ENCRYPTION_KEY = "sixteen bytes!!!";
    
    public EncryptionProviderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of encryptBytes method, of class EncryptionProvider.
     */
    @Test
    public void testEncryptBytes() throws Exception {
        System.out.println("= EncryptionProvider.testEncryptBytes =");
        byte[] bytes = DECRYPTED_TEST_MESSAGE.getBytes("UTF-8");
        byte[] keyValue = ENCRYPTION_KEY.getBytes("UTF-8");
        
        byte[] expResult = ByteUtils.stringToBytes(ENCRYPTED_TEST_MESSAGE);
        byte[] result = EncryptionProvider.encryptBytes(bytes, keyValue);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of decryptBytes method, of class EncryptionProvider.
     */
    @Test
    public void testDecryptBytes() throws Exception {
        System.out.println("= EncryptionProvider.testDecryptBytes =");
        byte[] bytes = ByteUtils.stringToBytes(ENCRYPTED_TEST_MESSAGE);
        
        byte[] keyValue = ENCRYPTION_KEY.getBytes("UTF-8");
        
        byte[] expResult = DECRYPTED_TEST_MESSAGE.getBytes("UTF-8");
        byte[] result = EncryptionProvider.decryptBytes(bytes, keyValue);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of sha512 method. Hash length must be equals HASH_SIZE_BYTES.
     */
    @Test
    public void testSha512_hashlength() throws UnsupportedEncodingException {
        System.out.println("= EncryptionProvider.sha512 hash length test =");
        byte[] bytes = "fooBarBAZ".getBytes("UTF-8");
        
        int expResult = EncryptionProvider.HASH_SIZE_BYTES;
        
        byte[] hashBytes = EncryptionProvider.sha512(bytes);
        assertEquals(expResult, hashBytes.length);
    }
    
    /**
     * Test of sha512 method, of class EncryptionProvider.
     * Tests equality condition. Two hashesh of same argument must be equals
     */
    @Test
    public void testSha512_equality() throws UnsupportedEncodingException {
        System.out.println("= EncryptionProvider.sha512 `H(x) = H(x); x = x` test =");
        byte[] bytes = "fooBarBAZ".getBytes("UTF-8");
        
        byte[] hashBytes1 = EncryptionProvider.sha512(bytes);
        byte[] hashBytes2 = EncryptionProvider.sha512(bytes);
        assertArrayEquals(hashBytes1, hashBytes2);
    }    
    
    /**
     * Test of sha512 method, of class EncryptionProvider.
     * Two hashes of different argument must be inequal
     */
    @Test
    public void testSha512_inequality() throws UnsupportedEncodingException {
        System.out.println("= EncryptionProvider.sha512 `H(x) != H(y); x != y` test =");
        byte[] bytes1 = "fooBarBAZ".getBytes("UTF-8");
        byte[] bytes2 = "fooBarBAY".getBytes("UTF-8");
        
        byte[] hashBytes1 = EncryptionProvider.sha512(bytes1);
        byte[] hashBytes2 = EncryptionProvider.sha512(bytes2);
        
        // must be inequal
        assertFalse(Arrays.equals(hashBytes1, hashBytes2));
    }     
}
