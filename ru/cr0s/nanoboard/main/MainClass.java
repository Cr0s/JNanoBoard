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
package cr0s.nanoboard.main;

import cr0s.nanoboard.rules.RulesManager;
import cr0s.nanoboard.stegano.EncryptionProvider;
import cr0s.nanoboard.util.Base64;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main application class
 * @author Cr0s
 */
public class MainClass {
    public static final String VERSION = "3.0a";
    public static final String RULES_DIR = System.getProperty("user.dir") + System.getProperty("file.separator") + "rules";
    public static final String NANOPOSTS_DIR = System.getProperty("user.dir") + System.getProperty("file.separator") + "nanoposts";
    
    public static final String OUTBOX_DIR = System.getProperty("user.dir") + System.getProperty("file.separator") + "outbox";
    public static final String CONTAINERS_DIR = System.getProperty("user.dir") + System.getProperty("file.separator") + "containers";
    
    public static final String CONFIG_FILE = System.getProperty("user.dir") + System.getProperty("file.separator") + "JNanoBoard.properties";
    public static final String URLS_FILE = NANOPOSTS_DIR + System.getProperty("file.separator") + "urls.txt";
    
    public static NBFrame mainFrame;
    
    public static Properties config;
    public static void main(String[] args) {
        try {

                System.out.println("[ JNanoBoard version: " + VERSION + "]");
            
            RulesManager.getInstance().loadRules(RULES_DIR);      

            config = new Properties();
            try {
              config.load(new FileInputStream(CONFIG_FILE));
            } catch (IOException e) {
                (new File(CONFIG_FILE)).createNewFile();
                e.printStackTrace();
            }            
            
            setLookAndFeel();
            mainFrame = new NBFrame();
            mainFrame.setVisible(true);
            
        } catch (Exception ex) {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void setLookAndFeel() {
        try {
            OUTER:
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                //System.out.println(info.getName());
                switch (info.getName()) {
                    case "Windows":
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break OUTER;
                    case "GTK+":
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break OUTER;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
        }         
    }
}
