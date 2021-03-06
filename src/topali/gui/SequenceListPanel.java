// (C) 2003-2007 Biomathematics & Statistics Scotland
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.gui;

import static topali.gui.WinMainMenuBar.*;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;

import javax.swing.*;
import javax.swing.event.*;

import topali.data.*;
import topali.i18n.Text;
import topali.var.utils.SequenceSetUtils;
import scri.commons.gui.MsgBox;

public class SequenceListPanel extends JPanel implements ListSelectionListener, PropertyChangeListener
{
	private AlignmentPanel disPanel;

	private SequenceSet ss;

	private JList list;

	private DefaultListModel model;

	private Font font;

	private MyPopupMenuAdapter popup;

	SequenceListPanel(AlignmentPanel disPanel, SequenceSet ss)
	{
		this.disPanel = disPanel;
		this.ss = ss;
		this.ss.addChangeListener(this);

		// Create the JList of sequence names
		model = new DefaultListModel();
		list = new JList(model);
		list.setCellRenderer(new SequenceRenderer());
		list.setBackground(new Color(234, 234, 234));
		list.setToolTipText("Sequences in the current project");

		// Add some mouse handlers
		list.addMouseMotionListener(new MouseMotionAdapter()
		{

			public void mouseMoved(MouseEvent e)
			{
				WinMainStatusBar.setText(Text.get("SequenceListPanel.gui05", list
						.getSelectedIndices().length, model.getSize()));
			}
		});
		list.addMouseListener(new MouseAdapter()
		{

			public void mouseExited(MouseEvent e)
			{
				WinMainStatusBar.setText("");
			}
		});

		popup = new MyPopupMenuAdapter();
		list.addMouseListener(popup);
		disPanel.setPopupMenu(popup);

		refreshAndRepaint();

		setLayout(new BorderLayout());
		add(list, BorderLayout.CENTER);
	}

	void refreshAndRepaint()
	{
		font = new Font("Monospaced", Font.PLAIN, Prefs.gui_seq_font_size);
		list.removeListSelectionListener(this);

		model.clear();
		for (int i = 0; i < ss.getSize(); i++)
			model.add(i, ss.getSequence(i).getName() + " \n");

		list.setSelectedIndices(ss.getSelectedSequences());
		list.addListSelectionListener(this);
	}

	JList getList()
	{
		return list;
	}

	void findSequence(JScrollPane sp, int index, boolean select)
	{
		if (select)
			list.setSelectedIndex(index);

		// Work out where in the display this sequence is
		Rectangle r = sp.getViewport().getViewRect();
		Point p = list.indexToLocation(index);

		// And move there if it isn't already visible
		if (r.contains(p) == false)
		{
			sp.getViewport().setViewPosition(new Point(0, 0));
			sp.getViewport().setViewPosition(p);
		}

		//WinMainMenuBar.aFileSave.setEnabled(true);
		//WinMainMenuBar.aVamCommit.setEnabled(true);
		ProjectState.setDataChanged();
	}

	void moveSequences(boolean up, boolean top)
	{
		int[] indices = list.getSelectedIndices();
		ss.moveSequences(indices, up, top);

		disPanel.refreshAndRepaint();

		if (top)
		{
			int[] newIndices = new int[indices.length];
			for (int i = 0; i < newIndices.length; i++)
				newIndices[i] = i;
			indices = newIndices;
		} else
		{
			for (int i = 0; i < indices.length; i++)
				if (up)
					indices[i]--;
				else
					indices[i]++;
		}

		updateList(indices);
	}

	void updateList(int[] indices)
	{
		list.setValueIsAdjusting(true);
		list.setSelectedIndices(indices);
		list.setValueIsAdjusting(false);

		valueChanged(null);
	}

	void selectAll()
	{
		int[] indices = new int[model.size()];
		for (int i = 0; i < model.size(); i++)
			indices[i] = i;

		updateList(indices);
	}

	void selectNone()
	{
		int[] indices = new int[0];
		updateList(indices);
	}

	void selectInvert()
	{
		int[] current = list.getSelectedIndices();
		int newCount = model.size() - current.length;
		int[] indices = new int[newCount];

		for (int i = 0, c = 0; i < model.size() && c < newCount; i++)
		{
			// Search for the current index in the existing selection. If it
			// exists, then we *don't* want to add it to the new set
			boolean found = false;
			for (int j = 0; j < current.length; j++)
				if (current[j] == i)
				{
					found = true;
					break;
				}

			if (!found)
				indices[c++] = i;
		}

		updateList(indices);
	}

	void selectHighlighted(int first, int last)
	{
		int[] indices = new int[last - first + 1];
		for (int i = 0, j = first; j <= last; i++, j++)
			indices[i] = j;
		updateList(indices);
	}

	void selectUnique()
	{
		int[] indices = SequenceSetUtils.getUniqueSequences(ss);
		int duplicateCount = SequenceSetUtils.duplicateCount;
		updateList(indices);

		// Output message
		String str;
		if (indices.length == 1)
			str = Text.get("SequenceListPanel.gui01",
					indices.length)
					+ " ";
		else
			str = Text.get("SequenceListPanel.gui02",
					indices.length)
					+ " ";

		if (duplicateCount == 1)
			str += Text.get("SequenceListPanel.gui03",
					duplicateCount);
		else
			str += Text.get("SequenceListPanel.gui04",
					duplicateCount);

		MsgBox.msg(str, MsgBox.INF);
	}

