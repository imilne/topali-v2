package topali.gui.dialog.region;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import topali.data.RegionAnnotations.Region;
import topali.gui.Utils;

public class EditRegionDialog extends JDialog implements ActionListener,
		ChangeListener {

	private Region r;
	
	private JSpinner spinStart, spinEnd;
	private SpinnerNumberModel modelStart, modelEnd;
	private JButton bOK, bCancel;
	
	public Region newRegion = null;
	
	EditRegionDialog(Region r, RegionDialog parent, int seqLength)
	{
		super(parent, "Edit Partition", true);
		this.r = r;
		
		// Create the spinner controls
		modelStart = new SpinnerNumberModel(r.getS(), 1, r.getE(), 1);
		modelEnd = new SpinnerNumberModel(r.getE(), r.getS(), seqLength, 1);
		
		spinStart = new JSpinner(modelStart);
		spinStart.addChangeListener(this);
		spinEnd = new JSpinner(modelEnd);
		spinEnd.addChangeListener(this);
		
		// Create button controls
		bOK = new JButton("OK");
		bOK.addActionListener(this);
		bCancel = new JButton("Cancel");
		bCancel.addActionListener(this);
		
		JLabel sLabel = new JLabel("Starting nucleotide: ");
		sLabel.setDisplayedMnemonic(KeyEvent.VK_S);
		sLabel.setLabelFor(spinStart);
		JLabel eLabel = new JLabel("Ending nucleotide: ");
		eLabel.setDisplayedMnemonic(KeyEvent.VK_E);
		eLabel.setLabelFor(spinEnd);
					
		
		JPanel p1 = new JPanel(new GridLayout(2, 2, 5, 5));
		p1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		p1.add(sLabel);
		p1.add(spinStart);
		p1.add(eLabel);
		p1.add(spinEnd);
		
		JPanel p2 = new JPanel(new GridLayout(1, 2, 5, 5));
		p2.add(bOK);
		p2.add(bCancel);
		
		JPanel p3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		p3.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
		p3.add(p2);
		
		getContentPane().setLayout(new BorderLayout(5, 5));
		getContentPane().add(p1, BorderLayout.CENTER);
		getContentPane().add(p3, BorderLayout.SOUTH);
		
		Utils.addCloseHandler(this, bCancel);
		
		pack();
		setLocationRelativeTo(parent);
		setResizable(false);
		setVisible(true);
	}
			
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == bOK)
		{			
			newRegion = new Region(modelStart.getNumber().intValue(), modelEnd.getNumber().intValue());
			setVisible(false);
		}
		
		else if (e.getSource() == bCancel)
			setVisible(false);
	}
	
	public void stateChanged(ChangeEvent e)
	{
		// Starting nucleotide spinner
		if (e.getSource() == spinStart)
			modelEnd.setMinimum(modelStart.getNumber().intValue());
		
		// Ending nucleotide spinner
		else
			modelStart.setMaximum(modelEnd.getNumber().intValue());
	}

}