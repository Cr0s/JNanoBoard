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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
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
    
    private ArrayList<NanoPost> childs = new ArrayList<>();
    private boolean outbox = false;
    
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
    
    public void saveToFile(boolean outbox) throws IOException {
        String nanopostsDir = getNanopostsDir(outbox);
        
        // Save nanopost file
        ByteUtils.writeBytesToFile(getNanoPostFile(outbox), this.sourceImageData);
        
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
    
    public String getNanopostsDir(boolean outbox) {
        String nanopostsDir;
        
        if (!outbox) { 
            nanopostsDir = MainClass.NANOPOSTS_DIR + System.getProperty("file.separator");
        } else {
            nanopostsDir = MainClass.OUTBOX_DIR + System.getProperty("file.separator");
        }
        
        if (!new File(nanopostsDir).exists()) {
            new File(nanopostsDir).mkdir();
        }   
        
        return nanopostsDir;
    }
    
    public boolean isAlreadyDownloaded() {
        String nanopostsDir = getNanopostsDir(this.outbox);
        return new File(nanopostsDir + ByteUtils.bytesToHexString(postHash) + ".nanopost.png").exists();
    }
    
    public File getNanoPostFile(boolean outbox) {
        String nanopostsDir = getNanopostsDir(outbox);
        return new File(nanopostsDir + ByteUtils.bytesToHexString(postHash) + ".nanopost.png");       
    }
    
    public String getShortHash() {
        return getPostHash().substring(0, 10);
    }
    
    public int getPostTimestamp() {
        return this.postTimestamp;
    }
    
    public String postDateToString() {
        return (new SimpleDateFormat("yyyy-MM-dd HH-mm-ss")).format(new Date(this.postTimestamp * 1000L));    
    }
    
    public void addChild(NanoPost np) {
        this.childs.add(np);
    }
    
    public ArrayList<NanoPost> getChilds() {
        return this.childs;
    }
    
    public boolean isParentOf(NanoPost np) {
        return /*!np.isOpPost() &&*/ Arrays.equals(this.postHash, np.parentHash);
    }
    
    @Override
    public String toString() {
        String isOp = (this.isOpPost()) ? "[OP]" : "";
        
        if (this.attachData == null) {
            return String.format("%s (%s) %s", getShortHash(), postDateToString(), isOp);
        } else {
            if (this.attachData.getLocalFile() != null) {
                return String.format("%s (%s) - %s (%.2f Mb) %s", getShortHash(), postDateToString(), this.attachData.getFileName(), (this.attachData.getLocalFile().length() / 1024.0f / 1024.0f), isOp);
            } else {
                return String.format("%s (%s) - %s %s", getShortHash(), postDateToString(), this.attachData.getFileName(), isOp);                
            }
        }
    }
    
    /**
     * Gets last NanoPost's child in childs list
     * @return 
     */
    public NanoPost getLastChildPost() {
        if (!this.childs.isEmpty()) {
            return this.childs.get(this.childs.size() - 1);
        } else {
            return null;
        }
    }
    
    /**
     * Disposes all binary data to make attempt to free memory by allowing GC to collect these binary arrays
     */
    public void clearAllBinaryData() {
        this.sourceImageData = null;
        
        if (this.attachData != null) {
            this.attachData.clearBinaryData();
        }
    }
    
    /**
     * Sorts NanoPosts OP-s by timestamp (newest at the top), check for bumps of OP-s and raise bumped OP-s up
     * @param nanoposts 
     */
    public static void sortNanoPostsOpsByTimestamp(ArrayList<NanoPost> nanoposts) {
        ArrayList<NanoPost> nonOps = new ArrayList<>();

        // Remove from main list all non-ops and save removed non-ops in other list
        for (Iterator<NanoPost> i = nanoposts.listIterator(); i.hasNext();) {
            NanoPost item = i.next();
            
            if (!item.isOpPost()) {
                nonOps.add(item);
                
                i.remove();
            }   
        }      
        
        // Sorting only OP-posts remaining
        Collections.sort(nanoposts, new Comparator<NanoPost>(){
            @Override
            public int compare(NanoPost p1, NanoPost p2) {
                if (p1.isOpPost() && p2.isOpPost()) {
                    
                    int ts1 = p1.getPostTimestamp();
                    int ts2 = p2.getPostTimestamp();
                    
                    // Checking bump condition
                    // Last child post in one thread is newer than last child post in other thread
                    NanoPost p1Bump = p1.getLastChildPost();
                    NanoPost p2Bump = p2.getLastChildPost();     
                    
                    if (p1Bump != null) {
                        ts1 = p1Bump.getPostTimestamp();
                    }

                    if (p2Bump != null) {
                        ts2 = p2Bump.getPostTimestamp();
                    }
           
                    return ts2 - ts1;
                } else {
                    return 0;
                }
            }
        });  
        
        // Append non-ops posts to the end
        nanoposts.addAll(nonOps);
    }
    
    /**
     * Sorts NanoPost's childs by timestamp (oldest at thebtop)
     * @param np 
     */
    public static void sortNanoPostsChildsByTimestamp(NanoPost np) {
        if (np.getChilds().isEmpty()) {
            return;
        }
        
        Collections.sort(np.getChilds(), new Comparator<NanoPost>(){
            @Override
            public int compare(NanoPost p1, NanoPost p2) {
                return p1.getPostTimestamp() - p2.getPostTimestamp();
            }
        });         
    }
    
    /**
     * Adds chils in childs list by checking parentness relationship in hashes
     * @param npList list of NanoPosts
     */
    public static void addChildsToNanoPostsInList(ArrayList<NanoPost> npList) {
        for (NanoPost np : npList) {
            np.getChilds().clear();
            
            for (NanoPost npp : npList) {
                if (np.isParentOf(npp)) {
                    np.addChild(npp);
                }
            }
            
            // Sort childs by timestamp
            sortNanoPostsChildsByTimestamp(np);
        }       
    }

    /**
     * Sets attach data to specified NanoPostAttach object
     * @param npa attach info
     */
    public void setAttach(NanoPostAttach npa) {
        this.attachData = npa;
    }
    
    public void setOutbox(boolean outbox) {
        this.outbox = outbox;
    }
    
    public boolean isFromOutbox() {
        return this.outbox;
    }
}
