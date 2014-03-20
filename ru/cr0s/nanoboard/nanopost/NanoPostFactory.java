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
import java.util.Arrays;

/**
 * A NanoPost factory, checks creates NanoPosts from byte arrays
 * 
 * NanoPost data structure:
 * [Post hash (64 bytes) | Parent hash or zeros (64 bytes) | Post data bytes] 
 * 
 * @author Cr0s
 */
public class NanoPostFactory {
    public static NanoPost getNanoPost(byte[] data) throws MalformedNanoPostException {
        // 1. Determine data size
        int dataLength = data.length;
        
        if (dataLength < EncryptionProvider.HASH_SIZE_BYTES * 2) {
            throw new MalformedNanoPostException("Post data is too small");
        }
        
        // 2. Get post hash
        byte[] postHash; // SHA-512
        postHash = Arrays.copyOfRange(data, 0, EncryptionProvider.HASH_SIZE_BYTES);
        assert postHash.length == EncryptionProvider.HASH_SIZE_BYTES;
        
        // 3. Get hash of data and compare
        byte[] dataHashToCheck; // SHA-512
        dataHashToCheck = EncryptionProvider.sha512(Arrays.copyOfRange(data, EncryptionProvider.HASH_SIZE_BYTES + 1, dataLength));
        assert dataHashToCheck.length == EncryptionProvider.HASH_SIZE_BYTES;
        
        boolean result = Arrays.equals(postHash, dataHashToCheck);
        if (!result) {
            throw new MalformedNanoPostException("Post hash and real hash of data is not equals. Is post data corrupted?");
        }
        
        // 4. Get parent hash
        byte[] parentHash = Arrays.copyOfRange(data, EncryptionProvider.HASH_SIZE_BYTES + 1, 2 * EncryptionProvider.HASH_SIZE_BYTES);
        assert parentHash.length == EncryptionProvider.HASH_SIZE_BYTES;
        return null;
    }
}
