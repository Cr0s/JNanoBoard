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
import cr0s.nanoboard.main.RuleDialog;
import cr0s.nanoboard.rules.Rule;
import cr0s.nanoboard.rules.parser.RuleDrivenTextParser;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingWorker;

/**
 * Rule testing worker
 * @author Cr0s
 */
public class WorkerTestRule extends SwingWorker<Void, TestTaskState> {

    private RuleDialog rd;
    private Rule rule;
    
    public WorkerTestRule(RuleDialog rd, Rule rule) {
        this.rd = rd;
        this.rule = rule;
    }
    
    @Override
    protected Void doInBackground() {
        publish(new TestTaskState(0, "[*] Checking " + rule.toString()));
        
        String htmlPage = "";
        
        try {
            htmlPage = HttpDownloader.getString(rule.getRuleURL());
        } catch(MalformedURLException ex) { 
            publish(new TestTaskState(0, "[!] Invalid rule URL: " + ex.getMessage()));
            //this.cancel(true);
            return null;
        } catch (IOException ex) {
            publish(new TestTaskState(0, "[!] Can't get HTML content by rule: " + ex.getMessage()));
            //this.cancel(true);
            return null;            
        }
        
        publish(new TestTaskState(1, "[*] Got page content, checking by rule's regexpr... (" + rule.getRuleRegExpr() + ")"));
        
        RuleDrivenTextParser rdtp = new RuleDrivenTextParser(rule);
        
        ArrayList<String> resultList = rdtp.parseTextByRule(htmlPage);
        if (resultList.size() > 0) {
            publish(new TestTaskState(2, "[*] Found some matches, you can see it in matches tab"));
            
            for (String s : resultList) {
                publish(new TestTaskState(-1, s));
            }
        } else {
            publish(new TestTaskState(0, "[!] There is no results matched with rule's regexpr!"));
        }
        
        
        publish(new TestTaskState(3, "[*] Testing finished!"));
        return null;
    }
    
    @Override
    protected void process(List<TestTaskState> chunks) {
        for (TestTaskState chunk : chunks) {
            int progressValue = chunk.i;
            String stringToLog = chunk.s;
            
            if (progressValue == -1) {
                rd.addToMatchesList(stringToLog);
                continue;
            }
            
            rd.setTestingProgress(progressValue);
            rd.addToLog(stringToLog);
        }
    }
}
