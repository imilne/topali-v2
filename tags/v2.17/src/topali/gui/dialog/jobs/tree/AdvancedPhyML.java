/*
 * AdvancedPhyML.java
 *
 * Created on 07 September 2007, 08:23
 */

package topali.gui.dialog.jobs.tree;

import javax.swing.*;

import topali.data.*;

/**
 *
 * @author  dlindn
 */
public class AdvancedPhyML extends javax.swing.JPanel {
    
	SequenceSet ss;
	PhymlResult result;
	
    /** Creates new form AdvancedPhyML */
    public AdvancedPhyML(SequenceSet ss, PhymlResult result) {
    	this.ss = ss;
		this.result = result;
        initComponents();
        setDefaults();
        
        if(result!=null)
        	initPrevResult(result);
    }
    
    private void setDefaults() {
		SequenceSetParams params = ss.getParams();
		
		ComboBoxModel cm = new DefaultComboBoxModel(SequenceSetParams.availCodes);
		this.genCode.setModel(cm);
		String c = params.getGeneticCode();
		for(int i=0; i<SequenceSetParams.availCodes.length; i++)
			if(SequenceSetParams.availCodes[i].equals(c)) {
				this.genCode.setSelectedIndex(i);
				break;
			}
		
		String[] models = ss.isDNA() ?  SequenceSetParams.availDNAModels : SequenceSetParams.availAAModels;
		cm = new DefaultComboBoxModel(models);
		this.subModel.setModel(cm);
		String m = params.getModel();
		for(int i=0; i<models.length; i++)
			if(models[i].equals(m)) {
				this.subModel.setSelectedIndex(i);
				break;
			}
		
		
		this.gamma.setSelected(params.isModelGamma());
		this.inv.setSelected(params.isModelInv());
		
		this.optBranch.setSelected(result.optBranchPara);
		this.optTop.setSelected(result.optTopology);
		
		SpinnerNumberModel mNBoot = new SpinnerNumberModel(result.bootstrap, 0, 1000, 10);
		this.bootstraps.setModel(mNBoot);
	}
    
    private void initPrevResult(PhymlResult res) {
    	this.bootstraps.setValue(res.bootstrap);
    	this.optTop.setSelected(res.optTopology);
    	this.optBranch.setSelected(res.optBranchPara);
    }
    
    public void onOK() {
		ss.getParams().setModel((String)subModel.getSelectedItem());
		ss.getParams().setModelGamma(gamma.isSelected());
		ss.getParams().setModelInv(inv.isSelected());
		
		result.bootstrap = (Integer)bootstraps.getValue();
		result.optTopology = optTop.isSelected();
		result.optBranchPara = optBranch.isSelected();
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
        jLabel11 = new javax.swing.JLabel();
        genCode = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        optTop = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        optBranch = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        bootstraps = new javax.swing.JSpinner();

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

        jLabel11.setText("Genetic code:");

        genCode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        genCode.setToolTipText("Genetic code");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(gamma)
                    .addComponent(inv)
                    .addComponent(genCode, 0, 182, Short.MAX_VALUE)
                    .addComponent(subModel, 0, 182, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(genCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(subModel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(inv)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(gamma)))
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

        jLabel5.setText("Optimize branch length/rate par. :");

        optBranch.setToolTipText("Optimize branch lengths and rate parameters");
        optBranch.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        optBranch.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel6.setText("Bootstrap Runs: ");

        bootstraps.setToolTipText("Number of bootstrap runs");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(optTop)
                    .addComponent(bootstraps, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                    .addComponent(optBranch))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(optTop, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(optBranch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addComponent(bootstraps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
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
    private javax.swing.JComboBox genCode;
    private javax.swing.JCheckBox inv;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
