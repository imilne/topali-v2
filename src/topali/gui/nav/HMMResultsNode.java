// (C) 2003-2007 Biomathematics & Statistics Scotland
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.gui.nav;

import java.awt.print.Printable;

import javax.swing.JComponent;

import topali.data.*;
import topali.gui.WinMainTipsPanel;
import topali.gui.results.HMMResultPanel;

public class HMMResultsNode extends ResultsNode 
{

	HMMResultsNode(AlignmentData data, HMMResult result)
	{
		super(data, result);

		panel = new HMMResultPanel(data, result);
	}

	
	public String toString()
	{
		return result.guiName;
	}

	
	public int getTipsKey()
	{
		return WinMainTipsPanel.TIPS_NONE;
	}

	
	public String getHelpKey()
	{
		return "hmm_method";
	}

	
	public JComponent getPanel()
	{
		return panel;
	}
	
	public boolean isPrintable()
	{
		return true;
	}
	
	public Printable[] getPrintables()
	{
		return panel.getPrintables();
	}
}