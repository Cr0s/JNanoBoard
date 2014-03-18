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
package cr0s.nanoboard.rules;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Rules manager. Implements loading/saving rules, adding or removing rules.
 * @author Cr0s
 */
public class RulesManager {
    private static RulesManager instance;
    
    private ArrayList<Rule> rulesList = new ArrayList<>();
    
    public static synchronized RulesManager getInstance() {
        if (instance == null) {
            instance = new RulesManager();
        }
        
        return instance;
    }
    
    /**
     * Load NanoPosts gathering rules from directory
     * 
     * 
     * @param rulesDir 
     */
    public void loadRules(String rulesDir) throws Exception {
        this.rulesList.clear();
        
        File dir = new File(rulesDir);
        
        if (!dir.exists() || !dir.isDirectory()) {
            throw new Exception("Cannot load rules from rules directory!");
        }
        
        File[] rules = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File directory, String fileName) {
                return fileName.endsWith(".rule");
            }
        });
        
        for (File f : rules) {
            Rule rule = new Rule(f);
            rulesList.add(rule);
        }
    }
    
    /**
     * Save rules list into directory with rules files
     * @param rulesDir directory path
     */
    public void saveRules(String rulesDir) {
        for (Iterator<Rule> it = rulesList.iterator(); it.hasNext();) {
            Rule r = it.next();
            r.saveToFile(new File(rulesDir));
        }
    }
    
    public ArrayList<Rule> getRulesList() {
        return this.rulesList;
    }
    
    /**
     * Returns rule object by name
     * @param name rule name
     * @return rule object or null
     */
    public Rule getRuleByName(String name) {
        for (Rule r : rulesList) {
            if (r.getRuleName().equals(name)){
                return r;
            }
        }
        
        return null;
    }
}
