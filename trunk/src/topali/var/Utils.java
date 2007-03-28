// (C) 2003-2007 Biomathematics & Statistics Scotland
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.var;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import topali.cluster.ClusterUtils;
import topali.gui.*;
import doe.MsgBox;

/**
 * Contains misc methods that don't really fit in anywhere else (but are likely
 * to be used by multiple classes
 */
public class Utils
{
	
	/* Ensures the scratch directory exists */
	public static void createScratch()
	{
		Prefs.tmpDir.mkdirs();
	}

	/* Deletes all the files in the scratch directory */
	public static void emptyScratch()
	{
		TOPALi.log.info("Clearing scratch files");
		ClusterUtils.emptyDirectory(Prefs.tmpDir, true);
	}

	public static JPanel getButtonPanel(ActionListener al, JButton bOK,
			JButton bCancel, String helpString)
	{
		bOK.addActionListener(al);
		bCancel.addActionListener(al);
		JButton bHelp = TOPALiHelp.getHelpButton(helpString);

		JPanel p1 = new JPanel(new GridLayout(1, 3, 5, 5));
		p1.add(bOK);
		p1.add(bCancel);
		p1.add(bHelp);

		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		p2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		p2.add(p1);

		return p2;
	}

	/**
	 * Forces the given Window to hide by mapping a KeyEvent for Escape onto the
	 * given JComponent.
	 */
	public static void addCloseHandler(final Window wnd, JComponent cmp)
	{
		AbstractAction closeAction = new AbstractAction()
		{
			public void actionPerformed(ActionEvent e)
			{
				wnd.setVisible(false);
			}
		};

		KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		cmp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, "close");
		cmp.getActionMap().put("close", closeAction);
	}

	public static void setTextAreaDefaults(JTextArea text)
	{
		text.setMargin(new Insets(5, 5, 5, 5));
		text.setEditable(false);
		text.setFont(new Font("Monospaced", Font.PLAIN, 11));
		text.setTabSize(4);
	}

	public static Color getColor(int number)
	{
		Random r = new Random(Prefs.gui_color_seed + 1253 + number);
		return new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
	}

	public static void copyToClipboard(String str)
	{
		StringSelection selection = new StringSelection(str);

		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection,
				null);

		MsgBox.msg(Text.Gui.getString("clipboard_2"), MsgBox.INF);
	}
	
	public static String getClipboardContent()
	{
		Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable transfer = sysClip.getContents(null);
		
		try
		{
			Object o = transfer.getTransferData(DataFlavor.stringFlavor);
			if(o instanceof String)
				return (String)o;
			else
				return null;
		} catch (Exception e)
		{
			TOPALi.log.warning("Clipboard content is not a String\n"+e);
			return null;
		}
	}

	public static void saveComponent(Component cmp, File file, int w, int h) throws IOException
	{
		// Store the container's current parent
		Container parent = cmp.getParent();

		// Create an image to draw upon
		BufferedImage bi = new BufferedImage(w, h,
				BufferedImage.TYPE_BYTE_INDEXED);
		Graphics2D g2d = (Graphics2D) bi.createGraphics();

		// Redraw the component using the image

		SwingUtilities.paintComponent(g2d, cmp, new Container(), 0, 0, w, h);

		g2d.setColor(Color.black);
		g2d.drawRect(0, 0, w - 1, h - 1);

		// Save it...
		ImageIO.write(bi, "png", file);

		// Finally, reassociate the container with its parent
		if (parent != null)
			parent.add(cmp);
	}

	public static String getLocalPath()
	{
		if (Prefs.isWindows)
			return System.getProperty("user.dir") + "\\web\\binaries\\";
		else if (Prefs.isMacOSX)
			return System.getProperty("user.dir") + "/web/binaries/src/";
		else
			return System.getProperty("user.dir") + "/web/binaries/src/";
	}
	
	public static String mapToString(Map<String, String> map, char keyValueDelim, char delim) {
		StringBuffer sb = new StringBuffer();
		Set<String> keys = map.keySet();
		Iterator<String> it = keys.iterator();
		while(it.hasNext()) {
			String k = it.next();
			sb.append(k);
			sb.append(keyValueDelim);
			sb.append(map.get(k));
			if(it.hasNext())
				sb.append(delim);
		}
		return sb.toString();
	}
	
	public static Map<String, String> stringToMap(String map, char keyValueDelim, char delim) {
		Map<String, String> result = new HashMap<String, String>();
		String[] tokens = map.split(Character.toString(delim));
		for(String token : tokens) {
			String[] tmp = token.split(Character.toString(keyValueDelim));
			result.put(tmp[0], tmp[1]);
		}
		return result;
	}
	
	public static double[][] float2doubleArray(float[][] data) {
		double[][] ddata  = new double[0][0];
		if(data.length>0) {
			ddata = new double[data.length][data[0].length];
			for(int i=0; i<data.length; i++)
				for(int j=0; j<data[i].length; j++)
					ddata[i][j] = (double)data[i][j];
		}
		return ddata;
	}
}