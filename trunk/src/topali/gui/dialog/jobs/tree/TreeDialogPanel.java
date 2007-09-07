/*
 * TreeDialogPanel.java
 *
 * Created on 07 September 2007, 08:13
 */

package topali.gui.dialog.jobs.tree;

import topali.data.SequenceSet;
import topali.gui.Prefs;

/**
 *
 * @author  dlindn
 */
public class TreeDialogPanel extends javax.swing.JPanel {
    
	CreateTreeDialog parent;
	SequenceSet ss;
	
	
    /** Creates new form TreeDialogPanel */
    public TreeDialogPanel(CreateTreeDialog parent, SequenceSet ss) {
    	this.parent = parent;
    	this.ss = ss;
        initComponents();
        setDefaults();
    }
    
    public void setDefaults() {
    	if(ss.getParams().isDNA()) {
    		f84.setText("F84+Gamma/neighbor joining");
    	}
    	else {
    		f84.setText("WAG+Gamma/neighbor joining");
    		bayescdna.setEnabled(false);
    	}
    	
    	if(Prefs.gui_tree_method==0) {
    		f84.setSelected(true);
    	}
    	else if(Prefs.gui_tree_method==1){
    		phyml.setSelected(true);
    	}
    	else if(Prefs.gui_tree_method==2){
    		bayes.setSelected(true);
    	}
    	else if(Prefs.gui_tree_method==3){
    		bayescdna.setSelected(true);
    	}
    	
    	if(Prefs.gui_tree_useall)
    		allseq.setSelected(true);
    	else
    		selseq.setSelected(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        f84 = new javax.swing.JRadioButton();
        phyml = new javax.swing.JRadioButton();
        bayes = new javax.swing.JRadioButton();
        bayescdna = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        allseq = new javax.swing.JRadioButton();
        selseq = new javax.swing.JRadioButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Tree creation method"));
        buttonGroup1.add(f84);
        f84.setText("F84+Gamma/Neighbor joining");
        f84.setToolTipText("Fast, approximate method with fixed ts/tv and alpha parameters");
        f84.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        f84.setMargin(new java.awt.Insets(0, 0, 0, 0));
        f84.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                f84StateChanged(evt);
            }
        });

        buttonGroup1.add(phyml);
        phyml.setText("Maximum Likelihood (using PhyML)");
        phyml.setToolTipText("Medium, accurate method");
        phyml.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        phyml.setMargin(new java.awt.Insets(0, 0, 0, 0));
        phyml.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                phymlStateChanged(evt);
            }
        });

        buttonGroup1.add(bayes);
        bayes.setText("Bayesian phylogenetic analysis (using MrBayes)");
        bayes.setToolTipText("Slow, sophisticated method");
        bayes.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        bayes.setMargin(new java.awt.Insets(0, 0, 0, 0));
        bayes.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                bayesStateChanged(evt);
            }
        });

        buttonGroup1.add(bayescdna);
        bayescdna.setText("cDNA bayesian phylogenetic analysis (using MrBayes)");
        bayescdna.setToolTipText("Slow, sophisticated method for cDNA analysis");
        bayescdna.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        bayescdna.setMargin(new java.awt.Insets(0, 0, 0, 0));
        bayescdna.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                bayescdnaStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(f84)
                    .addComponent(phyml)
                    .addComponent(bayes)
                    .addComponent(bayescdna))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(f84)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(phyml)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bayes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bayescdna)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Sequence selection"));
        buttonGroup2.add(allseq);
        allseq.setText("Use all sequences in the alignment");
        allseq.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        allseq.setMargin(new java.awt.Insets(0, 0, 0, 0));
        allseq.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                allseqStateChanged(evt);
            }
        });

        buttonGroup2.add(selseq);
        selseq.setText("Use currently selected sequences only");
        selseq.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        selseq.setMargin(new java.awt.Insets(0, 0, 0, 0));
        selseq.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                selseqStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(allseq)
                    .addComponent(selseq))
                .addContainerGap(84, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(allseq)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selseq)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void selseqStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_selseqStateChanged
    	if(selseq.isSelected()) {
    		Prefs.gui_tree_useall = false;
    	}
    }//GEN-LAST:event_selseqStateChanged

    private void allseqStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_allseqStateChanged
    	if(allseq.isSelected()) {
    		Prefs.gui_tree_useall = true;
    	}
    }//GEN-LAST:event_allseqStateChanged

    private void bayescdnaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_bayescdnaStateChanged
    	if(bayescdna.isSelected()) {
    		Prefs.gui_tree_method = 3;
    		parent.switchMethod();
    	}
    }//GEN-LAST:event_bayescdnaStateChanged

    private void bayesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_bayesStateChanged
    	if(bayes.isSelected()) {
    		Prefs.gui_tree_method = 2;
    		parent.switchMethod();
    	}
    }//GEN-LAST:event_bayesStateChanged

    private void phymlStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_phymlStateChanged
    	if(phyml.isSelected()) {
    		Prefs.gui_tree_method = 1;
    		parent.switchMethod();
    	}
    }//GEN-LAST:event_phymlStateChanged

    private void f84StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_f84StateChanged
    	if(f84.isSelected()) {
    		Prefs.gui_tree_method = 0;
    		parent.switchMethod();
    	}
    }//GEN-LAST:event_f84StateChanged
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton allseq;
    private javax.swing.JRadioButton bayes;
    private javax.swing.JRadioButton bayescdna;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JRadioButton f84;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton phyml;
    private javax.swing.JRadioButton selseq;
    // End of variables declaration//GEN-END:variables
    
}
