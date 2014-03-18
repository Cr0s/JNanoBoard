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
package cr0s.nanoboard.rules.parser;

import cr0s.nanoboard.rules.Rule;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse any kind of test (HTML code, as example) by specified rule
 * @author user
 */
public class RuleDrivenTextParser {
    private Rule rule;
    
    private Pattern rulePattern;
    
    public RuleDrivenTextParser(Rule rule) {
        this.rule = rule;
        
        rulePattern = Pattern.compile(this.rule.getRuleRegExpr());
    }
    
    public ArrayList<String> parseTextByRule(String inputText) {
        ArrayList<String> resultList = new ArrayList<>();
        
        Matcher ruleMatcher = rulePattern.matcher(inputText);
        
        while (ruleMatcher.find()) {
            resultList.add(ruleMatcher.group(1));
        }
        
        return resultList; 
    }
    
    @Override
    public String toString() {
        return String.format("Rule [%s, %s]", this.rule.getRuleName(), this.rule.getRuleRegExpr());
    }
}
