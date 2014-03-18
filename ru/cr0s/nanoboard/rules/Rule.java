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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * NanoPost (.png file, actually) gathering rule record
 * @author Cr0s
 */
public class Rule {
    private String ruleName;
    private String ruleURL;
    private String ruleRegExpr;
    private boolean isEnabled;
    
    public Rule() {    
    }
    
    public Rule(String ruleName, String ruleURL, String ruleRegExpr, boolean isEnabled) {
        this.ruleName = ruleName;
        this.ruleURL = ruleURL;
        this.ruleRegExpr = ruleRegExpr;
        this.isEnabled = isEnabled;
    }
    
    public Rule(File file) {
        String[] s = new String[4];
        BufferedReader br = null;
        
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            int i = 0;
            
            while ((line = br.readLine()) != null) {
                s[i++] = line;
            }
        } catch (IOException ex) {
            Logger.getLogger(Rule.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(Rule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (s.length != 4) {
            return;
        }
        
        this.ruleName = s[0];
        this.ruleURL = s[1];
        this.ruleRegExpr = s[2];
        this.isEnabled = s[3].equals("1");
    }
    
    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleURL() {
        return ruleURL;
    }

    public void setRuleURL(String ruleURL) {
        this.ruleURL = ruleURL;
    }

    public String getRuleRegExpr() {
        return ruleRegExpr;
    }

    public void setRuleRegExpr(String ruleRegExpr) {
        this.ruleRegExpr = ruleRegExpr;
    }
    
    /**
     * Save rule into file
     * @param f rules directory
     */
    public void saveToFile(File f) {
        try {
            File out = new File(f.getPath() + System.getProperty("file.separator") + getRuleName() + ".rule");
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(out, false))) {
                bw.write(getRuleName()); bw.newLine();
                bw.write(getRuleURL()); bw.newLine();
                bw.write(getRuleRegExpr()); bw.newLine();
                bw.write((this.isIsEnabled()) ? "1" : "0"); bw.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }

    /**
     * @return the isEnabled
     */
    public boolean isIsEnabled() {
        return isEnabled;
    }

    /**
     * @param isEnabled the isEnabled to set
     */
    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object instanceof Rule) {
            return (
                    this.ruleName.equals(((Rule)object).ruleName)
                    && this.ruleURL.equals(((Rule)object).ruleURL)
                    && this.ruleRegExpr.equals(((Rule)object).ruleRegExpr)
                    );
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.ruleName);
        hash = 41 * hash + Objects.hashCode(this.ruleURL);
        hash = 41 * hash + Objects.hashCode(this.ruleRegExpr);
        return hash;
    }
    
    @Override
    public String toString() {
        return String.format("Rule [%s, %s, %s]", this.ruleName, this.ruleURL, this.ruleRegExpr);
    }
}
