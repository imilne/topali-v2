// (C) 2003-2007 Biomathematics & Statistics Scotland
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.gui.dialog.jobs.cml;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import topali.data.*;
import topali.gui.*;
import topali.var.Utils;

public class CMLSiteDialog extends JDialog implements ActionListener
{

	WinMain winmain;
	AlignmentData data;
	CodeMLResult res;
	
	CMLSitePanel panel;
	
	public JButton bRun = new JButton(), bCancel = new JButton(), bDefault = new JButton(), bHelp = new JButton();
	
	public CMLSiteDialog(WinMain winMain, AlignmentData data, CodeMLResult res) {
		super(winMain, "Positive Selection - Site Models", false);
		
		this.winmain = winMain;
		this.data = data;
		this.res = res;
		init();
		
		pack();
		setLocationRelativeTo(winmain);
		setResizable(false);
	}
	
	private void init() {
		panel = new CMLSitePanel(res, this);

		this.setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);
		
		JPanel bp = Utils.getButtonPanel(bRun, bCancel, bDefault, bHelp, this, "cmlsite_settings");
		add(bp, BorderLayout.SOUTH);
		
		getRootPane().setDefaultButton(bRun);
		Utils.addCloseHandler(this, bCancel);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == bCancel)
			setVisible(false);

		else if (e.getSource() == bRun)
		{
			setVisible(false);
			
			SequenceSet ss = data.getSequenceSet();

			res = panel.getResult();

			if (Prefs.isWindows)
				res.codemlPath = Utils.getLocalPath() + "codeml.exe";
			else
				res.codemlPath = Utils.getLocalPath() + "codeml/codeml";

			res.selectedSeqs = ss.getSelectedSequenceSafeNames();
			res.isRemote = ((e.getModifiers() & ActionEvent.CTRL_MASK) == 0);
			
			int runNum = data.getTracker().getCodeMLRunCount() + 1;
			data.getTracker().setCodeMLRunCount(runNum);
			res.guiName = "CodeML Result " + runNum;
			res.jobName = "CodeML Analysis " + runNum + " on " + data.name
					+ " (" + ss.getSelectedSequences().length + "/" + ss.getSize()
					+ " sequences)";

			winmain.submitJob(data, res);
		}

		else if (e.getSource() == bDefault) {
			panel.setDefaults();
		}
	}
	
	
}
