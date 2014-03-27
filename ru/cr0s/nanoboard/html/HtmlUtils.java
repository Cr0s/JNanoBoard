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
package cr0s.nanoboard.html;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Collection of HTML utilities
 *
 * @author Cr0s
 */
public class HtmlUtils {

    private final static String boldRegexp = "\\*\\*(.+?)\\*\\*";
    private final static String italicRegexp = "\\*(.+?)\\*";
    private final static String quoteRegexp = "^(\\&gt\\;.+?)$";
    
    private static Pattern boldPattern, italicPattern, quotePattern;
    
    static {
        boldPattern = Pattern.compile(boldRegexp);
        italicPattern = Pattern.compile(italicRegexp);
        quotePattern = Pattern.compile(quoteRegexp, Pattern.MULTILINE);
    };

    public static String stripHtmlTags(String html) {
        return html.replaceAll("<br>", "\n").replaceAll("\\<", "&lt;").replaceAll("\\>", "&gt;");
    }

    /**
     * Replaces markdown characters to html code
     *
     * @param text input tet
     * @return html code
     *
     * TODO: make this work via Pattern/Matcher
     */
    public static String markdownToHtml(String text) {
        String result = "";
        
        result = replaceWakabamark(text);
        
        return result;
    }
    
    private static String replaceWakabamark(String text) {
        String result = "";

        // Bold
        Matcher matcher = boldPattern.matcher(text);
        result = matcher.replaceAll(getReplacement("bold"));
        
        // Italic
        matcher = italicPattern.matcher(result);
        result = matcher.replaceAll(getReplacement("italic"));
        
        // Quotes
        matcher = quotePattern.matcher(result);
        result = matcher.replaceAll(getReplacement("quote"));
        
        return result;    
    }
    
    // TODO: use HashMap
    private static String getReplacement(String type) {
        switch (type) {
            case "bold":
                return "<strong>$1</strong>";
            case "italic":
                return "<em>$1</em>";
            case "quote":
                return "<font color=\"green\">$1</font>\n";
        }
        
        return "";
    }
}
