/*
 * OmegaValuesDialog.java
 *
 * Created on 07 September 2007, 10:46
 */

package topali.gui.dialog.jobs.cml;

import java.util.*;
import javax.swing.*;
import topali.data.CMLModel;
import topali.var.utils.Utils;

/**
 *
 * @author  dlindn
 */
public class OmegaValuesDialog extends javax.swing.JDialog {
    
    CMLModel model;
    DefaultListModel listmodel = new DefaultListModel();
    
    /** Creates new form OmegaValuesDialog */
    public OmegaValuesDialog(JDialog parent, boolean modal, CMLModel model) {
        super(parent, modal);
        this.model = model;
        initComponents();
        setDefaults();
        
        getRootPane().setDefaultButton(ok);
		Utils.addCloseHandler(this, cancel);
		
		setLocationRelativeTo(parent);
		setResizable(false);
    }
    
    public CMLModel getModel() {
        return model;
    }
    
    private void setDefaults() {
        for(int i=0; i<model.wStart.size(); i++)
            listmodel.addElement(model.wStart.get(i));
        values.setModel(listmodel);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        values = new javax.swing.JList();
        add = new javax.swing.JButton();
        remove = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        ok = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Modify omega start values");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Omega start values:"));

        values.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "5.0" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        values.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                valuesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(values);

        add.setText("Add");
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });

        remove.setText("Remove");
        remove.setEnabled(false);
        remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(remove)
                    .add(add, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                .add(10, 10, 10))
        );

        jPanel1Layout.linkSize(new java.awt.Component[] {add, remove}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(add)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(remove))
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                .addContainerGap())
        );

        cancel.setText("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        ok.setText("Ok");
        ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(ok)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cancel)))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {cancel, ok}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancel)
                    .add(ok))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void removeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeActionPerformed
        int index = values.getSelectedIndex();
        listmodel.remove(index);
        if(listmodel.getSize()>0) {
            if(index==listmodel.getSize())
                index--;
            values.setSelectedIndex(index);
        }
        else
        	ok.setEnabled(false);
        
    }//GEN-LAST:event_removeActionPerformed

    private void valuesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_valuesValueChanged
        if(values.getSelectedIndex()>-1)
            remove.setEnabled(true);
        else
            remove.setEnabled(false);
    }//GEN-LAST:event_valuesValueChanged

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
        JSpinner spin = new JSpinner(new SpinnerNumberModel(5.0d, 0.1d, 10.0d, 0.1d));
        int i = JOptionPane.showConfirmDialog(this, spin, "Enter new value", JOptionPane.OK_CANCEL_OPTION);
        if(i==JOptionPane.OK_OPTION) {
            Double d = (Double)spin.getValue();
            if(!listmodel.contains(d)) {
            	double[] tmp = new double[listmodel.getSize()+1];
            	for(int j=0; j<listmodel.getSize(); j++) 
            		tmp[j] = (Double)listmodel.get(j);
            	tmp[tmp.length-1] = d;
            	Arrays.sort(tmp);
            	listmodel.removeAllElements();
            	for(int j=0; j<tmp.length; j++)
            		listmodel.addElement(tmp[j]);
            	ok.setEnabled(true);
            }
        }
    }//GEN-LAST:event_addActionPerformed

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        model = null;
        this.setVisible(false);
    }//GEN-LAST:event_cancelActionPerformed

    private void okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okActionPerformed
        model.wStart = new Vector<Double>();
        for(int i=0; i<values.getModel().getSize(); i++) 
            model.wStart.add((Double)values.getModel().getElementAt(i));
        this.setVisible(false);
    }//GEN-LAST:event_okActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add;
    private javax.swing.JButton cancel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton ok;
    private javax.swing.JButton remove;
    private javax.swing.JList values;
    // End of variables declaration//GEN-END:variables
    
}
