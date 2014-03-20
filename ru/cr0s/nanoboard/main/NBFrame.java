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

import cr0s.nanoboard.image.ImageUtils;
import cr0s.nanoboard.main.workers.WorkerExecuteRule;
import cr0s.nanoboard.main.workers.WorkerSyncImage;
import cr0s.nanoboard.nanopost.MalformedNanoPostException;
import cr0s.nanoboard.nanopost.NanoPost;
import cr0s.nanoboard.nanopost.NanoPostFactory;
import cr0s.nanoboard.rules.Rule;
import cr0s.nanoboard.rules.RulesManager;
import cr0s.nanoboard.stegano.EncryptionProvider;
import cr0s.nanoboard.util.ByteUtils;
import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.filechooser.*;

/**
 * Main program window
 * @author Cr0s
 */
public class NBFrame extends javax.swing.JFrame {

    public ArrayList<WorkerSyncImage> syncWorkers;
    
    /**
     * Creates new form NBFrame
     */
    public NBFrame() {
        ruleDialog = new RuleDialog(this, true);
        
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fcAttach = new javax.swing.JFileChooser();
        fcContainer = new javax.swing.JFileChooser();
        tabs = new javax.swing.JTabbedPane();
        panelRefresh = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        edBoardCode = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableRules = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        btnAddRule = new javax.swing.JButton();
        btnDeleteRule = new javax.swing.JButton();
        btnEditRule = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnStart = new javax.swing.JButton();
        panelSynch = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableSync = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        panPostText = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtPostText = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        edAttachFile = new javax.swing.JTextField();
        btnSelectFile = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        edContainerFile = new javax.swing.JTextField();
        btnSelectContainerFile = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        panelRefresh.setName("panelRefresh"); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Board code"));

        jLabel2.setText("Specify the security board code (like password) to get access to specified (password-dependent) board of NanoPosts");

        edBoardCode.setName("textKey"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(edBoardCode))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(edBoardCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setText("Refresh (sync) local NanoBoard database by searching and downloading NanoPosts from known resources.");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Download rules"));

        tableRules.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Name", "Page URL", "RegExpr"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableRules.setName("tableRules"); // NOI18N
        tableRules.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableRules.getTableHeader().setReorderingAllowed(false);
        tableRules.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableRulesMouseClicked(evt);
            }
        });
        tableRules.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                tableRulesInputMethodTextChanged(evt);
            }
        });
        jScrollPane1.setViewportView(tableRules);

        jLabel3.setText("Define rules (regular expressions) to extract .PNG files with NanoPosts and URLs of Web-pages, where NanoBoard can find NanoPost's files.");
        jLabel3.setToolTipText("");

        btnAddRule.setText("Add rule");
        btnAddRule.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddRuleMouseClicked(evt);
            }
        });
        btnAddRule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddRuleActionPerformed(evt);
            }
        });

        btnDeleteRule.setText("Delete rule");

        btnEditRule.setText("Edit Rule");
        btnEditRule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditRuleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(btnAddRule)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDeleteRule)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEditRule)))
                        .addGap(0, 100, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddRule)
                    .addComponent(btnDeleteRule)
                    .addComponent(btnEditRule))
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnStart.setText("START SYNC >>>");
        btnStart.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnStart, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelRefreshLayout = new javax.swing.GroupLayout(panelRefresh);
        panelRefresh.setLayout(panelRefreshLayout);
        panelRefreshLayout.setHorizontalGroup(
            panelRefreshLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRefreshLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRefreshLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelRefreshLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelRefreshLayout.setVerticalGroup(
            panelRefreshLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRefreshLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tabs.addTab("Main", panelRefresh);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Downloading and checking progress"));

        tableSync.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Rule name", "Image URL", "Status", "Progress"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tableSync);
        tableSync.getColumnModel().getColumn(2).setResizable(false);
        tableSync.getColumnModel().getColumn(2).setPreferredWidth(10);
        tableSync.getColumnModel().getColumn(3).setResizable(false);
        tableSync.getColumnModel().getColumn(3).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                if (table.getValueAt(row, 3) == null) {
                    table.setValueAt(new JProgressBar(), row, 3);  
                } 

                if (table.getValueAt(row, 3) instanceof Component) {
                    return (Component)table.getValueAt(row, 3);
                } else {
                    return null;
                }
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 794, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelSynchLayout = new javax.swing.GroupLayout(panelSynch);
        panelSynch.setLayout(panelSynchLayout);
        panelSynchLayout.setHorizontalGroup(
            panelSynchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSynchLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelSynchLayout.setVerticalGroup(
            panelSynchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSynchLayout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 36, Short.MAX_VALUE))
        );

        tabs.addTab("NanoPosts sync", panelSynch);

        panPostText.setBorder(javax.swing.BorderFactory.createTitledBorder("Post text"));

        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        txtPostText.setColumns(20);
        txtPostText.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPostText.setRows(5);
        jScrollPane3.setViewportView(txtPostText);

        javax.swing.GroupLayout panPostTextLayout = new javax.swing.GroupLayout(panPostText);
        panPostText.setLayout(panPostTextLayout);
        panPostTextLayout.setHorizontalGroup(
            panPostTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
        );
        panPostTextLayout.setVerticalGroup(
            panPostTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Post attach"));

        jLabel4.setText("File name:");

        edAttachFile.setEditable(false);
        edAttachFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edAttachFileActionPerformed(evt);
            }
        });

        btnSelectFile.setText("Open...");
        btnSelectFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectFileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(edAttachFile, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSelectFile, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edAttachFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelectFile)))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("NanoPost container PNG"));

        jLabel5.setText("File name:");

        edContainerFile.setEditable(false);
        edContainerFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edContainerFileActionPerformed(evt);
            }
        });

        btnSelectContainerFile.setText("Open...");
        btnSelectContainerFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectContainerFileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(edContainerFile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSelectContainerFile, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edContainerFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelectContainerFile)))
        );

        jButton1.setText("CREATE");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panPostText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(274, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(250, 250, 250))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panPostText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabs.addTab("Create NanoPost", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        this.setTitle("JNanoBoard version: " + MainClass.VERSION);
        evt.getWindow().setLocationRelativeTo(evt.getOppositeWindow());
        
        this.edBoardCode.setText(MainClass.config.getProperty("boardKey", ""));
        addRulesInTable();
        
        fcContainer.addChoosableFileFilter(new FileNameExtensionFilter("PNG files", "png"));
    }//GEN-LAST:event_formWindowOpened

    private void btnAddRuleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddRuleActionPerformed
        this.ruleDialog.setParent(this);
        this.ruleDialog.setIsAdding(true);
        
        this.ruleDialog.setVisible(true);
    }//GEN-LAST:event_btnAddRuleActionPerformed

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        tabs.setSelectedIndex(1);

        DefaultTableModel model = (DefaultTableModel) tableSync.getModel();
        int rows = model.getRowCount(); 
        for(int i = rows - 1; i >= 0; i--)
        {
           model.removeRow(i); 
        }        
        
        for (Rule r : RulesManager.getInstance().getRulesList()) {
            if (r.isIsEnabled()) {
                WorkerExecuteRule wer = new WorkerExecuteRule(this, r);
                wer.execute();
            }
        }        
    }//GEN-LAST:event_btnStartActionPerformed

    private void btnAddRuleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddRuleMouseClicked

    }//GEN-LAST:event_btnAddRuleMouseClicked

    private void tableRulesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableRulesMouseClicked
        if (tableRules.getSelectedRow() == -1) {
            return;
        }
        
        Rule rule = RulesManager.getInstance().getRuleByName((String)tableRules.getModel().getValueAt(tableRules.getSelectedRow(), 1));
        
        if (rule == null) {
            return;
        }
        
        rule.setIsEnabled((Boolean)tableRules.getModel().getValueAt(tableRules.getSelectedRow(), 0));
        
        RulesManager.getInstance().saveRules(MainClass.RULES_DIR);
    }//GEN-LAST:event_tableRulesMouseClicked

    private void btnEditRuleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditRuleActionPerformed
        if (tableRules.getSelectedRow() == -1) {
            return;
        }
        
        Rule rule = RulesManager.getInstance().getRuleByName((String)tableRules.getModel().getValueAt(tableRules.getSelectedRow(), 1));
        
        if (rule == null) {
            return;
        }
        
        this.ruleDialog.setRule(rule);
        this.ruleDialog.setParent(this);
        this.ruleDialog.setIsAdding(false);

        this.ruleDialog.setVisible(true);    
    }//GEN-LAST:event_btnEditRuleActionPerformed

    private void tableRulesInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_tableRulesInputMethodTextChanged
        /*Rule rule = RulesManager.getInstance().getRuleByName((String)tableRules.getModel().getValueAt(tableRules.getSelectedRow(), 1));
        
        if (rule == null) {
            return;
        }
        
        switch (tableRules.getSelectedColumn()) {
            case 1:
                rule.setRuleName(evt.paramString());
                break;
                
            case 2:
                rule.setRuleURL(evt.paramString());
                break;
                
            case 3:
                rule.setRuleRegExpr(evt.paramString());
        }
        
        RulesManager.getInstance().saveRules(MainClass.RULES_DIR);*/
    }//GEN-LAST:event_tableRulesInputMethodTextChanged

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        RulesManager.getInstance().saveRules(MainClass.RULES_DIR);
        MainClass.config.setProperty("boardKey", this.edBoardCode.getText());
        
        try {
            MainClass.config.store(new FileOutputStream(MainClass.CONFIG_FILE), "JNanoBoard config file");
        } catch (IOException ex) {
            Logger.getLogger(NBFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowClosed

    private void edAttachFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edAttachFileActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edAttachFileActionPerformed

    private void edContainerFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edContainerFileActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edContainerFileActionPerformed

    private void btnSelectFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectFileActionPerformed
        if (fcAttach.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            edAttachFile.setText(fcAttach.getSelectedFile().toString());
        }
    }//GEN-LAST:event_btnSelectFileActionPerformed

    private void btnSelectContainerFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectContainerFileActionPerformed
        fcContainer.setAcceptAllFileFilterUsed(false);
        
        if (fcContainer.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            edContainerFile.setText(fcContainer.getSelectedFile().toString());
        }        
    }//GEN-LAST:event_btnSelectContainerFileActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        NanoPost np;
        
        if (!edAttachFile.getText().isEmpty()) {
            File attachFile = new File(edAttachFile.getText());
            if (!attachFile.exists()) {
                JOptionPane.showMessageDialog(this, "Selected attach file does not exist", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            np = NanoPostFactory.createNanoPost(txtPostText.getText(), EncryptionProvider.EMPTY_HASH, attachFile);
        } else {
            np = NanoPostFactory.createNanoPost(txtPostText.getText(), EncryptionProvider.EMPTY_HASH, null);
        }
        
        File containerFile = new File(edContainerFile.getText());
        if (!containerFile.exists()) {
            JOptionPane.showMessageDialog(this, "Selected container file does not exists.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        ImageUtils.encodeIntoImage(containerFile, np.getAsBytes(), edBoardCode.getText());
        
        try {
            byte[] dataBytes = ImageUtils.tryToDecodeSteganoImage(ByteUtils.readBytesFromFile(new File(containerFile.toString() + ".nanopost.png")), edBoardCode.getText());
            try {
                NanoPost nanoPost = NanoPostFactory.getNanoPostFromBytes(dataBytes);
                nanoPost.setSourceImageData(ByteUtils.readBytesFromFile(new File(containerFile.toString() + ".nanopost.png")));
                nanoPost.saveToFile();
            } catch (MalformedNanoPostException ex) {
                Logger.getLogger(NBFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(NBFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed
    
    public void addRulesInTable() {
        try {
            RulesManager.getInstance().loadRules(MainClass.RULES_DIR);
        } catch (Exception ex) {
            Logger.getLogger(NBFrame.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        DefaultTableModel model = (DefaultTableModel) tableRules.getModel();
        int rows = model.getRowCount(); 
        for(int i = rows - 1; i >= 0; i--)
        {
           model.removeRow(i); 
        }
        
        for (Rule r : RulesManager.getInstance().getRulesList()) {
            System.out.println(r.getRuleName());
            model.addRow(new Object[] { (Boolean)r.isIsEnabled(), (String)r.getRuleName(), (String)r.getRuleURL(), (String)r.getRuleRegExpr() });
        }
    }
    
    public void setSyncTableRow(int rowIndex, String status, int progressValue, int maxProgressValue) {
        tableSync.getModel().setValueAt(status, rowIndex, 2);
        
        Component c = (Component) tableSync.getValueAt(rowIndex, 3);

        if (c instanceof JProgressBar) {
            ((JProgressBar) c).setMaximum(maxProgressValue);
            ((JProgressBar) c).setValue(progressValue);
        }
        
        tableSync.repaint();
    }
    
    public synchronized void createWorkerByRuleMatch(Rule rule, String imageUrl) {
        ((DefaultTableModel) tableSync.getModel()).addRow(new Object[] { rule.getRuleName(), imageUrl, "-", new JProgressBar() });
        WorkerSyncImage wsi = new WorkerSyncImage(this, edBoardCode.getText(), rule, imageUrl, tableSync.getModel().getRowCount() - 1);
        //syncWorkers.add(wsi);
        wsi.execute();
    }
    
    public RuleDialog ruleDialog;
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddRule;
    private javax.swing.JButton btnDeleteRule;
    private javax.swing.JButton btnEditRule;
    private javax.swing.JButton btnSelectContainerFile;
    private javax.swing.JButton btnSelectFile;
    private javax.swing.JButton btnStart;
    private javax.swing.JTextField edAttachFile;
    private javax.swing.JTextField edBoardCode;
    private javax.swing.JTextField edContainerFile;
    private javax.swing.JFileChooser fcAttach;
    private javax.swing.JFileChooser fcContainer;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPanel panPostText;
    private javax.swing.JPanel panelRefresh;
    private javax.swing.JPanel panelSynch;
    private javax.swing.JTable tableRules;
    private javax.swing.JTable tableSync;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTextArea txtPostText;
    // End of variables declaration//GEN-END:variables
}
