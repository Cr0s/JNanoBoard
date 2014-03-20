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

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Cryptography utilities:
 *
 * Encrypt and Decrypt routines Hashsum generation
 *
 * @author Cr0s
 */
public class EncryptionProvider {

    public static final String ALGO = "AES";
    public static final String MODE = "CBC";
    public static final String PADDING = "PKCS5Padding";
    public static final int KEY_LENGTH = 16;
    
    public static final byte[] EMPTY_HASH = new byte[] {
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0
    };
    
    /**
     * Encrypt bytes with AES by specified key
     * @param bytes bytes to crypt
     * @param keyValue encryption key
     * @return 
     */
    public static byte[] encryptBytes(byte[] bytes, byte[] keyValue) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] keyValuePad = new byte[KEY_LENGTH];
        System.arraycopy(keyValue, 0, keyValuePad, 0, keyValue.length);

        // Init random IV based on key hash
        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        Random rng = new Random(new String(keyValue).hashCode());
        rng.nextBytes(iv);
        IvParameterSpec ivspec = new IvParameterSpec(iv);            

        Key key;
        key = new SecretKeySpec(keyValuePad, ALGO);
        Cipher c = Cipher.getInstance(ALGO + "/" + MODE + "/" + PADDING);
        c.init(Cipher.ENCRYPT_MODE, key, ivspec);

        byte[] encVal = c.doFinal(bytes);
        return encVal;
    }

    /**
     * Decrypt bytes with AES by specified key
     * @param bytes bytes to decrypt
     * @param keyValue encryption key
     * @return 
     */
    public static byte[] decryptBytes(byte[] bytes, byte[] keyValue) throws NoSuchAlgorithmException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        // Init random IV based on key hash
        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        Random rng = new Random(new String(keyValue).hashCode());
        rng.nextBytes(iv);
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        byte[] keyValuePad = new byte[KEY_LENGTH];
        System.arraycopy(keyValue, 0, keyValuePad, 0, keyValue.length);

        Key key;
        key = new SecretKeySpec(keyValuePad, ALGO);          
        Cipher c = Cipher.getInstance(ALGO + "/" + MODE + "/" + PADDING);
        c.init(Cipher.DECRYPT_MODE, key, ivspec);

        byte[] encVal = c.doFinal(bytes);
        return encVal;
    }
    
    public static int HASH_SIZE_BYTES = 64;
    /**
     * Returns sha512 hash of bytes array
     * @param bytes input bytes
     * @return hash bytes
     */
    public static byte[] sha512(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(bytes);

            byte byteData[] = md.digest();

            return byteData;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EncryptionProvider.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        
        return null;
    }
}
