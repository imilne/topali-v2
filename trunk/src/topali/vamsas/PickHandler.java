package topali.vamsas;

import javax.swing.*;

import topali.gui.*;

import uk.ac.vamsas.client.picking.*;

public class PickHandler implements IMessageHandler
{
	private IPickManager manager;
		
	public PickHandler()
	{
		manager = new SocketManager();
		manager.registerMessageHandler(this);
	}
	
	public void sendMessage(Message message)
	{
		manager.sendMessage(message);
	}
	
	public void handleMessage(final Message message)
	{
		Runnable r = new Runnable() {
			public void run() {
				processMessage(message);
			}
		};
		
		SwingUtilities.invokeLater(r);
	}
	
	private void processMessage(Message message)
	{
		if (message instanceof MouseOverMessage)
		{
			MouseOverMessage mom = (MouseOverMessage) message;
			
			String seqID = mom.getVorbaID();
			int position = mom.getPosition();
			
			TOPALi.winMain.vamsasMouseOver(seqID, position);
		}
	}
}