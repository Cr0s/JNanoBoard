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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * Compression and decompression routines
 *
 * @author Cr0s
 */
public class CompressionProvider {

    public static byte[] compressBytes(byte[] source) throws IOException, DataFormatException {
        Deflater deflater = new Deflater(9);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DeflaterOutputStream dos = new DeflaterOutputStream(baos, deflater);

        dos.write(source, 0, source.length);

        dos.finish();

        return baos.toByteArray();

    }

    public static byte[] decompressBytes(byte[] source) throws IOException, DataFormatException {
        ByteArrayInputStream bais = new ByteArrayInputStream(source);
        Inflater inflater = new Inflater();
        InflaterInputStream iis = new InflaterInputStream(bais, inflater);

        ByteArrayOutputStream uncompressedBaos = new ByteArrayOutputStream();

        for (int c = iis.read(); c != -1; c = iis.read()) {
            uncompressedBaos.write(c);
        }

        return uncompressedBaos.toByteArray();
    }
}
