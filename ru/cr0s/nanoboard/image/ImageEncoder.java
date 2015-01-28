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

import cr0s.nanoboard.stegano.EncryptionProvider;
import cr0s.nanoboard.stegano.RC4;
import cr0s.nanoboard.util.BitInputStream;
import cr0s.nanoboard.util.BitOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.imageio.ImageIO;

/**
 * Steganography encoder by F5 LSB method
 *
 * @author Cr0s
 */
public class ImageEncoder {

    /**
     * Write some custom data shorts (as short 0-255) into image and saves it
     * into file
     *
     * Bytes sequence followed little endian
     *
     * @param bimg opened source buffered image
     * @param msg shorts to store
     * @param filename
     */
    public static void writeBytesToImage(BufferedImage bimg, byte[] msg, String filename, String key) throws ImageWriteException, IOException {

        int w = bimg.getWidth();
        int h = bimg.getHeight();

        //System.out.println("[Key] " + key);

        RC4 prng = new RC4(EncryptionProvider.sha256(key.getBytes()));
        Set<Integer> usedBytes = new HashSet<>();

        // Encode message length
        int length = msg.length;
        //System.out.println("[Actual length] " + length);
        byte[] lengthBytes = new byte[4];
        lengthBytes[0] = (byte) ((length >> 24) & 0xFF);
        lengthBytes[1] = (byte) ((length >> 16) & 0xFF);
        lengthBytes[2] = (byte) ((length >> 8) & 0xFF);
        lengthBytes[3] = (byte) (length & 0xFF);

        byte[] data = new byte[1 + 4 + length];

        // Add a header byte to the nanopost to be able to check validity
        byte[] header = new byte[1];
        header[0] = prng.getByte();
        
        //System.out.println("[Validation byte]: " + header[0]);
        
        // Write header byte
        System.arraycopy(header, 0, data, 0, 1);
        
        // Write length to data array
        System.arraycopy(lengthBytes, 0, data, 1, 4);

        // Write data to data array
        System.arraycopy(msg, 0, data, 5, msg.length);

        //System.out.println("[Data array] " + Arrays.toString(data));

        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        BitInputStream bis = new BitInputStream(bais);

        // Write data bits as LSB for pixel bytes
        int bitNumber = 0;
        //System.out.println("[Index] ");
        byte bit;
        
        while ((bit = (byte) bis.readBits(1)) != -1 && bitNumber < data.length * 8) {
            // Get image pixel
            int index = nextPixelIndex(prng, usedBytes, h, w);

            int x = getPixelX(index, h, w);
            int y = getPixelY(index, h, w);
            int byteNumber = getPixelZ(index, h, w);

            ////System.out.print(index + " x: " + x + " y: " + y + " b: " + byteNumber + "  ");            
            
            int pixel = 0;
            try {
                pixel = bimg.getRGB(x, y);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("x: " + x + " y: " + y + " z: " + byteNumber);
            }

            // Write LSB to pixel
            if (bit != 0) {
                pixel |= 1 << byteNumber * 8;
            } else {
                pixel &= ~(1 << byteNumber * 8);
            }
            
            
            //System.out.println("lsb: " + bit + " = " + ((pixel >> (8 * byteNumber)) & 0x01));            
            
            bimg.setRGB(x, y, pixel);

            bitNumber++;
        }

        try {
            File outputfile = new File(filename);
            ImageIO.write(bimg, "png", outputfile);
        } catch (IOException e) {
            throw new ImageWriteException("Error writing to file: " + e.getMessage());
        }
    }

    private static int getPixelX(int index, int h, int w) {
        return index / (h * 3) % w;
    }

    private static int getPixelY(int index, int h, int w) {
        return index / 3 % h;
    }
    
    private static int getPixelZ(int index, int h, int w) {
        return index % 3;
    }    
    
    public static int nextPixelIndex(RC4 prng, Set<Integer> usedBytes, int h, int w) {
        int index = 0;
        int max = h * w * 3;
        do {
            index = (int) prng.getUnsignedInt() % max;
        } while (!usedBytes.add(index));

        return index;
    }

