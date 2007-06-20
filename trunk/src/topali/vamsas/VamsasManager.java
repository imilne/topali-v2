// (C) 2003-2007 Biomathematics & Statistics Scotland
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.vamsas;

import java.beans.*;
import java.io.IOException;

import org.apache.log4j.Logger;

import topali.gui.Project;
import uk.ac.vamsas.client.*;
import uk.ac.vamsas.client.picking.IPickManager;
import uk.ac.vamsas.client.simpleclient.SimpleClientFactory;
import doe.MsgBox;

public class VamsasManager
{
	Logger log = Logger.getLogger(this.getClass());
	
	
	// An instance of the pickmanager for dealing with inter-app messages
	public VamsasMsgHandler msgHandler;

	private IClientFactory clientfactory;
	private IClient vorbaclient;
	
	static UserHandle user;
	static ClientHandle app;

	Project topaliProject;
	
	private ObjectMapper vMap = new ObjectMapper();

	public VamsasManager()
	{
	}
	
	public boolean initializeVamsas(Project project)
	{
		this.topaliProject = project;
		
		if (initVamsas() == false)
			return false;
		
		if (addHandlers() == false)
			return false;
		
		if (joinSession() == false)
			return false;
		
		// This can fail - just means pick events won't work
		createPickHandler();
		
		return true;
	}

	private boolean initVamsas()
	{
		try { clientfactory = new SimpleClientFactory(); }
		catch (IOException e)
		{
			log.warn("Creating VAMSAS ClientFactory failed.", e);
			return false;
		}
		
		// Get an Iclient with session data
		app = new ClientHandle("topali", "2");
		user = new UserHandle(System.getProperty("user.name"), "");
		// TODO: session can hold the ID of an existing session that the user
		// wants to connect to
		String session = null;
		
		try
		{
			if (session != null)
				vorbaclient = clientfactory.getIClient(app, user, session);
			else
				vorbaclient = clientfactory.getIClient(app, user);
		}
		catch (NoDefaultSessionException e)
		{
			MsgBox.msg("There appear to be several sessions to choose from.", MsgBox.ERR);			
			return false;
		}
		
		String[] sessions = clientfactory.getCurrentSessions();
		System.out.println("VAMSAS sessions:");
		for (int s = 0; s < sessions.length; s++)
			System.err.println(sessions[s]);
		
		return true;
	}
	
	private boolean addHandlers()
	{
		vorbaclient.addDocumentUpdateHandler(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e)
			{
				handleDocumentUpdate(e);
			}
		});
		
		// Register close handler
		vorbaclient.addVorbaEventHandler(Events.DOCUMENT_REQUESTTOCLOSE, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e)
			{
				handleCloseEvent(e);
			}
		});
		
		// Register client creation handler
		vorbaclient.addVorbaEventHandler(Events.CLIENT_CREATION, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e)
			{
				handleClientCreationEvent(e);
			}
		});
		
		// Register client finalization handler
		vorbaclient.addVorbaEventHandler(Events.CLIENT_FINALIZATION, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e)
			{
				handleClientFinalizationEvent(e);
			}
		});
		
		// Register session shutdown handler
		vorbaclient.addVorbaEventHandler(Events.SESSION_SHUTDOWN, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e)
			{
				handleSessionShutdownEvent(e);
			}
		});
		
		// Register document finalize handler
		vorbaclient.addVorbaEventHandler(Events.DOCUMENT_FINALIZEAPPDATA, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e)
			{
				handleDocumentFinalizeEvent(e);
			}
		});
		
		return true;
	}
	
	private boolean joinSession()
	{
		// Join the session
		try { vorbaclient.joinSession(); }
		catch (Exception e)
		{
			log.warn("Could not join session", e);
			return false;
		}
		
		return true;
	}
	
	private boolean createPickHandler()
	{
		IPickManager manager = vorbaclient.getPickManager();
		
		if (manager == null)
		{
			log.warn("TOPALi could not initiate a VAMSAS session pick "
				+ "handler. You will be unable to trasmit events\nbetween "
				+ "VAMSAS applications.");

			return false;
		}
		
		msgHandler = new VamsasMsgHandler(manager);		
		return true;
	}

	/**
	 * Opens a reference to the vamsas session and uses the DocumentHandler to
	 * store the current TOPALi datasets in it.
	 */
	public boolean writeToDocument()
	{
		try
		{
			IClientDocument cdoc = vorbaclient.getClientDocument();
				
			DocumentHandler handler = new DocumentHandler(topaliProject, vMap, cdoc);
			handler.writeToDocument();			
			cdoc.setVamsasRoots(cdoc.getVamsasRoots());
			vorbaclient.updateDocument(cdoc);
			return true;
		} catch (Exception e)
		{
			log.warn("Writing to VAMSAS document failed.", e);
			return false;
		} 
	}
	
	/**
	 * Reads the data from the current VAMSAS session and updates 
	 * the Topali project
	 * @return
	 * @throws Exception
	 */
	public boolean readFromDocument() {
		try
		{
			IClientDocument cdoc = vorbaclient.getClientDocument();
			DocumentHandler handler = new DocumentHandler(topaliProject, vMap, cdoc);
			handler.readFromDocument();
			vorbaclient.updateDocument(cdoc);
			cdoc = null;
			return true;
		} catch (Exception e)
		{
			log.warn("Reading from VAMSAS document failed.", e);
			return false;
		}
	}
	
	private void handleDocumentUpdate(PropertyChangeEvent e)
	{
		System.out.println("Vamsas document update for "
			+ e.getPropertyName() + ": " + e.getOldValue()
			+ " to " + e.getNewValue());
		
		readFromDocument();
	}
	
	private void handleCloseEvent(PropertyChangeEvent e)
	{
		System.out.println("handleCloseEvent...\n"+e);
		// TODO: ask user for a fileto save to then pass it to the vorba object
		// vorbaclient.storeDocument(java.io.File);
	}
	
	private void handleClientCreationEvent(PropertyChangeEvent e)
	{
		// Tell app add new client to its list of clients
		System.out.println("New Vamsas client for "
			+ e.getPropertyName() + ": "
			+ e.getOldValue() + " to "
			+ e.getNewValue());
	}
	
	private void handleClientFinalizationEvent(PropertyChangeEvent e)
	{
		// Tell app to update its list of clients to communicate with
		System.out.println("Vamsas client finalizing for "
			+ e.getPropertyName() + ": "
			+ e.getOldValue() + " to "
			+ e.getNewValue());
        
	}
	
	private void handleSessionShutdownEvent(PropertyChangeEvent e)
	{
		// Tell app to finalize its session data before shutdown
		System.out.println("Session " + e.getPropertyName()
			+ " is shutting down.");
	}
	
	private void handleDocumentFinalizeEvent(PropertyChangeEvent e)
	{
		// Tell app to finalize its session data prior to the storage of the
		// current session as an archive.
		System.out.println("Application received a DOCUMENT_FINALIZEAPPDATA event.\n"+e);   
	}
	
}