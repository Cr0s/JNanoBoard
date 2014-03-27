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
import cr0s.nanoboard.main.NBFrame;
import cr0s.nanoboard.nanopost.MalformedNanoPostException;
import cr0s.nanoboard.nanopost.NanoPost;
import cr0s.nanoboard.nanopost.NanoPostFactory;
import cr0s.nanoboard.rules.Rule;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import javax.swing.SwingWorker;

/**
 * Rule testing worker
 *
 * @author Cr0s
 */
public class WorkerSyncImage extends SwingWorker<Void, SyncTaskState> {

    private NBFrame nbf;
    private Rule rule;
    private String imageUrl;
    private int totalProgressValue;
    private String boardCode;
    private int tableRowIndex;

    public WorkerSyncImage(NBFrame nbf, String boardCode, Rule rule, String imageUrl, int tableRowIndex) {
        this.nbf = nbf;
        this.boardCode = boardCode;
        this.rule = rule;
        this.imageUrl = imageUrl;
        this.tableRowIndex = tableRowIndex;
    }

    @Override
    protected Void doInBackground() {
        final int BUFFER_SIZE = 1024;

        // 1. Download image
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "User-Agent: Mozilla/5.0 (Windows NT 6.1; rv:27.0) Gecko/20100101 Firefox/27.0");
            
            this.totalProgressValue = connection.getContentLength();

            int totalDataRead = 0;
            bis = new BufferedInputStream(connection.getInputStream());
            baos = new ByteArrayOutputStream();
            try (BufferedOutputStream bos = new BufferedOutputStream(baos, BUFFER_SIZE)) {
                byte[] data = new byte[BUFFER_SIZE];
                int i = 0;

                while ((i = bis.read(data)) != -1) {
                    totalDataRead = totalDataRead + i;
                    baos.write(data, 0, i);

                    publish(new SyncTaskState(rule, imageUrl, "Downloading", totalDataRead));
                }

                publish(new SyncTaskState(rule, imageUrl, "Processing", this.totalProgressValue));

                byte[] imageBytes = baos.toByteArray();
                try {
                    byte[] imageSteganoBytes = ImageUtils.tryToDecodeSteganoImage(imageBytes, boardCode);

                    if (imageSteganoBytes != null) {
                        try {
                            NanoPost np = NanoPostFactory.getNanoPostFromBytes(imageSteganoBytes, false);
                            np.setSourceImageData(imageBytes);
                            
                            if (!np.isAlreadyDownloaded()) {
                                publish(new SyncTaskState(rule, imageUrl, "NEW NANOPOST", this.totalProgressValue));
                                np.setOutbox(false);
                                np.saveToFile(false);
                            } else {
                                publish(new SyncTaskState(rule, imageUrl, "IN SYNC", this.totalProgressValue));
                            }
                            
                            imageSteganoBytes = null;
                            imageBytes = null;
                            np.clearAllBinaryData();
                            nbf.addNanoPostToList(np);
                        } catch (IOException | MalformedNanoPostException ex) {
                            publish(new SyncTaskState(rule, imageUrl, "Not an NanoPost", this.totalProgressValue));
                        }
                    } else {
                        publish(new SyncTaskState(rule, imageUrl, "Not an NanoPost", this.totalProgressValue));
                    }
                } catch (Exception ex) {
                    //ex.printStackTrace();
                    publish(new SyncTaskState(rule, imageUrl, "Not an NanoPost", this.totalProgressValue));
                }
            }
        } catch (IOException ex) {
            publish(new SyncTaskState(rule, imageUrl, "IO Error", 0));
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
                publish(new SyncTaskState(rule, imageUrl, "IO Error", 0));
            }
        }
        
        return null;
    }

    @Override
    protected void process(List<SyncTaskState> chunks) {
        for (SyncTaskState sts : chunks) {
            nbf.setSyncTableRow(this.tableRowIndex, sts.status, sts.progressValue, this.totalProgressValue);
        }
    }
}
