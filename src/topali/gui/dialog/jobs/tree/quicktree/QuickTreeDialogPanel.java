/*
 * QuickTreeDialogPanel.java
 *
 * Created on 06 December 2007, 08:05
 */

package topali.gui.dialog.jobs.tree.quicktree;

import javax.swing.SpinnerNumberModel;

import topali.gui.Prefs;

/**
 *
 * @author  dlindn
 */
public class QuickTreeDialogPanel extends javax.swing.JPanel {
    
    /** Creates new form QuickTreeDialogPanel */
    public QuickTreeDialogPanel() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        modelpanel = new javax.swing.JPanel();
        tstvlabel = new javax.swing.JLabel();
        tstvlabel2 = new javax.swing.JLabel();
        alphalabel = new javax.swing.JLabel();
        alphalabel2 = new javax.swing.JLabel();
        tstv = new javax.swing.JSpinner();
        alpha = new javax.swing.JSpinner();
        estimate = new javax.swing.JCheckBox();
        tstvlabel3 = new javax.swing.JLabel();
        alphalabel3 = new javax.swing.JLabel();
        runpanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        bs = new javax.swing.JSpinner();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("topali/i18n/i18n"); // NOI18N
        modelpanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("Model_Settings"))); // NOI18N

        tstvlabel.setText(bundle.getString("Transition/Transversion_Ratio")); // NOI18N
        tstvlabel.setEnabled(false);

        tstvlabel2.setText(bundle.getString("ts/tv")); // NOI18N
        tstvlabel2.setEnabled(false);

        alphalabel.setText(bundle.getString("Rate_Heterogenity")); // NOI18N
        alphalabel.setEnabled(false);

        alphalabel2.setText(bundle.getString("alpha")); // NOI18N
        alphalabel2.setEnabled(false);

        tstv.setEnabled(false);
        SpinnerNumberModel mod0 = new SpinnerNumberModel(Prefs.qt_tstv, 0d, 100d, 0.1d);
        tstv.setModel(mod0);

        alpha.setEnabled(false);
        SpinnerNumberModel mod1 = new SpinnerNumberModel(Prefs.qt_alpha, 0.1d, 100d, 0.1d);
        alpha.setModel(mod1);

        estimate.setSelected(true);
        estimate.setText(bundle.getString("Estimate")); // NOI18N
        estimate.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        estimate.setMargin(new java.awt.Insets(0, 0, 0, 0));
        estimate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                estimateActionPerformed(evt);
            }
        });

        tstvlabel3.setText("="); // NOI18N
        tstvlabel3.setEnabled(false);

        alphalabel3.setText("="); // NOI18N
        alphalabel3.setEnabled(false);

        javax.swing.GroupLayout modelpanelLayout = new javax.swing.GroupLayout(modelpanel);
        modelpanel.setLayout(modelpanelLayout);
        modelpanelLayout.setHorizontalGroup(
            modelpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modelpanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(modelpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(estimate)
                    .addComponent(tstvlabel, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(alphalabel, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, modelpanelLayout.createSequentialGroup()
                        .addComponent(tstvlabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tstvlabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                        .addComponent(tstv, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, modelpanelLayout.createSequentialGroup()
                        .addComponent(alphalabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(alphalabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                        .addComponent(alpha, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        modelpanelLayout.setVerticalGroup(
            modelpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modelpanelLayout.createSequentialGroup()
                .addComponent(estimate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tstvlabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(modelpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tstvlabel2)
                    .addComponent(tstvlabel3)
                    .addComponent(tstv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alphalabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(modelpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(alphalabel2)
                    .addComponent(alphalabel3)
                    .addComponent(alpha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        runpanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("Program_settings"))); // NOI18N

        jLabel5.setText(bundle.getString("Bootstrap_Runs")); // NOI18N

        jLabel6.setText(bundle.getString("Bootstraps")); // NOI18N

        SpinnerNumberModel mod2 = new SpinnerNumberModel(Prefs.qt_bootstrap, 0, 1000, 10);
        bs.setModel(mod2);

        javax.swing.GroupLayout runpanelLayout = new javax.swing.GroupLayout(runpanel);
        runpanel.setLayout(runpanelLayout);
        runpanelLayout.setHorizontalGroup(
            runpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(runpanelLayout.createSequentialGroup()
                .addGroup(runpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(runpanelLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                        .addComponent(bs, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        runpanelLayout.setVerticalGroup(
            runpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(runpanelLayout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(runpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(bs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(modelpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(runpanel, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(modelpanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(runpanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void estimateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_estimateActionPerformed
        if(estimate.isSelected()) {
            tstvlabel.setEnabled(false);
            tstvlabel2.setEnabled(false);
            tstvlabel3.setEnabled(false);
            tstv.setEnabled(false);
            alphalabel.setEnabled(false);
            alphalabel2.setEnabled(false);
            alphalabel3.setEnabled(false);
            alpha.setEnabled(false);
        }
        else {
            tstvlabel.setEnabled(true);
            tstvlabel2.setEnabled(true);
            tstvlabel3.setEnabled(true);
            tstv.setEnabled(true);
            alphalabel.setEnabled(true);
            alphalabel2.setEnabled(true);
            alphalabel3.setEnabled(true);
            alpha.setEnabled(true);
        }
    }//GEN-LAST:event_estimateActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JSpinner alpha;
    public javax.swing.JLabel alphalabel;
    public javax.swing.JLabel alphalabel2;
    public javax.swing.JLabel alphalabel3;
    public javax.swing.JSpinner bs;
    public javax.swing.JCheckBox estimate;
    public javax.swing.JLabel jLabel5;
    public javax.swing.JLabel jLabel6;
    public javax.swing.JPanel modelpanel;
    public javax.swing.JPanel runpanel;
    public javax.swing.JSpinner tstv;
    public javax.swing.JLabel tstvlabel;
    public javax.swing.JLabel tstvlabel2;
    public javax.swing.JLabel tstvlabel3;
    // End of variables declaration//GEN-END:variables
    
}
