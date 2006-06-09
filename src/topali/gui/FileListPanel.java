// (C) 2003-2006 Iain Milne
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import topali.data.*;
import topali.gui.dialog.*;

public class FileListPanel extends JPanel implements ListSelectionListener
{
	private AlignmentData data;
	private LinkedList<AlignmentFileStat> refs;
	
	private JTable table;
	private PanelToolBar toolbar;
	
	public FileListPanel(AlignmentData data)
	{
		this.data = data;
		refs = data.getReferences();
		
		setLayout(new BorderLayout());		
		add(createControls());
		add(toolbar = new PanelToolBar(), BorderLayout.EAST);
	}
	
	private JPanel createControls()
	{
		table = new JTable(new AlignmentTableModel());
		table.sizeColumnsToFit(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		table.getSelectionModel().addListSelectionListener(this);
		
		// http://forum.java.sun.com/thread.jspa?forumID=57&threadID=726667
		// Widths to very large values, but proportional values, so it has the 
		// effect of setting the column widths as a percentage of the total 
		// width of the table. It's a bit of a hack, but it works.
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(52000);
		columnModel.getColumn(1).setPreferredWidth(12000);
		columnModel.getColumn(2).setPreferredWidth(12000);
		columnModel.getColumn(3).setPreferredWidth(12000);
		columnModel.getColumn(4).setPreferredWidth(12000);
		
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() != 2) return;
				
				loadAlignment(table.rowAtPoint(e.getPoint()));
			}
		});
		
		
		JScrollPane sp = new JScrollPane(table);
		
		JPanel p1 = new JPanel(new BorderLayout());
//		p1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		p1.add(sp);
		
		return p1;
	}
	
	public void valueChanged(ListSelectionEvent e)
	{
		if (e.getValueIsAdjusting()) return;
		
		toolbar.bImport.setEnabled(table.getSelectedRowCount() == 1);
	}
	
	// Loads the currently selected alignment into TOPALi "properly".
	private void loadAlignment(int tableRow)
	{
		Object obj = table.getValueAt(tableRow, 0);
		if (obj == null)
			return;
				
		AlignmentFileStat stat = (AlignmentFileStat) obj;
				
		File file = new File(stat.filename);
		new ImportDataSetDialog(TOPALi.winMain).loadAlignment(file);
	}
	
	private class AlignmentTableModel extends AbstractTableModel
	{
		public String getColumnName(int col)
		{
			switch (col)
			{
				case 0: return "Alignment Filename";
				case 1: return "Sequences";
				case 2: return "Length";
				case 3: return "Type";
				case 4: return "Size on Disk";
			}
			
			return null;
		}
		
		public int getColumnCount()
			{ return 5; }
		
		public int getRowCount()
			{ return refs.size(); }
		
		public Object getValueAt(int row, int col)
		{
			AlignmentFileStat stat = refs.get(row);
			
			switch (col)
			{
				case 0: return stat;
				case 1: return stat.size;
				case 2: return stat.length;
				case 3: return stat.isDna ? "DNA" : "Protein";
				case 4: return (stat.fileSize / 1024) + "KB";
			}
			
			return null;
		}
	}
	
	private class PanelToolBar extends JToolBar implements ActionListener
	{
		private JButton bImport;
		
		PanelToolBar()
		{
			setFloatable(false);
			setBorderPainted(false);
			setOrientation(VERTICAL);
			setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
			
			bImport = (JButton) WinMainToolBar.getButton(false, null, "list01",
				Icons.TABLE_IMPORT, null);
			bImport.setEnabled(false);
			bImport.addActionListener(this);
			
			add(bImport);
		}
		
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == bImport)
				loadAlignment(table.getSelectedRow());
		}
	}
}