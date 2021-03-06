// (C) 2003-2007 Biomathematics & Statistics Scotland
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.gui.dialog;

import static topali.mod.Filters.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;

import org.apache.log4j.Logger;

import pal.alignment.Alignment;
import topali.data.*;
import topali.fileio.AlignmentLoadException;
import topali.gui.*;
import topali.i18n.Text;
import topali.mod.Filters;
import topali.var.utils.SequenceSetUtils;
import scri.commons.gui.MsgBox;

public class ImportDataSetDialog extends JDialog implements Runnable
{
	 Logger log = Logger.getLogger(this.getClass());

	private AlignmentData data;

	private WinMain winMain;

	private String name;

	private File filename;

	private Alignment alignment;

	public ImportDataSetDialog(WinMain winMain)
	{
		super(winMain, Text.get("ImportDataSetDialog.gui01"),
				true);
		this.winMain = winMain;

		addWindowListener(new WindowAdapter()
		{

			public void windowOpened(WindowEvent e)
			{
				doLoad();
			}
		});

		JLabel icon = new JLabel(Icons.UNKNOWN);
		icon.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
		JLabel label1 = new JLabel(Text.get("ImportDataSetDialog.gui02"));
		JPanel p1 = new JPanel(new GridLayout(1, 1, 0, 2));
		p1.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));
		p1.add(label1);
		add(p1);
		add(icon, BorderLayout.WEST);

		pack();
		setResizable(false);
		setLocationRelativeTo(winMain);
	}

	public void promptForAlignment()
	{
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle(Text.get("ImportDataSetDialog.gui01"));
		fc.setCurrentDirectory(new File(Prefs.gui_dir));

		Filters.setFilters(fc, -1, FAS, PHY_S, PHY_I, ALN, MSF, NEX, NEX_B);

		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			File file = fc.getSelectedFile();
			Prefs.gui_dir = "" + fc.getCurrentDirectory();

			loadAlignment(file);
		}
	}

	public void loadAlignment(File file)
	{
		String name = file.getName();
		if (name.indexOf(".") != -1)
			name = name.substring(0, name.lastIndexOf("."));

		load(name, file, null);
	}

	public void cloneAlignment(String name, Alignment alignment)
	{
		load(name, null, alignment);
	}

	private void load(String name, File filename, Alignment alignment)
	{
		this.name = name;
		this.filename = filename;
		this.alignment = alignment;

		setVisible(true);
	}

	private void doLoad()
	{
		new Thread(this).start();
	}

	public void run()
	{
		try
		{
			SequenceSet ss = null;
			if (filename != null)
				ss = new SequenceSet(filename);
			else
				ss = new SequenceSet(alignment);

			if (SequenceSetUtils.verifySequenceNames(ss) == false)
				MsgBox.msg(Text.get("ImportDataSetDialog.err05"),
						MsgBox.WAR);

			data = new AlignmentData(name, ss);

			if (isVisible())
				winMain.addNewAlignmentData(data);
		}
		catch (AlignmentLoadException e)
		{
			log.warn("Import failed.\n",e);
			int code = e.getReason();

			String text = Text.get("ImportDataSetDialog.err0" + code);
			if(e.getInfo()!=null)
				text += "\n["+e.getInfo()+"]";
			MsgBox.msg(text, MsgBox.ERR);
		}

		setVisible(false);
	}
}