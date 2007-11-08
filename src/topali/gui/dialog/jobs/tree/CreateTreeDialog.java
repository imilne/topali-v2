// (C) 2003-2007 Biomathematics & Statistics Scotland
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.gui.dialog.jobs.tree;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import pal.alignment.Alignment;
import topali.data.*;
import topali.gui.*;
import topali.var.Utils;
import doe.MsgBox;

public class CreateTreeDialog extends JDialog implements ActionListener
{
	private AlignmentData data;

	private SequenceSet ss;

	private Alignment alignment;

	private TreeResult result;

	//private JButton bOK, bCancel;
	private JButton bRun = new JButton(), bCancel = new JButton(), bDefault = new JButton(), bHelp = new JButton();

	private JTabbedPane tabs;

	private TreeDialogPanel basicPanel;

	private AdvancedMrBayes bayesPanel;

	private AdvancedPhyML phymlPanel;

	private AdvancedCDNAMrBayes cdnaPanel;
	
	MBTreeResult mbResult = null;
	MBTreeResult cdnaResult = null;
	PhymlResult phymlResult = null;

	boolean showPhyMLInfo = true;
	boolean showMBInfo = true;
	
	public CreateTreeDialog(WinMain winMain, AlignmentData data, TreeResult result)
	{
		super(winMain, "Estimate New Tree", true);
		this.data = data;

		if(result!=null) {
			if(result instanceof MBTreeResult) {
				if(((MBTreeResult)result).partitions.size()==1) {
					Prefs.gui_tree_method=2;
					this.mbResult = (MBTreeResult)result;
				}
				else if(((MBTreeResult)result).partitions.size()==3) {
					Prefs.gui_tree_method=3;
					this.cdnaResult = (MBTreeResult)result;
				}
			}
			else if(result instanceof PhymlResult) {
				this.phymlResult = (PhymlResult)result;
				Prefs.gui_tree_method = 1;
			}
		}
		
		ss = data.getSequenceSet();

		setLayout(new BorderLayout());
		add(createControls());
		JPanel bp = Utils.getButtonPanel(bRun, bCancel, bDefault, bHelp, this, "estimate_tree");
		JPanel bPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		bPanel.add(bp);
		add(bPanel, BorderLayout.SOUTH);

		getRootPane().setDefaultButton(bRun);
		Utils.addCloseHandler(this, bCancel);

		pack();
		setSize(360,350);
		setLocationRelativeTo(winMain);
		setResizable(false);
		setVisible(true);
	}

	public void switchMethod() {
		
		if(tabs==null)
			return;
		
		if(Prefs.gui_tree_method==0) {
			tabs.setEnabledAt(1, false);
		}
		else if(Prefs.gui_tree_method==1) {
			tabs.remove(1);
			//tabs.add(new JScrollPane(phymlPanel), "Advanced");
			tabs.add(new JScrollPane(phymlPanel), "Advanced");
			tabs.setEnabledAt(1, true);
			
			if(!phymlPanel.modelIsSupported && showPhyMLInfo) {
    			if(!phymlPanel.altModelFound)
    				MsgBox.msg("The substitution model associated with this\n" +
    						"alignment is not supported by PhyML.\n" +
    						"Therefore model '"+phymlPanel.altModel+"' has been suggested.", MsgBox.INF);
    			else
    				MsgBox.msg("The substitution model associated with this\n" +
    						"alignment is not supported by PhyML.\n" +
    						"Therefore the default '"+phymlPanel.altModel+"' model will be preselected.", MsgBox.INF);
    			showPhyMLInfo = false;
    		}			
		}
		else if(Prefs.gui_tree_method==2) {
			tabs.remove(1);
			//tabs.add(new JScrollPane(bayesPanel), "Advanced");
			tabs.add(new JScrollPane(bayesPanel), "Advanced");
			tabs.setEnabledAt(1, true);
			
			if(!bayesPanel.modelIsSupported && showMBInfo) {
    			if(!bayesPanel.altModelFound)
    				MsgBox.msg("The substitution model associated with this\n" +
    						"alignment is not supported by MrBayes.\n" +
    						"Therefore model '"+bayesPanel.altModel+"' has been suggested.", MsgBox.INF);
    			else
    				MsgBox.msg("The substitution model associated with this\n" +
    						"alignment is not supported by MrBayes.\n" +
    						"Therefore the default '"+bayesPanel.altModel+"' model will be preselected.", MsgBox.INF);
    			showMBInfo = false;
    		}
		}
		else if(Prefs.gui_tree_method==3) {
			tabs.remove(1);
			tabs.add(new JScrollPane(cdnaPanel), "Advanced");
			tabs.setEnabledAt(1, true);
		}
		validate();
		repaint();
	}

