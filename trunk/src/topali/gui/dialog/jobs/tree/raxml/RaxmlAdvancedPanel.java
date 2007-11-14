/*
 * RaxmlAdvancedPanel.java
 *
 * Created on 08 November 2007, 14:45
 */

package topali.gui.dialog.jobs.tree.raxml;

import javax.swing.*;

import topali.data.*;
import topali.gui.Prefs;

/**
 *
 * @author  dlindn
 */
public class RaxmlAdvancedPanel extends javax.swing.JPanel {
    
	AlignmentData data;
	
    /** Creates new form RaxmlAdvancedPanel */
    public RaxmlAdvancedPanel() {
        initComponents();
        SpinnerNumberModel mod = new SpinnerNumberModel(0, 0, 1000, 10);
        bs.setModel(mod);
    }
    
    public RaxmlAdvancedPanel(AlignmentData data) {
    	this();
    	this.data = data;
    }
    
    public void init(RaxmlResult res) {
    	if(data.getSequenceSet().isDNA()) {
    		DefaultComboBoxModel mod = new DefaultComboBoxModel(new String[] {"GTR"});
    		model.setModel(mod);
    		model.setEnabled(false);
    		empfreqLabel.setEnabled(false);
    		empfreq.setEnabled(false);
    	}
    	else {
    		String[] models = new String[] {"Dayhoff", "DCMut", "JTT", "MTRev", "WAG", "RTRev", "CPRev", "VT", "Blosum", "MtMam", "GTR"};
    		DefaultComboBoxModel mod = new DefaultComboBoxModel(models);
    		model.setModel(mod);
    		
    		String defModel = Prefs.rax_protmodel;
    		if(res!=null && res.partitions.size()>0) {
    			RaxPartition f = res.partitions.get(0);
    			defModel = f.model;
    		}
    		else if(data.getSequenceSet().getParams().getModel()!=null) {
    			for(String s : models) {
    				if(data.getSequenceSet().getParams().getModel().is(s)) {
    					defModel = s;
    					break;
    				}
    			}
    		}
    		model.setSelectedItem(defModel);
    		model.setEnabled(true);
    		empfreqLabel.setEnabled(true);
    		empfreq.setEnabled(true);
    	}
    	if(res!=null)
    		bs.setValue(res.bootstrap);
    	else
    		bs.setValue(Prefs.rax_bootstrap);
    	
    	if(res!=null)
    		empfreq.setSelected(res.empFreq);
    	else 
    		empfreq.setSelected(Prefs.rax_empfreq);
    	
    	if(res!=null)
    		ratehet.setSelectedItem(res.rateHet);
    	else
    		ratehet.setSelectedItem(Prefs.rax_ratehet);
    }
    
    public void setDefaults() {
    	bs.setValue(0);
    	ratehet.setSelectedItem("MIX");
    	empfreq.setSelected(false);
    	if(!data.getSequenceSet().isDNA()) 
    		model.setSelectedItem("WAG");
    }
    
    public RaxmlResult onOK() {
    	RaxmlResult res = new RaxmlResult();
    	res.bootstrap = (Integer)bs.getValue();
    	res.empFreq = empfreq.isSelected();
    	res.rateHet = (String)ratehet.getSelectedItem();
    	RaxPartition p1 = new RaxPartition("1-"+data.getSequenceSet().getLength(), "partition", (String)model.getSelectedItem(), data.getSequenceSet().isDNA());
    	res.partitions.add(p1);
    	
    	Prefs.rax_bootstrap = res.bootstrap;
    	Prefs.rax_empfreq = res.empFreq;
    	Prefs.rax_ratehet = res.rateHet;
    	if(!data.getSequenceSet().isDNA())
    		Prefs.rax_protmodel = (String)model.getSelectedItem();
    	
    	return res;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabel3 = new javax.swing.JLabel();
        bs = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        model = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        ratehet = new javax.swing.JComboBox();
        empfreqLabel = new javax.swing.JLabel();
        empfreq = new javax.swing.JCheckBox();

        jLabel3.setText("Bootstrap:");

        jLabel1.setText("Model:");

        model.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setText("Rate Heterogenity: ");

        ratehet.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "CAT", "MIX", "GAMMA" }));

        empfreqLabel.setText("Emp. Frequencies:");

        empfreq.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        empfreq.setMargin(new java.awt.Insets(0, 0, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(empfreqLabel)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bs, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                    .addComponent(empfreq)
                    .addComponent(model, 0, 206, Short.MAX_VALUE)
                    .addComponent(ratehet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(model, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(ratehet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(empfreqLabel)
                    .addComponent(empfreq))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(bs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JSpinner bs;
    public javax.swing.JCheckBox empfreq;
    public javax.swing.JLabel empfreqLabel;
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabel2;
    public javax.swing.JLabel jLabel3;
    public javax.swing.JComboBox model;
    public javax.swing.JComboBox ratehet;
    // End of variables declaration//GEN-END:variables
    
}