	public void setMenus()
	{
		WinMainMenuBar.aAlgnMoveUp.setEnabled(false);
		WinMainMenuBar.aAlgnMoveDown.setEnabled(false);
		WinMainMenuBar.aAlgnMoveTop.setEnabled(false);
		WinMainMenuBar.aAlgnRenameSeq.setEnabled(false);

		int[] indices = list.getSelectedIndices();

		if (indices.length == 1)
			WinMainMenuBar.aAlgnRenameSeq.setEnabled(true);

		if (indices.length > 0)
		{
			if (indices[0] > 0)
				WinMainMenuBar.aAlgnMoveUp.setEnabled(true);

			if (indices[indices.length - 1] < model.size() - 1)
				WinMainMenuBar.aAlgnMoveDown.setEnabled(true);

			WinMainMenuBar.aAlgnMoveTop.setEnabled(true);
		}

		// Changes in the list's selection need to be mirrored back in the
		// SequenceSet object
		ss.setSelectedSequences(list.getSelectedIndices());

		if (WinMain.vEvents != null)
			WinMain.vEvents.sendSelectedSequencesEvent(ss);
	}

	public void valueChanged(ListSelectionEvent e)
	{
		if (e!=null && e.getValueIsAdjusting())
			return;

		setMenus();
		//disPanel.refreshAndRepaint();
		disPanel.repaint();

		if (Prefs.gui_preview_current)
			//WinMain.rDialog.updateTreePreview(false);

		ProjectState.setDataChanged();
	}


	public void propertyChange(PropertyChangeEvent evt)
	{
		//if(evt.getPropertyName().equals("name"))
		refreshAndRepaint();
	}

	class SequenceRenderer extends JLabel implements ListCellRenderer
	{
		public SequenceRenderer()
		{
			setOpaque(true);
		}

		// Set the attributes of the class and return a reference
		public Component getListCellRendererComponent(JList list, Object obj,
				int i, boolean iss, boolean chf)
		{
			setFont(font);
			setText(obj.toString());

			// Set background/foreground colours
			if (iss)
			{
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else
			{
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			return this;
		}
	}

	class MyPopupMenuAdapter extends PopupMenuAdapter
	{
		JMenuItem selectHighlighted;

		JMenuItem addPart;

		JMenu annotate;

		MyPopupMenuAdapter()
		{
			// Create the "Select" submenu
			JMenu menu = new JMenu(Text.get("menuAlgnSelect"));

			JMenuItem m1 = getItem(aAlgnSelectAll, KeyEvent.VK_A,
					KeyEvent.VK_A, WinMainMenuBar.menuShortcut);
			JMenuItem m2 = getItem(aAlgnSelectNone, KeyEvent.VK_N, 0, 0);
			JMenuItem m3 = getItem(aAlgnSelectUnique, KeyEvent.VK_U, 0, 0);
			JMenuItem m4 = getItem(aAlgnSelectInvert, KeyEvent.VK_I, 0, 0);
			selectHighlighted = getItem(aAlgnSelectHighlighted, KeyEvent.VK_H,
					0, 0);

			menu.add(m1);
			menu.add(m2);
			menu.add(selectHighlighted);
			menu.add(m3);
			menu.addSeparator();
			menu.add(m4);

			addPart = new JMenuItem();
			addPart.setAction(new AbstractAction()
			{
				public void actionPerformed(ActionEvent e)
				{
					//WinMain.rDialog.addRegion(disPanel.displayCanvas.mouse.nucPosS+1, disPanel.displayCanvas.mouse.nucPosE+1, PartitionAnnotations.class);
					WinMain.annoDialog.addPartition(disPanel.displayCanvas.mouse.nucPosS+1, disPanel.displayCanvas.mouse.nucPosE+1);
					disPanel.highlight(-1, -1, true);
				}
			});
			addPart.setText(Text.get("aAlgnAddPartition"));
			addPart.setMnemonic(KeyEvent.VK_P);

			JMenuItem addCodReg = new JMenuItem();
			addCodReg.setAction(new AbstractAction()
			{
				public void actionPerformed(ActionEvent e)
				{
//					WinMain.rDialog.addRegion(disPanel.mouseHighlight.x,
//							disPanel.mouseHighlight.x
//									+ disPanel.mouseHighlight.width,
//							CDSAnnotations.class);
//					disPanel.highlight(-1, -1, true);
//					disPanel.holdMouseHighlight = false;
				}
			});
			addCodReg.setText(Text.get("aAlgnAddCDS"));
			addCodReg.setMnemonic(KeyEvent.VK_C);

			annotate = new JMenu(Text.get("menuAlgnAnnotate"));
			annotate.add(addPart);
			annotate.add(addCodReg);

			add(aAlgnDisplaySummary, Icons.INFO16, KeyEvent.VK_I, 0, 0, 16,
					false);
			p.addSeparator();
			p.add(menu);
			//p.add(annotate);
//			p.add(addPart);
			p.addSeparator();
			add(aAlgnFindSeq, Icons.FIND16, KeyEvent.VK_F, KeyEvent.VK_F,
					WinMainMenuBar.menuShortcut, 0, false);
			add(aAlgnRenameSeq, KeyEvent.VK_R, 0, 0, 0, false);
			add(aAlgnGoTo, KeyEvent.VK_G, 0, 0, 0, false);
			add(aViewDisplaySettings, KeyEvent.VK_D, KeyEvent.VK_F5, 0, 0, true);
		}

		public void enableSelectHighlighted(boolean b)
		{
			selectHighlighted.setEnabled(b);
			WinMainMenuBar.aAlgnSelectHighlighted.setEnabled(b);
		}

		public void enableAnnotate(boolean b)
		{
			//annotate.setEnabled(b);
			addPart.setEnabled(b);
		}


		protected void handlePopup(int x, int y)
		{

		}


	}
}