	private JComponent createControls()
	{
		basicPanel = new TreeDialogPanel(this, ss);
		basicPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		bayesPanel = new AdvancedMrBayes(data.getSequenceSet(), this.mbResult);
		bayesPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		phymlPanel = new AdvancedPhyML(data.getSequenceSet(), this.phymlResult);
		phymlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		cdnaPanel = new AdvancedCDNAMrBayes(data.getSequenceSet(), this.cdnaResult);
		cdnaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		tabs = new JTabbedPane();
		tabs.add(basicPanel, "Basic");
		if(Prefs.gui_tree_method==0) {
			tabs.add(new JPanel(), "Advanced");
			tabs.setEnabledAt(1, false);
		}
		else if(Prefs.gui_tree_method==1) {
			tabs.add(new JScrollPane(phymlPanel), "Advanced");
		}
		else if(Prefs.gui_tree_method==2) {
			tabs.add(new JScrollPane(bayesPanel), "Advanced");
		}
		else if(Prefs.gui_tree_method==3) {
			tabs.add(new JScrollPane(cdnaPanel), "Advanced");
		}

		return tabs;
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == bCancel)
			setVisible(false);

		else if (e.getSource() == bRun)
			onOK((e.getModifiers() & ActionEvent.CTRL_MASK) == 0);
		
		else if(e.getSource() == bDefault) {
			bayesPanel = new AdvancedMrBayes(data.getSequenceSet(), null);
			phymlPanel = new AdvancedPhyML(data.getSequenceSet(), null);
			cdnaPanel = new AdvancedCDNAMrBayes(data.getSequenceSet(), null);
			
			basicPanel.f84.setSelected(true);
		}
	}

	private void onOK(boolean makeRemote)
	{
		if (Prefs.gui_tree_method == 0)
			result = new TreeResult();
		else if(Prefs.gui_tree_method == 1) {
			this.result = phymlPanel.onOK();
		}
		else if(Prefs.gui_tree_method == 2) {
			this.result = bayesPanel.onOK();
		}
		else if(Prefs.gui_tree_method == 3) {
			this.result = cdnaPanel.onOk();
		}

		result.isRemote = makeRemote;
		initTreeResult(result);
		setVisible(false);
	}

	// Returns the TreeResult object, and also sets up the PAL alignment used
	// to make the tree if a local JC tree is being created.
	public TreeResult getTreeResult()
	{
		if (result == null)
			return null;

		int[] indices = null;
		if (Prefs.gui_tree_useall)
			indices = ss.getAllSequences();
		else
			indices = ss.getSelectedSequences();

		if (indices.length < 3)
		{
			MsgBox.msg("You must have at least 3 sequences selected to create "
					+ "a phylogenetic tree.", MsgBox.ERR);
			return null;
		}

		alignment = ss.getAlignment(indices, result.getPartitionStart(), result
				.getPartitionEnd(), true);

		return result;
	}

	public Alignment getAlignment()
	{
		return alignment;
	}

	private void initTreeResult(TreeResult tr)
	{
		// Current partition information
		tr.setPartitionStart(data.getActiveRegionS());
		tr.setPartitionEnd(data.getActiveRegionE());

		int runNum = data.getTracker().getTreeRunCount() + 1;
		data.getTracker().setTreeRunCount(runNum);

		result.selectedSeqs = ss.getSelectedSequenceSafeNames();
		if(Prefs.gui_tree_method==0) {
			if(ss.getParams().isDNA())
				result.guiName = "F84+G Tree" + runNum;
			else
				result.guiName = "WAG+G Tree" + runNum;
			result.jobName = "Tree Estimation";
		}
		else if(Prefs.gui_tree_method==1) {
			result.guiName = "ML Tree "+runNum;
			result.jobName = "PhyML Tree Estimation ";
		}
		else if(Prefs.gui_tree_method==2 || Prefs.gui_tree_method==3) {
			result.guiName = "MrBayes Tree " + runNum;
			result.jobName = "MrBayes Tree Estimation ";
		}

		result.jobName += runNum + " on " + data.name
				+ " (" + ss.getSelectedSequences().length + "/" + ss.getSize()
				+ " sequences)";

		if(Prefs.gui_tree_method==1)
			initPhymlTreeResult((PhymlResult)tr);

		if (Prefs.gui_tree_method == 2 || Prefs.gui_tree_method == 3)
			initMBTreeResult((MBTreeResult) tr);

	}

	private void initPhymlTreeResult(PhymlResult tr) {
		//Path to Phyml
		if (Prefs.isWindows)
			tr.phymlPath = Utils.getLocalPath() + "\\phyml_win32.exe";
		else if(Prefs.isMacOSX)
			tr.phymlPath = Utils.getLocalPath() + "/phyml/phyml_macOSX";
		else if (Prefs.isLinux)
			tr.phymlPath = Utils.getLocalPath() + "/phyml/phyml_linux";
		else
			tr.phymlPath = Utils.getLocalPath() + "/phyml/phyml_sunOS";

		//Use all sequences, or just those selected?
		if (Prefs.gui_tree_useall)
			tr.selectedSeqs = data.getSequenceSet().getAllSequenceSafeNames();
		else
			tr.selectedSeqs = data.getSequenceSet()
					.getSelectedSequenceSafeNames();
	}

	private void initMBTreeResult(MBTreeResult tr)
	{
		// Path to MrBayes
		if (Prefs.isWindows)
			tr.mbPath = Utils.getLocalPath() + "\\mb.exe";
		else
			tr.mbPath = Utils.getLocalPath() + "/mrbayes/mb";

		// Use all sequences, or just those selected?
		if (Prefs.gui_tree_useall)
			tr.selectedSeqs = data.getSequenceSet().getAllSequenceSafeNames();
		else
			tr.selectedSeqs = data.getSequenceSet()
					.getSelectedSequenceSafeNames();
	}
}