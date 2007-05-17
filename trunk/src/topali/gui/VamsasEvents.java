package topali.gui;

import topali.data.*;
import topali.vamsas.*;

import uk.ac.vamsas.client.picking.*;

public class VamsasEvents
{
	private WinMain winMain;
	
	public VamsasEvents(WinMain winMain)
	{
		this.winMain = winMain;
	}
	
	void sendAlignmentPanelMouseOverEvent(String seqName, int nuc)
	{
		MouseOverMessage message = new MouseOverMessage(seqName, nuc);
		winMain.vamsas.msgHandler.sendMessage(message);
	}
	
	public void processAlignmentPanelMouseOverEvent(MouseOverMessage message)
	{
		String seqID = message.getVorbaID();
		int position = message.getPosition();
		
		for (AlignmentData data : winMain.getProject().getDatasets())
		{
			int i = 0;
			for (Sequence seq : data.getSequenceSet().getSequences())
			{
				if (seq.name.equals(seqID))
				{
					AlignmentPanel panel = winMain.navPanel.getCurrentAlignmentPanel(data);

					panel.highlight(i, position, false);

					break;
				}

				i++;
			}
		}
	}
}