/*
 * basicPanel.java
 *
 * Created on 08 November 2007, 14:05
 */

package topali.gui.dialog.jobs.tree.raxml;

/**
 *
 * @author  dlindn
 */
public class RaxmlBasicPanel extends javax.swing.JPanel {
    
    /** Creates new form basicPanel */
    public RaxmlBasicPanel() {
        initComponents();
    }
 

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        bOnemodel = new javax.swing.JRadioButton();
        bCodonpos = new javax.swing.JRadioButton();

        buttonGroup1.add(bOnemodel);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("topali/i18n/i18n"); // NOI18N
        bOnemodel.setText(bundle.getString("One_Model")); // NOI18N
        bOnemodel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        bOnemodel.setMargin(new java.awt.Insets(0, 0, 0, 0));

        buttonGroup1.add(bCodonpos);
        bCodonpos.setText(bundle.getString("Codon_position_Models")); // NOI18N
        bCodonpos.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        bCodonpos.setMargin(new java.awt.Insets(0, 0, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bOnemodel)
                    .addComponent(bCodonpos))
                .addContainerGap(265, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bOnemodel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bCodonpos)
                .addContainerGap(253, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JRadioButton bCodonpos;
    public javax.swing.JRadioButton bOnemodel;
    public javax.swing.ButtonGroup buttonGroup1;
    // End of variables declaration//GEN-END:variables
    
}
