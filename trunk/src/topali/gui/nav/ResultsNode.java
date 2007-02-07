// (C) 2003-2007 Biomathematics & Statistics Scotland
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.gui.nav;

import topali.data.*;
import static topali.gui.WinMainMenuBar.*;

public abstract class ResultsNode extends INode
{
	protected AnalysisResult result;
	
	public ResultsNode(AlignmentData data, AnalysisResult result)
	{
		super(data);
		
		this.result = result;
	}
	
	AnalysisResult getResult()
		{ return result; }
	
	public void setMenus()
	{
		aFileExportDataSet.setEnabled(true);
		
		aAnlsCreateTree.setEnabled(true);
		aAnlsPartition.setEnabled(true);
		aAnlsRename.setEnabled(true);
		aAnlsRemove.setEnabled(true);
		
		aVamExport.setEnabled(true);
	}
}