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

import cr0s.nanoboard.rules.Rule;

/**
 * Synchronization task state
 * @author Cr0s
 */
public class SyncTaskState {
    public Rule rule;
    public String imageUrl;
    public String status;
    public int progressValue;
    
    public SyncTaskState(Rule rule, String imageUrl, String status, int progressValue) {
        this.rule = rule;
        this.imageUrl = imageUrl;
        this.status = status;
        this.progressValue = progressValue;
    }
}
