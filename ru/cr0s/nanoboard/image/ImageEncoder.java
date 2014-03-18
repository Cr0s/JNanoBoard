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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 * Steganography encoder by LSB method
 *
 * @author Cr0s
 */

/*
 * There is a sample of data encoding:
 * 
 * Image shorts stored in ARGB format, which means each pixel has stored in 4 shorts:
 * A        R        G        B
 * AAAAAAAA RRRRRRRR GGGGGGGG BBBBBBBB
 * 
 * By default we will store our message into last 3 bits of R, G and last 2 bits of B color component of pixel:
 * AAAAAAAA RRRRRMMM GGGGGMMM BBBBBBMM
 * Where M is a message bits   
 * 
 * In file we store the message length info first PIXELS_PER_LENGTH pixels
 * Then, we going for each EACH_PIXEL (4 by default) pixels and read/write message bits
 */
public class ImageEncoder {
    public final static int EACH_PIXEL = 4;              // Store information in each N pixels
    public final static int MAX_MESSAGE_LENGTH = 1024;

    /**
     * 
     * @param ARGB pixel value
     * @param b short to write (0-255)
     * @return 
     */
    private static int writeByteToPixel(int ARGB, short b) {
        int aValue = ((ARGB >> 24) & 0xff);
        // Message bits: MMMMMMMM
        // Colors:       RRRGGGBB

        // At R color component we store at last 3 bits by default: RRRRRRRR -> RRRRRMMM
        int rValue = (((ARGB >> 16) & 0xFF) >> 3) << 3;
        //System.out.println("Bits: " + Integer.toBinaryString((b >> (8 - 3)) & 0xFF));
        //System.out.println("B: " + Integer.toBinaryString(rValue));
        rValue |= (b >> (8 - 3)) & 0xFF;
        //System.out.println("A: " + Integer.toBinaryString(rValue));

        // At G color component we store at last 3 bits by default: GGGGGGGG -> GGGGGMMM
        int gValue = (((ARGB >> 8) & 0xFF) >> 3) << 3;
        gValue |= ((b >> 2) & 7) & 0xFF;

        // And, finally, store at last 2 bits of B color component by default: BBBBBBBB -> BBBBBBMM
        int bValue = ((ARGB & 0xff) >> (3 - 1)) << (3 - 1);
        bValue |= (b & 0x03) & 0xFF;

        // Compact pixel back
        ARGB = 0;             // A - transparency, R - red color, G - green color, B - blue color, M - message bits
        ARGB |= aValue << 24; // AAAAAAAA 00000000 00000000 00000000
        ARGB |= rValue << 16; // AAAAAAAA RRRRRMMM 00000000 00000000
        ARGB |= gValue << 8;  // AAAAAAAA RRRRRMMM GGGGGMMM 00000000
        ARGB |= bValue;       // AAAAAAAA RRRRRMMM GGGGGMMM BBBBBBMM   
            
        return ARGB;
    }
    
    /**
     * 
     * @param ARGB pixel
     * @return data short, extracted from pixel (0-255)
     */
    public static short readByteFromPixel(int ARGB) {
        short result;

        // Read 3 bits from R, 3 bits from G and 2 bits from B and pack bits into 8-bits short (short) value (-128 - 127)
        int msgByte = (((ARGB >> 16) & 0x7) << 5);
        msgByte |= (((ARGB >> 8) & 0x7) << 2);
        msgByte |= ((ARGB & 0x3));

        result = (short) (msgByte & 0xFF);    
        
        return result;
    }
    
    /**
     * Write some custom data shorts (as short 0-255) into image and saves it into file
     *
     * Bytes sequence followed little endian
     * @param bimg opened source buffered image
     * @param msg shorts to store
     * @param filename
     */
    public static void writeBytesToImage(BufferedImage bimg, short[] msg, String filename, Random rng) throws ImageWriteException {

        int w = bimg.getWidth();
        int h = bimg.getHeight();

        if (msg.length > Math.pow(2, 8 * 4)) {
            throw new ImageWriteException("Message length exceeds possible length (" + msg.length + " > " + Math.pow(2, 8 * 4) + ")");
        } else {
            // Encode message length into first 4 pixels (integer has 4 shorts)
            // We don't write message length info into A bytes
            // Note that shorts of number following LITTLE ENDIAN order
            
            for (int b = 0; b < 4; b++) {
                // Get one short from length
                short lengthByte = (short) ((msg.length >> 8 * b) & 0xFF);
                
                // Write short (unsigned byte) to image
                int pixel = bimg.getRGB(b, 0);
                pixel = writeByteToPixel(pixel, (short)lengthByte);
                bimg.setRGB(b, 0, pixel);
            }

            for (int i = 1, msgPos = 0, row = 0, j = 0; row < h; row++) {
                for (int col = 4; col < w && j < msg.length; col++, i++) {
                    if (rng.nextBoolean()) {
                        int ARGB = bimg.getRGB(col, row);
                        ARGB = writeByteToPixel(ARGB, msg[msgPos]);

                        bimg.setRGB(col, row, ARGB);

                        msgPos++;
                        j++;
                    }
                }
            }

            try {
                File outputfile = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + filename);
                ImageIO.write(bimg, "png", outputfile);
            } catch (IOException e) { 
                throw new ImageWriteException("Error writing to file: " + e.getMessage());
            }
        }
    }

    /**
     * Get message length info from PNG file
     * 
     * Reading first PIXELS_PER_LENGTH pixels
     * Length shorts following LITTLE_ENDIAN order
     * @param bimg
     * @return 
     */
    private static int decodeMessageLength(BufferedImage bimg) {
        int result = 0;      
        for (int b = 0; b < 4; b++) {
            int pixel = bimg.getRGB(4 - b - 1, 0);
            
            int lengthByte = readByteFromPixel(pixel);
            
            // Compacting shorts into numeric value, starting from lower shorts
            result = (result << 8) + lengthByte;
        }
        
        return result;
    }
    
    public static short[] readBytesFromImage(BufferedImage bimg, Random rng) {
        int w = bimg.getWidth(), h = bimg.getHeight();

        int msglength = decodeMessageLength(bimg);
        
        short[] result = new short[msglength];
        
        for (int row = 0, j = 0; row < h; row++) {
            for (int col = 4; col < w && j < msglength; col++) {

                if (rng.nextBoolean()) {
                    int pixelByte = bimg.getRGB(col, row);
                    
                    result[j] = (short)readByteFromPixel(pixelByte);
                    j++;
                }
            }   
        }
        
        return result; 
    }
}
