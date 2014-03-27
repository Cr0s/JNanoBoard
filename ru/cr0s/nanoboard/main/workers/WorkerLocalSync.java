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
package cr0s.nanoboard.main.workers;

import cr0s.nanoboard.image.ImageUtils;
import cr0s.nanoboard.main.MainClass;
import cr0s.nanoboard.main.NBFrame;
import cr0s.nanoboard.nanopost.MalformedNanoPostException;
import cr0s.nanoboard.nanopost.NanoPost;
import cr0s.nanoboard.nanopost.NanoPostFactory;
import cr0s.nanoboard.rules.Rule;
import cr0s.nanoboard.util.ByteUtils;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.SwingWorker;

/**
 * Rule testing worker
 *
 * @author Cr0s
 */
public class WorkerLocalSync extends SwingWorker<Void, SyncTaskState> {

    private NBFrame nbf;
    private int totalProgressValue;
    private String boardCode;

    public WorkerLocalSync(NBFrame nbf, String boardCode) {
        this.nbf = nbf;
        this.boardCode = boardCode;
    }

    @Override
    protected Void doInBackground() {
        // Scan nanoposts dir
        File dir = new File(MainClass.NANOPOSTS_DIR);
        if (!dir.exists() || !dir.isDirectory()) {
            publish (new SyncTaskState(new Rule(), "", "Can't load nanoposts directory!", 0));
            return null;
        }
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File directory, String fileName) {
                return fileName.endsWith(".nanopost.png");
            }
        });
        
        // Scan outbox directory
        File dirOutbox = new File(MainClass.OUTBOX_DIR);
        if (!dirOutbox.exists() || !dirOutbox.isDirectory()) {
            publish (new SyncTaskState(new Rule(), "", "Can't load outbox directory!", 0));
            return null;
        }
        File[] outbox = dirOutbox.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File directory, String fileName) {
                return fileName.endsWith(".nanopost.png");
            }
        });        
        
        int currentProgress = 0;
        this.totalProgressValue = files.length + outbox.length;
        
        for (File f : files) {
            try {
                byte[] dataBytes = ImageUtils.tryToDecodeSteganoImage(ByteUtils.readBytesFromFile(f), boardCode);

                try {
                    NanoPost nanoPost = NanoPostFactory.getNanoPostFromBytes(dataBytes, false);
                    nanoPost.setSourceImageData(ByteUtils.readBytesFromFile(f));
                    nanoPost.setOutbox(false);
                    
                    nbf.addNanoPostToList(nanoPost);
                    
                    currentProgress++;
                    publish (new SyncTaskState(new Rule(), "", nanoPost.toString(), currentProgress));
                } catch (MalformedNanoPostException ex) {
                    Logger.getLogger(NBFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | IOException ex) {
                Logger.getLogger(NBFrame.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }
        
        for (File f : outbox) {
            try {
                byte[] dataBytes = ImageUtils.tryToDecodeSteganoImage(ByteUtils.readBytesFromFile(f), boardCode);

                try {
                    NanoPost nanoPost = NanoPostFactory.getNanoPostFromBytes(dataBytes, true);
                    nanoPost.setSourceImageData(ByteUtils.readBytesFromFile(f));
                    nanoPost.setOutbox(true);
                    
                    nbf.addNanoPostToList(nanoPost);
                    
                    currentProgress++;
                    publish (new SyncTaskState(new Rule(), "", "outbox: " + nanoPost.toString(), currentProgress));
                } catch (MalformedNanoPostException ex) {
                    Logger.getLogger(NBFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | IOException ex) {
                Logger.getLogger(NBFrame.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }        
        
        publish (new SyncTaskState(new Rule(), "", "Complete", this.totalProgressValue));
        return null;
    }

    @Override
    protected void process(List<SyncTaskState> chunks) {
        for (SyncTaskState sts : chunks) {
            nbf.setLocalSyncProgress(sts.status, sts.progressValue, this.totalProgressValue);
        }
    }
}
