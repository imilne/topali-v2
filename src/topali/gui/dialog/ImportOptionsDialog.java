// (C) 2003-2007 Biomathematics & Statistics Scotland
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.gui.dialog;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import topali.data.Prefs;
import topali.gui.*;
import topali.i18n.Text;
import topali.var.utils.Utils;

public class ImportOptionsDialog extends JDialog implements ActionListener
{
	private JRadioButton rImport, rCDNA, rAlign, rMulti, rJoin, rExSeq;

	private JButton bOK, bCancel;

	private boolean isOK = false;

	private WinMain winMain;

	public ImportOptionsDialog(WinMain winMain)
	{
		super(winMain, Text.get("import_alignment.1"), true);

		this.winMain = winMain;

		add(getControls(), BorderLayout.CENTER);
		add(getButtons(), BorderLayout.SOUTH);
		getRootPane().setDefaultButton(bOK);
		Utils.addCloseHandler(this, bCancel);

		pack();

		setLocationRelativeTo(winMain);
		setResizable(false);
		setVisible(true);
	}

	private JPanel getControls()
	{
		DblClickListener dblListener = new DblClickListener();

		rImport = new JRadioButton(Text.get("import_alignment.2"));
		rImport.setMnemonic(KeyEvent.VK_E);
		rImport.addMouseListener(dblListener);
		rCDNA = new JRadioButton(Text.get("import_alignment.3"));
		rCDNA.setMnemonic(KeyEvent.VK_C);
		rCDNA.addMouseListener(dblListener);
		rAlign = new JRadioButton(Text.get("import_alignment.4"));
		rAlign.setMnemonic(KeyEvent.VK_P);
		rAlign.setEnabled(false);
		rMulti = new JRadioButton(Text.get("import_alignment.5"));
		rMulti.setMnemonic(KeyEvent.VK_F);
		rMulti.addMouseListener(dblListener);

		rJoin = new JRadioButton(Text.get("import_alignment.8"));
		rJoin.setMnemonic(KeyEvent.VK_J);
		rJoin.addMouseListener(dblListener);
		if(winMain.getProject()!=null && winMain.getProject().getDatasets().size()>1)
			rJoin.setEnabled(true);
		else {
			rJoin.setEnabled(false);
			if(rJoin.isSelected())
				rImport.setSelected(true);
		}

		rExSeq = new JRadioButton(Text.get("import_alignment.6"));
		rExSeq.setMnemonic(KeyEvent.VK_T);
		rExSeq.addMouseListener(dblListener);

		switch (Prefs.gui_import_method)
		{
		case 0:
			rImport.setSelected(true);
			break;
		case 1:
			rCDNA.setSelected(true);
			break;
		case 2:
			rAlign.setSelected(true);
			break;
		case 3:
			rMulti.setSelected(true);
			break;
		case 4:
			rExSeq.setSelected(true);
			break;
		case 5:
			rJoin.setSelected(true);
			break;
		}

		ButtonGroup group = new ButtonGroup();
		group.add(rImport);
		group.add(rCDNA);
		group.add(rAlign);
		group.add(rMulti);
		group.add(rJoin);
		group.add(rExSeq);

		JPanel p1 = new JPanel(new GridLayout(6, 1));
		p1.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createTitledBorder(Text.get("import_alignment.7")),
			BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		p1.add(rImport);
		p1.add(rCDNA);
		p1.add(rAlign);
		p1.add(rMulti);
		p1.add(rJoin);
		p1.add(rExSeq);

		JPanel p2 = new JPanel(new BorderLayout());
		p2.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		p2.add(p1);

		return p2;
	}

	private JPanel getButtons()
	{
		bOK = new JButton(Text.get("ok"));
		bCancel = new JButton(Text.get("cancel"));

		return Utils.getButtonPanel(this, bOK, bCancel, "import_alignment");
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == bCancel)
			setVisible(false);

		else if (e.getSource() == bOK)
		{
			if (rImport.isSelected())
				Prefs.gui_import_method = 0;
			else if (rCDNA.isSelected())
				Prefs.gui_import_method = 1;
			else if (rAlign.isSelected())
				Prefs.gui_import_method = 2;
			else if (rMulti.isSelected())
				Prefs.gui_import_method = 3;
			else if (rExSeq.isSelected())
				Prefs.gui_import_method = 4;
			else if(rJoin.isSelected())
				Prefs.gui_import_method = 5;
			isOK = true;
			setVisible(false);
		}
	}

	public boolean isOK()
	{
		return isOK;
	}

	private class DblClickListener extends MouseAdapter
	{

		public void mouseClicked(MouseEvent e)
		{
			if (e.getClickCount() != 2)
				return;

			bOK.doClick();
		}
	}
}