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

import cr0s.nanoboard.main.MainClass;
import cr0s.nanoboard.stegano.EncryptionProvider;
import cr0s.nanoboard.util.ByteUtils;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A NanoPost object
 * @author Cr0s
 */
public class NanoPost {
    private byte[] postHash;
    private byte[] parentHash;
    private int postTimestamp;
    
    private String postText;
    private NanoPostAttach attachData;
    
    private byte[] sourceImageData;
    
    private LinkedList<NanoPost> childs = new LinkedList<>();
    
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
        return Arrays.equals(this.parentHash, EncryptionProvider.EMPTY_HASH);
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
        String nanopostsDir = MainClass.NANOPOSTS_DIR + System.getProperty("file.separator");
        
        // Save nanopost file
        ByteUtils.writeBytesToFile(getNanoPostFile(), this.sourceImageData);
        
        // Save post text
        String postDate = postDateToString();
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
        String nanopostsDir = MainClass.NANOPOSTS_DIR + System.getProperty("file.separator");
        return new File(nanopostsDir + ByteUtils.bytesToHexString(postHash) + ".png").exists();
    }
    
    public File getNanoPostFile() {
        String nanopostsDir = MainClass.NANOPOSTS_DIR + System.getProperty("file.separator");
        return new File(nanopostsDir + ByteUtils.bytesToHexString(postHash) + ".png");       
    }
    
    public String getShortHash() {
        return getPostHash().substring(0, 10);
    }
    
    public String postDateToString() {
        return (new SimpleDateFormat("yyyy-MM-dd HH-mm-ss")).format(new Date(this.postTimestamp * 1000L));    
    }
    
    public void addChild(NanoPost np) {
        this.childs.add(np);
    }
    
    public LinkedList<NanoPost> getChilds() {
        return this.childs;
    }
    
    public boolean isParentOf(NanoPost np) {
        return !np.isOpPost() && Arrays.equals(this.postHash, np.parentHash);
    }
    
    @Override
    public String toString() {
        if (this.attachData == null) {
            return String.format("%s (%s)", getShortHash(), postDateToString());
        } else {
            if (this.attachData.getLocalFile() != null) {
                return String.format("%s (%s) - %s (%.2f Mb)", getShortHash(), postDateToString(), this.attachData.getFileName(), (this.attachData.getLocalFile().length() / 1024.0f / 1024.0f));
            } else {
                return String.format("%s (%s) - %s", getShortHash(), postDateToString(), this.attachData.getFileName());                
            }
        }
    }
}
