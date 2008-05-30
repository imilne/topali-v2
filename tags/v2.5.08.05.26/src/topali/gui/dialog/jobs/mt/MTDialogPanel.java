/*
 * MTDialogPanel.java
 *
 * Created on 15 October 2007, 14:51
 */

package topali.gui.dialog.jobs.mt;

import java.util.*;

import javax.swing.DefaultComboBoxModel;

import topali.data.*;
import topali.data.models.*;
import topali.i18n.Text;

/**
 *
 * @author  dlindn
 */
public class MTDialogPanel extends javax.swing.JPanel {
    
	ModelTestResult res;
	boolean dna;
	
    /** Creates new form MTDialogPanel */
    public MTDialogPanel(ModelTestResult res, boolean dna) {
    	this.res = (res==null) ? new ModelTestResult() : res;
    	this.dna = dna;
        initComponents();
        
        DefaultComboBoxModel mod = new DefaultComboBoxModel(new String[] {ModelTestResult.TYPE_PHYML, ModelTestResult.TYPE_MRBAYES});
        models.setModel(mod);
        
        DefaultComboBoxModel mod2 = new DefaultComboBoxModel(new String[] {ModelTestResult.SAMPLE_SEQLENGTH, ModelTestResult.SAMPLE_ALGNSIZE});
        sampleSize.setModel(mod2);
        
        models.setSelectedItem(Prefs.ms_models);
    	gamma.setSelected(Prefs.ms_gamma);
    	inv.setSelected(Prefs.ms_inv);
    	sampleSize.setSelectedItem(Prefs.ms_samplesize);
    	
    	if(res!=null)
    		initPrevResult(res);
    }
    
    private void initPrevResult(ModelTestResult res) {
    	this.models.setSelectedItem(res.type);
    	boolean gamma = false;
    	boolean inv = false;
    	for(Model mod : res.models) {
    	    if(mod.isGamma())
    		gamma = true;
    	    if(mod.isInv())
    		inv = true;
    	}
    	this.gamma.setSelected(gamma);
    	this.inv.setSelected(inv);
    	this.sampleSize.setSelectedItem(res.sampleCrit);
    }
    
    public ModelTestResult getResult() {
    	
    	List<Model> availModels = null;
		if(this.models.getSelectedItem().equals(ModelTestResult.TYPE_PHYML)) {
			availModels = ModelManager.getInstance().listPhymlModels(dna);
		}
		else if(this.models.getSelectedItem().equals(ModelTestResult.TYPE_MRBAYES)) { 
			availModels = ModelManager.getInstance().listMrBayesModels(dna);
		}
		ArrayList<Model> models = new ArrayList<Model>();
		for(Model m : availModels) {
				Model m1 = ModelManager.getInstance().generateModel(m.getName(), false, false);
				models.add(m1);
				if(gamma.isSelected()) {
					Model m2 = ModelManager.getInstance().generateModel(m.getName(), true, false);
					models.add(m2);
				}
				if(inv.isSelected()) {
					Model m3 = ModelManager.getInstance().generateModel(m.getName(), false, true);
					models.add(m3);
				}
				if(gamma.isSelected()&&inv.isSelected()) {
					Model m4 = ModelManager.getInstance().generateModel(m.getName(), true, true);
					models.add(m4);
				}
		}
		res.models = models;
		res.type = (String)this.models.getSelectedItem();
		res.sampleCrit = (String)this.sampleSize.getSelectedItem();
		
		Prefs.ms_gamma = gamma.isSelected();
		Prefs.ms_inv = inv.isSelected();
		Prefs.ms_models = (String)this.models.getSelectedItem();
		Prefs.ms_samplesize = (String)sampleSize.getSelectedItem();
    	return this.res;
    }
    
    public void setDefaults() {
    	models.setSelectedIndex(0);
    	gamma.setSelected(true);
    	inv.setSelected(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        models = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        gamma = new javax.swing.JCheckBox();
        inv = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        sampleSize = new javax.swing.JComboBox();

        jLabel1.setText(Text.get("Models"));

        models.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PhyML", "MrBayes" }));

        jLabel2.setText(Text.get("Gamma"));

        jLabel3.setText(Text.get("Invariant_Sites"));

        gamma.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        gamma.setMargin(new java.awt.Insets(0, 0, 0, 0));

        inv.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        inv.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel4.setText(Text.get("AIC2/BIC_Calculation"));

        jLabel5.setText(Text.get("Sample_Size"));

        sampleSize.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sequence Length", "Alignment Size" }));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel4)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel1)
                            .add(jLabel2)
                            .add(jLabel3)
                            .add(jLabel5))
                        .add(47, 47, 47)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(sampleSize, 0, 182, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, inv)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, gamma)
                            .add(models, 0, 182, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(models, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jLabel2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel3)
                        .add(20, 20, 20)
                        .add(jLabel4))
                    .add(layout.createSequentialGroup()
                        .add(gamma)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(inv)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(sampleSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox gamma;
    private javax.swing.JCheckBox inv;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JComboBox models;
    private javax.swing.JComboBox sampleSize;
    // End of variables declaration//GEN-END:variables
    
}