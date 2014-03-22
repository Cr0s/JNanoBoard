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
package cr0s.nanoboard.nanopost.tree;

import cr0s.nanoboard.main.NBFrame;
import cr0s.nanoboard.nanopost.NanoPost;
import java.util.ArrayList;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Helps to add NanoPost in JTree
 * @author Cr0s
 */
public class NanoPostTreeHelper {
    
    public static void addNanoPostsIntoTree(ArrayList<NanoPost> npList, JTree tree, NBFrame nbf) {
        // Add childs for nanoposts in list
        for (NanoPost np : npList) {
            np.getChilds().clear();
            
            for (NanoPost npp : npList) {
                if (np.isParentOf(npp)) {
                    np.addChild(npp);
                }
            }
        }
        
        // Add all OP posts and their child posts in tree
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)tree.getModel().getRoot();
        
        for (NanoPost np : npList) {
            DefaultMutableTreeNode npOpNode = new DefaultMutableTreeNode(new NanoPostTreeItem(np, nbf));
            
            if (np.isOpPost()) {
                addChildsAsNodes(np, npOpNode, nbf);
                
                rootNode.add(npOpNode);
            }
        }
    }
    
    public static void addChildsAsNodes(NanoPost np, DefaultMutableTreeNode parent, NBFrame nbf) {
        for (NanoPost child : np.getChilds()) {
            DefaultMutableTreeNode npChildNode = new DefaultMutableTreeNode(new NanoPostTreeItem(child, nbf));
            parent.add(npChildNode);
            addChildsAsNodes(child, npChildNode, nbf);
        }
    }
}
