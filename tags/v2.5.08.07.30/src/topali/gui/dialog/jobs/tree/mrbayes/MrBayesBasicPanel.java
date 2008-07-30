/*
 * MrBayesBasicPanel.java
 *
 * Created on 07 November 2007, 12:39
 */

package topali.gui.dialog.jobs.tree.mrbayes;

import topali.i18n.Text;

/**
 *
 * @author  dlindn
 */
public class MrBayesBasicPanel extends javax.swing.JPanel {
    
    /** Creates new form MrBayesBasicPanel */
    public MrBayesBasicPanel() {
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
        bOnemodel.setMnemonic('o');
        bOnemodel.setText(Text.get("One_Model"));
        bOnemodel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        bOnemodel.setMargin(new java.awt.Insets(0, 0, 0, 0));

        buttonGroup1.add(bCodonpos);
        bCodonpos.setMnemonic('c');
        bCodonpos.setText(Text.get("Codon_position_Models"));
        bCodonpos.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        bCodonpos.setMargin(new java.awt.Insets(0, 0, 0, 0));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(bOnemodel)
                    .add(bCodonpos))
                .addContainerGap(149, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(bOnemodel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(bCodonpos)
                .addContainerGap(47, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JRadioButton bCodonpos;
    public javax.swing.JRadioButton bOnemodel;
    public javax.swing.ButtonGroup buttonGroup1;
    // End of variables declaration//GEN-END:variables
    
}
