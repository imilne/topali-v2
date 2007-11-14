/*
 * AdvancedPhyML.java
 *
 * Created on 07 September 2007, 08:23
 */

package topali.gui.dialog.jobs.tree.phyml;

import java.util.*;

import javax.swing.*;

import doe.MsgBox;

import topali.data.*;
import topali.data.models.*;
import topali.gui.Prefs;
import topali.var.Utils;

/**
 *
 * @author  dlindn
 */
public class AdvancedPhyML extends javax.swing.JPanel {
    
	SequenceSet ss;
	PhymlResult result;
	
	public boolean modelIsSupported = true;
	public boolean altModelFound = true;
	public String altModel = "";
	
    /** Creates new form AdvancedPhyML */
    public AdvancedPhyML(SequenceSet ss, TreeResult result) {
    	this.ss = ss;
		this.result = new PhymlResult();
        initComponents();
        setDefaults();
        
        if(result!=null)
        	initPrevResult((PhymlResult)result);
    }
    
    public void setDefaults() {
		SequenceSetParams params = ss.getParams();
		
		List<Model> mlist = ModelManager.getInstance().listPhymlModels(ss.isDNA());
		String[] models = new String[mlist.size()];
		for(int i=0; i<mlist.size(); i++)
				models[i] = mlist.get(i).getName();
		
		ComboBoxModel cm = new DefaultComboBoxModel(models);
		this.subModel.setModel(cm);
		
		Model m = params.getModel();
		if(Utils.indexof(models, m.getName())==-1) {
			if(ss.isDNA())
				m = ModelManager.getInstance().generateModel(Prefs.phyml_default_dnamodel, m.isGamma(), m.isInv());
			else
				m = ModelManager.getInstance().generateModel(Prefs.phyml_default_proteinmodel, m.isGamma(), m.isInv());
		}
		
		this.subModel.setSelectedItem(m.getName());
		
		this.gamma.setSelected(params.getModel().isGamma());
		this.inv.setSelected(params.getModel().isInv());
		
		this.optBranch.setSelected(Prefs.phyml_optbranch);
		this.optTop.setSelected(Prefs.phyml_opttop);
		
		SpinnerNumberModel mNBoot = new SpinnerNumberModel(Prefs.phyml_bootstrap, 0, 1000, 10);
		this.bootstraps.setModel(mNBoot);
	}
    
    private void initPrevResult(PhymlResult res) {
    	this.bootstraps.setValue(res.bootstrap);
    	this.optTop.setSelected(res.optTopology);
    	this.optBranch.setSelected(res.optBranchPara);
    }
    
    public PhymlResult onOK() {
		
		String name = (String)subModel.getSelectedItem();
		boolean g = gamma.isSelected();
		boolean i = inv.isSelected();
		
		ss.getParams().setModel(ModelManager.getInstance().generateModel(name, g, i));
		
		result.model = ss.getParams().getModel();
		result.bootstrap = (Integer)bootstraps.getValue();
		result.optTopology = optTop.isSelected();
		result.optBranchPara = optBranch.isSelected();
		
		Prefs.phyml_bootstrap = result.bootstrap;
		Prefs.phyml_optbranch = result.optBranchPara;
		Prefs.phyml_opttop = result.optTopology;
		
		return result;
	}
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        subModel = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        inv = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        gamma = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        optTop = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        optBranch = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        bootstraps = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Model parameters"));
        jLabel8.setText("Substitution Model:");

        subModel.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        subModel.setToolTipText("Substitution model to use");

        jLabel9.setText("Invariant Sites:");

        inv.setToolTipText("Allow invariant sites");
        inv.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        inv.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel10.setText("Gamma:");

        gamma.setToolTipText("Use gamma distribution");
        gamma.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        gamma.setMargin(new java.awt.Insets(0, 0, 0, 0));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inv)
                    .addComponent(subModel, 0, 98, Short.MAX_VALUE)
                    .addComponent(gamma))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(subModel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9)
                    .addComponent(inv))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(gamma))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("PhyML specific parameters"));
        jLabel4.setText("Optimize Topology:");

        optTop.setToolTipText("Optimize tree topology (forces branch length/rate parameters optimization)\n");
        optTop.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        optTop.setMargin(new java.awt.Insets(0, 0, 0, 0));
        optTop.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                optTopStateChanged(evt);
            }
        });

        jLabel5.setText("Optimize branch length/");

        optBranch.setToolTipText("Optimize branch lengths and rate parameters");
        optBranch.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        optBranch.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel6.setText("Bootstrap Runs: ");

        bootstraps.setToolTipText("Number of bootstrap runs");

        jLabel1.setText("rate parameters:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel1)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(optTop)
                    .addComponent(bootstraps, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                    .addComponent(optBranch))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(optTop)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addComponent(jLabel1))
                    .addComponent(optBranch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(bootstraps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void optTopStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_optTopStateChanged
        if(optTop.isSelected())
            optBranch.setSelected(true);
    }//GEN-LAST:event_optTopStateChanged
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner bootstraps;
    private javax.swing.JCheckBox gamma;
    private javax.swing.JCheckBox inv;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JCheckBox optBranch;
    private javax.swing.JCheckBox optTop;
    private javax.swing.JComboBox subModel;
    // End of variables declaration//GEN-END:variables
    
}
