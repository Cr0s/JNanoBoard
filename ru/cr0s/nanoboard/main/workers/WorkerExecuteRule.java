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

import cr0s.nanoboard.http.HttpDownloader;
import cr0s.nanoboard.main.NBFrame;
import cr0s.nanoboard.main.RuleDialog;
import cr0s.nanoboard.rules.Rule;
import cr0s.nanoboard.rules.parser.RuleDrivenTextParser;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

/**
 * Rule testing worker
 *
 * @author Cr0s
 */
public class WorkerExecuteRule extends SwingWorker<Void, RuleTaskState> {

    private NBFrame nbf;
    private Rule rule;

    public WorkerExecuteRule(NBFrame nbf, Rule rule) {
        this.nbf = nbf;
        this.rule = rule;
    }

    @Override
    protected Void doInBackground() {
        String htmlPage = "";

        try {
            htmlPage = HttpDownloader.getString(rule.getRuleURL());
        } catch (MalformedURLException ex) {
            //this.cancel(true);
            return null;
        } catch (IOException ex) {
            //this.cancel(true);
            return null;
        }

        RuleDrivenTextParser rdtp = new RuleDrivenTextParser(rule);

        try {
            URL url = new URL(rule.getRuleURL());
            ArrayList<String> resultList = rdtp.parseTextByRule(htmlPage);
            if (resultList.size() > 0) {
                for (String s : resultList) {
                    publish(new RuleTaskState(-1, url.getProtocol() + "://" + url.getHost() + s));
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void process(List<RuleTaskState> chunks) {
        for (RuleTaskState chunk : chunks) {
            int progressValue = chunk.i;
            String imageUrl = chunk.s;

            if (progressValue == -1) {
                if (nbf.downloadedUrls.get(imageUrl) != null && nbf.downloadedUrls.get(imageUrl)) {
                    continue;
                }

                nbf.createWorkerByRuleMatch(rule, imageUrl);
                nbf.downloadedUrls.put(imageUrl, Boolean.TRUE);
            }
        }
    }
}
