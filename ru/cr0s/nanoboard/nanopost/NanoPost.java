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
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A NanoPost object
 * @author Cr0s
 */
public class NanoPost {
    public static final String NANOPOSTS_DIR = "nanoposts";
    private byte[] postHash;
    private byte[] parentHash;
    private int postTimestamp;
    
    private String postText;
    private NanoPostAttach attachData;
    
    private byte[] sourceImageData;
    
    public NanoPost(byte[] postHash, byte[] parentHash, String postText, int postTimestamp, NanoPostAttach attachData) {
        this.postHash = postHash;
        this.parentHash = parentHash;
        this.postText = postText;
        this.attachData = attachData;
        this.postTimestamp = postTimestamp;
    }
    
    public String getPostHash() {
        return ByteUtils.bytesToHexString(this.postHash);
    }
    
    public String getParentHash() {
        return ByteUtils.bytesToHexString(this.parentHash);
    }
    
    public String getPostText() {
        return this.postText;
    }
    
    public boolean isOpPost() {
        return ByteUtils.bytesToHexString(this.parentHash).equals("0000000000000000000000000000000000000000000000000000000000000000");
    }
    
    public NanoPostAttach getAttach() {
        return this.attachData;
    }
    
    public void setAttachPost() {
        if (this.attachData != null) {
            this.attachData.post = this;
        }
    }
    
    public void setSourceImageData(byte[] imgBytes) {
        this.sourceImageData = imgBytes;
    }
    
    public byte[] getAsBytes() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.write(postHash);
            dos.write(parentHash);
            
            dos.writeUTF(postText);
            
            if (this.attachData == null) {
                dos.writeUTF(""); // There is no attachment
            } else {
                this.attachData.writeToStream(dos);
            }
            
            dos.writeInt(this.postTimestamp);
            
            return baos.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(NanoPost.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public void saveToFile() throws IOException {
        String nanopostsDir = System.getProperty("user.dir") + System.getProperty("file.separator") + NANOPOSTS_DIR + System.getProperty("file.separator");
        
        // Save nanopost file
        File npFileImg = new File(nanopostsDir + ByteUtils.bytesToHexString(postHash) + ".png"); 
        ByteUtils.writeBytesToFile(npFileImg, this.sourceImageData);
        
        // Save post text
        String postDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date(this.postTimestamp * 1000));
        File npFilePost = new File(nanopostsDir + ByteUtils.bytesToHexString(postHash) + "_"  + postDate + ".txt"); 
        ByteUtils.writeBytesToFile(npFilePost, this.postText.getBytes("UTF-8"));        
        
        // If attach file is exists
        if (this.attachData != null) {
            // Save attach
            File npFileAttach = new File(nanopostsDir + ByteUtils.bytesToHexString(postHash) + "_" + this.attachData.getFileName()); 
            ByteUtils.writeBytesToFile(npFileAttach, this.attachData.getAttachData());  
        }
    }
    
    public boolean isAlreadyDownloaded() {
        String nanopostsDir = System.getProperty("user.dir") + System.getProperty("file.separator") + NANOPOSTS_DIR + System.getProperty("file.separator");
        return new File(nanopostsDir + ByteUtils.bytesToHexString(postHash) + ".png").exists();
    }
}
