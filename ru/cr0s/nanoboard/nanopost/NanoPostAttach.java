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

import cr0s.nanoboard.util.ByteUtils;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * A NanoPost attach container object
 * @author Cr0s
 */
public class NanoPostAttach {
    private byte[] attachData;
    private String fileName;
    
    public NanoPost post;
    
    private File localFile;
    
    public NanoPostAttach(byte[] attachData, String fileName, File localFile) {
        this.attachData = attachData;
        this.fileName = fileName;
        this.localFile = localFile;
    }

    /**
     * @return the attachData
     */
    public byte[] getAttachData() {
        return attachData;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return the post
     */
    public NanoPost getPost() {
        return post;
    }

    /**
     * @return the localFile
     */
    public File getLocalFile() {
        return localFile;
    }

    void writeToStream(DataOutputStream dos) throws IOException {
        dos.writeUTF(fileName);
        dos.writeInt((int)localFile.length());
        
        dos.write(ByteUtils.readBytesFromFile(localFile));
    }
}
