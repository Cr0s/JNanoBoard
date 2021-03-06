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
package cr0s.nanoboard.image;

import cr0s.nanoboard.stegano.CompressionProvider;
import cr0s.nanoboard.stegano.EncryptionProvider;
import cr0s.nanoboard.util.ByteUtils;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;

/**
 * Image encoding and stegano routines
 *
 * @author Cr0s
 */
public class ImageUtils {

    public static byte[] tryToDecodeSteganoImage(byte[] bytes, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        try {
            // 1. Try to load as image
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));

            // 2. Try to read stegano data
            short[] s = ImageEncoder.readBytesFromImage(img, key);

            if (s == null) {
                return null;
            }
            
            // Data inside image is too small, reject image
            if (s.length < EncryptionProvider.SHA_256_HASH_SIZE_BYTES * 2) {
                return null;
            }

            byte[] b = new byte[s.length];
            for (int i = 0; i < b.length; i++) {
                b[i] = (byte) (s[i] & 0xFF);
            }

            // 3. Try to decrypt bytes
            byte[] decryptedBytes = null;
            decryptedBytes = EncryptionProvider.decryptBytes(b, key.getBytes());

            // 4. Try to decompress bytes
            decryptedBytes = CompressionProvider.decompressBytes(decryptedBytes);
            
            return decryptedBytes;
        } catch (DataFormatException | IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static byte[] tryToDecodeSteganoImage(ByteArrayOutputStream baos, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        return tryToDecodeSteganoImage(baos.toByteArray(), key);
    }

    public static void encodeIntoImage(File containerImg, File outputFile, byte[] srcBytes, String key) {
        try {
            BufferedImage in = ImageIO.read(containerImg);
            BufferedImage newImage = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = newImage.createGraphics();
            g.drawImage(in, 0, 0, null);
            g.dispose();

            srcBytes = CompressionProvider.compressBytes(srcBytes);

            byte[] encryptedBytes = EncryptionProvider.encryptBytes(srcBytes, key.getBytes());

            // Trick: convert from unsigned number to signed byte
            short[] srcS = new short[encryptedBytes.length];
            for (int i = 0; i < encryptedBytes.length; i++) {
                srcS[i] = (short) (encryptedBytes[i] & 0xFF);
            }

            ImageEncoder.writeBytesToImage(in, encryptedBytes, outputFile.toString(), key);

            System.out.println("[OK] Steganographic .png \"" + outputFile.toString() + "\" generated.");
        } catch (DataFormatException ex) {
            Logger.getLogger(ImageUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | ImageWriteException | IOException ex) {
            Logger.getLogger(ImageUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static int calculatePossibleDataSpace(File containerImg, String key) throws IOException {
        BufferedImage in = ImageIO.read(containerImg);
        BufferedImage newImage = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(in, 0, 0, null);
        g.dispose();
            
        return calculatePossibleDataSpace(in, key);
    }
    
    public static int calculatePossibleDataSpace(BufferedImage bi, String key) {
        int h = bi.getHeight();
        int w = bi.getWidth();

        // In each pixel we have 3 bytes (R, G and B) in which we can store one bit of useful data at LSB
        // So we can store w * h * 3 / 8 bytes.
        return (w * h * 3) / 8;      
    }
}