    public static int readMessageLength(RC4 prng, Set<Integer> usedPixels, BufferedImage bimg, String key) {
        int w = bimg.getWidth();
        int h = bimg.getHeight();

        // Get first 4 bytes to decode message length
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitOutputStream bos = new BitOutputStream(baos);
        
        //System.out.println("[Index]: ");
        for (int i = 0; i < 8 * 4; i++) {
            int index = nextPixelIndex(prng, usedPixels, h, w);

            ////System.out.print(index + ", ");

            int x = getPixelX(index, h, w);
            int y = getPixelY(index, h, w);
            int byteNumber = getPixelZ(index, h, w);

            int pixel = bimg.getRGB(x, y);

            int lsb = (pixel >> (8 * byteNumber)) & 0x01;
            //System.out.println("lsb: " + lsb);
            bos.writeBits(1, lsb);
        }
        bos.flush();

        byte[] lengthBytes = baos.toByteArray();

        
        // Pack length bytes to 32 bit int
        int length = ((0xFF & lengthBytes[0]) << 24) | ((0xFF & lengthBytes[1]) << 16) | ((0xFF & lengthBytes[2]) << 8) | (0xFF & lengthBytes[3]);
        return length;
    }

    private static boolean validateHeaderByte(BufferedImage bimg, RC4 prng, Set<Integer> usedBytes, String key) {
        int w = bimg.getWidth();
        int h = bimg.getHeight();
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitOutputStream bos = new BitOutputStream(baos);
        
        byte expected = prng.getByte();
        
        // Read 8 bits from image
        for (int i = 0; i < 8; i++) {
            int index = nextPixelIndex(prng, usedBytes, h, w);

            ////System.out.print(index + ", ");

            int x = getPixelX(index, h, w);
            int y = getPixelY(index, h, w);
            int byteNumber = getPixelZ(index, h, w);

            int pixel = bimg.getRGB(x, y);

            int lsb = (pixel >> (8 * byteNumber)) & 0x01;

            //System.out.println("lsb: " + lsb);
            
            bos.writeBits(1, lsb);
        }

        bos.flush();            
        
        byte got = baos.toByteArray()[0];
        
        //System.out.println("[Validation byte]: " + got + " = " + expected);
        
        return got == expected;
    }
    
    public static short[] readBytesFromImage(BufferedImage bimg, String key) {
        //System.out.println("[Key] " + key);

        int w = bimg.getWidth();
        int h = bimg.getHeight();

        RC4 prng = new RC4(EncryptionProvider.sha256(key.getBytes()));
        Set<Integer> usedPixels = new HashSet<>();

        // Invalid header
        if (!validateHeaderByte(bimg, prng, usedPixels, key)) {
            return null;
        }
        
        int length = readMessageLength(prng, usedPixels, bimg, key);

        //System.out.println("[Length] " + length);
        if (length <= 0) {
            return null;
        }

        // Get first 4 bytes to decode message length
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitOutputStream bos = new BitOutputStream(baos);

        // Read data bits, skip first validation byte, skip first 4 bytes (message length)
        for (int i = 8 * (4 + 1) - 1; i < 8 * length + 32 + 7; i++) {
            int index = nextPixelIndex(prng, usedPixels, h, w);

            ////System.out.print(index + ", ");

            int x = getPixelX(index, h, w);
            int y = getPixelY(index, h, w);
            int byteNumber = getPixelZ(index, h, w);

            int pixel = bimg.getRGB(x, y);

            int lsb = (pixel >> (8 * byteNumber)) & 0x01;

            //System.out.println("lsb: " + lsb);
            
            bos.writeBits(1, lsb);
        }

        bos.flush();

        
        byte[] data = baos.toByteArray();
        short[] d = new short[data.length];
        for (int i = 0; i < data.length; i++) {
            d[i] = (short) (data[i] & 0xFF);
        }
        
        
        //System.out.println("[Data array] " + Arrays.toString(data));

        return d;
    }
}
