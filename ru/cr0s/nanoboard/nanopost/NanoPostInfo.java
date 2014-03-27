/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cr0s.nanoboard.nanopost;

/**
 * NanoPost's information container class
 * Will be serialized/deserialized via Google JSON (Gson)
 * @author user
 */
public class NanoPostInfo {
    
    public NanoPostInfo(String text, int timestamp) {
        this.postText = text;
        this.postTimestamp = timestamp;
    }
    
    public String postText;
    public int postTimestamp;
}
