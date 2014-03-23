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
package cr0s.nanoboard.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Downloads data from the internet by HTTP protocol
 *
 * @author Cr0s
 */
public class HttpDownloader {

    /**
     * Gets page or text file content as a string by HTTP protocol
     * @param urlString url of page/text file
     * @return contents as string
     * @throws MalformedURLException if URL is invalid
     * @throws IOException if something with I/O goes wrong
     */
    public static String getString(String urlString) throws MalformedURLException, IOException {
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;
        StringBuilder result = new StringBuilder();

        try {
            url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("User-Agent", "User-Agent: Mozilla/5.0 (Windows NT 6.1; rv:27.0) Gecko/20100101 Firefox/27.0");
            
            is = connection.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
        } finally {
            if (is != null) {
                is.close();
            }
        }

        return result.toString();
    }

    /**
     * Downloads file by HTTP protocol from the internet
     * @param fileName name of local file to save
     * @param urlString address of file
     * @throws MalformedURLException if URL is invalid
     * @throws IOException if something with I/O goes wrong
     */
    public void saveUrl(final String fileName, final String urlString)
            throws MalformedURLException, IOException {
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        
        try {
            in = new BufferedInputStream(new URL(urlString).openStream());
            fout = new FileOutputStream(fileName);

            final byte data[] = new byte[1024];
            int bytesCount;
            
            while ((bytesCount = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, bytesCount);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (fout != null) {
                fout.close();
            }
        }
    }
}
