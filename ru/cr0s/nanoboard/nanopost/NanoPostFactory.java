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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A NanoPost factory, checks creates NanoPosts from byte arrays
 *
 * NanoPost data structure: [Post hash (64 bytes) | Parent hash or zeros (64
 * bytes) | Post data bytes]
 *
 * @author Cr0s
 */
public class NanoPostFactory {

    public static NanoPost getNanoPostFromBytes(byte[] data) throws MalformedNanoPostException {
        try {
            // 1. Determine data size
            int dataLength = data.length;
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));

            if (dataLength < EncryptionProvider.HASH_SIZE_BYTES * 2) {
                throw new MalformedNanoPostException("Post data is too small");
            }

            // 2. Get post hash
            byte[] postHash = new byte[EncryptionProvider.HASH_SIZE_BYTES]; // SHA-512
            dis.readFully(postHash);

            // 3. Get hash of data and compare
            byte[] dataHashToCheck; // SHA-512

            // Whole nanopost data to check (including parent hash)
            byte[] npData = new byte[dataLength - EncryptionProvider.HASH_SIZE_BYTES];
            dis.readFully(npData);//, EncryptionProvider.HASH_SIZE_BYTES, 2 * EncryptionProvider.HASH_SIZE_BYTES);

            dataHashToCheck = EncryptionProvider.sha512(npData);
            
            boolean result = Arrays.equals(postHash, dataHashToCheck);
            if (!result) {
                System.err.println("[H] Hash from header: " + ByteUtils.bytesToHexString(postHash));
                System.err.println("[H] Real hash       : " + ByteUtils.bytesToHexString(dataHashToCheck));

                throw new MalformedNanoPostException("Post hash and real hash of data is not equals. Is post data corrupted?");
            }

            dis = new DataInputStream(new ByteArrayInputStream(npData));

            // 4. Get parent hash
            byte[] parentHash = new byte[EncryptionProvider.HASH_SIZE_BYTES];
            dis.readFully(parentHash, 0, EncryptionProvider.HASH_SIZE_BYTES);

            System.out.println("[H] Post hash   : " + ByteUtils.bytesToHexString(postHash));
            System.out.println("[H] Parent hash : " + ByteUtils.bytesToHexString(parentHash));

            // 5. Read post data
            byte[] postData = new byte[dataLength - (2 * EncryptionProvider.HASH_SIZE_BYTES)];
            dis.readFully(postData);//, (2 * EncryptionProvider.HASH_SIZE_BYTES), dataLength);
            dis = new DataInputStream(new ByteArrayInputStream(postData));

            // 6. Read post text
            String postText = dis.readUTF();

            System.out.println("[H] Post text : " + postText);
            
            // 7. Read post attach data
            String attachFileName = dis.readUTF();
            System.out.println("[H] Attach filename : " + attachFileName); 
            
            NanoPostAttach att = null;
            
            if (!attachFileName.isEmpty()) {
                int attachSize = dis.readInt();
                if (attachSize <= 0) {
                    throw new MalformedNanoPostException("Attach size is less or equals ZERO.");
                }
                
                byte[] attachData = new byte[attachSize];
                dis.read(attachData);
                
                att = new NanoPostAttach(attachData, attachFileName, null);
            }

            int postTimestamp = dis.readInt();
            if (postTimestamp < 0) {
                throw new MalformedNanoPostException("Invalid post timestamp!");
            }
            
            NanoPost np = new NanoPost(postHash, parentHash, postText, postTimestamp, att);

            return np;
        } catch (IOException ex) {
            Logger.getLogger(NanoPostFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw new MalformedNanoPostException("IO Error!");
        }
    }

    public static NanoPost createNanoPost(String postText, byte[] parentHash, File postAttach) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        NanoPostAttach att = null;
        try {
            dos.write(parentHash);
            dos.writeUTF(postText);
            
            if (postAttach == null) {
                dos.writeUTF(""); // there is no attached file
            } else {
                att = new NanoPostAttach(ByteUtils.readBytesFromFile(postAttach), postAttach.getName(), postAttach);
                att.writeToStream(dos);
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(NanoPostFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NanoPostFactory.class.getName()).log(Level.SEVERE, null, ex);
        }

        byte[] postHash = EncryptionProvider.sha512(baos.toByteArray());

        // TODO: attach work
        return new NanoPost(postHash, parentHash, postText, (int)(System.currentTimeMillis() / 1000), att);
    }
}
