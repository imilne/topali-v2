// (C) 2003-2007 Biomathematics & Statistics Scotland
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.cluster.jobs.dss.analysis;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import pal.alignment.*;
import pal.distance.*;
import pal.misc.Identifier;
import pal.tree.*;
import topali.analyses.TreeUtilities;
import topali.data.*;

public class DSS
{
	static final Logger log = Logger.getLogger(DSS.class);
	
	public static final int METHOD_JC = 1;

	public static final int METHOD_F84 = 2;

	public static final int POWER_UNWEIGHTED = 1;

	public static final int POWER_WEIGHTED = 2;

	public static final int ONE_PASS = 1;

	public static final int TWO_PASS = 2;

	private File wrkDir;

	private SimpleAlignment win1, win2;

	private DSSResult result;

	private double fitch1, fitch2, fitch3, fitch4;

	private double gapThreshold;
	
	boolean less4 = false;
	
	public DSS(File wrkDir, DSSResult result, SimpleAlignment win1,
			SimpleAlignment win2, double gapThreshold)
	{
		this.wrkDir = wrkDir;
		this.result = result;
		this.win1 = win1;
		this.win2 = win2;
		this.gapThreshold = gapThreshold;
	}

	void calculateFitchScores() throws Exception
	{
	    
		//Exclude bad sequences
		if(this.gapThreshold<1) {
			less4 = removeGapSequences();
			if(less4)
			    return;
		}
		
		// Calculate distances for the two windows
		DistanceMatrix dm1 = getDistance(win1, result.method);
		DistanceMatrix dm2 = getDistance(win2, result.method);

		// Scale (and multiply) each distance matrix
		scaleMulDistanceMatrix(dm1, result.avgDist / getAverageDistance(dm1));
		scaleMulDistanceMatrix(dm2, result.avgDist / getAverageDistance(dm2));

		// Forward
		Tree tree1 = new NeighborJoiningTree(dm1);
		RunFitch.saveData(wrkDir, 1, dm1, tree1);
		RunFitch.saveData(wrkDir, 2, dm2, tree1);

		// Backward
		if (result.passCount == TWO_PASS)
		{
			Tree tree2 = new NeighborJoiningTree(dm2);
			RunFitch.saveData(wrkDir, 3, dm2, tree2);
			RunFitch.saveData(wrkDir, 4, dm1, tree2);
		}
	}

	private void readFitchResults() throws Exception
	{
		fitch1 = RunFitch.readFitchResult(wrkDir, 1);
		fitch2 = RunFitch.readFitchResult(wrkDir, 2);
		if (result.passCount == TWO_PASS)
		{
			fitch3 = RunFitch.readFitchResult(wrkDir, 3);
			fitch4 = RunFitch.readFitchResult(wrkDir, 4);
		}
	}

	/*
	 * Calculates the DSS statistic.
	 */
	double calculateDSS() throws Exception
	{
	    
	    if(less4)
		return 0;
	    
		readFitchResults();

		// Scores for forward/backward
		double dss_f = 0, dss_b = 0;

		dss_f = getFitchSumOfSquares(fitch1, fitch2);
		if (result.passCount == TWO_PASS)
			dss_b = getFitchSumOfSquares(fitch3, fitch4);

		// Use the forward or the backward score?
		double dss = (dss_f > dss_b) ? dss_f : dss_b;

		return dss;
	}

	private double getFitchSumOfSquares(double ss1, double ss12)
			throws Exception
	{
		double ss = ss1 - ss12;
		if (ss < 0)
			ss *= -1;

		return ss;
	}

	private DistanceMatrix getDistance(SimpleAlignment window, int method)
	{
		switch (method)
		{
		case METHOD_JC:
			return TreeUtilities.getJukesCantorDistanceMatrix(window);

		case METHOD_F84:
		{
			return TreeUtilities.getMaximumLikelihoodDistanceMatrix(window,
					result.tRatio, result.alpha);
		}

		default:
			return null;
		}
	}

	/*
	 * Scales a DistanceMatrix by multiplying each element by the scaleBy value.
	 */
	private void scaleMulDistanceMatrix(DistanceMatrix dm, double scaleBy)
	{
		for (int i = 0; i < dm.getSize(); i++)
			for (int j = i; j < dm.getSize(); j++)
				dm.setDistance(i, j, (dm.getDistance(i, j) * scaleBy));
	}

	/**
	 * Removes sequences from the calculation which exceed the gap threshold
	 * @return true if there are less than 4 seq. left!
	 */
	private boolean removeGapSequences () {
		//Determine sequences which exceed gap treshold
		ArrayList<Integer> removeSeqPos = new ArrayList<Integer>(win1.getSequenceCount());
		for(int i=0; i<win1.getSequenceCount(); i++) {
			char[] seq = win1.getAlignedSequenceString(i).toCharArray();
			int count = 0;
			for(char c : seq) {
				if(c==Alignment.GAP)
					count++;
			}
			double gaps = ((double)count/(double)(seq.length));
			if(gaps>gapThreshold)
				removeSeqPos.add(new Integer(i));
		}
		
		for(int i=0; i<win2.getSequenceCount(); i++) {
			char[] seq = win2.getAlignedSequenceString(i).toCharArray();
			int count = 0;
			for(char c : seq) {
				if(c==Alignment.GAP)
					count++;
			}
			double gaps = ((double)count/(double)(seq.length));
			if(gaps>gapThreshold && !removeSeqPos.contains(new Integer(i)))
				removeSeqPos.add(new Integer(i));
		}
		
		//create new alignments without the bad sequences
		int newSize = win1.getSequenceCount()-removeSeqPos.size();
		if(newSize<4)
		    return true;
		
		Identifier[] ids1 = new Identifier[newSize];
		Identifier[] ids2 = new Identifier[newSize];
		String[] seqs1 = new String[newSize];
		String[] seqs2 = new String[newSize];
		for(int i=0, j=0; i<win1.getSequenceCount(); i++) {
			if(!removeSeqPos.contains(new Integer(i))) {
				ids1[j] = win1.getIdentifier(i);
				ids2[j] = win2.getIdentifier(i);
				seqs1[j] = win1.getAlignedSequenceString(i);
				seqs2[j] = win2.getAlignedSequenceString(i);
				j++;
			}
		}
		this.win1 = new SimpleAlignment(ids1, seqs1, win1.getDataType());
		this.win2 = new SimpleAlignment(ids2, seqs2, win1.getDataType());
		
		return false;
	}
	
	// Static helper methods

	/*
	 * Returns the average distance of the given SequenceSet alignment.
	 */
	public static double getAverageDistance(SequenceSet ss)
	{
		SimpleAlignment a = ss.getAlignment(true);
		JukesCantorDistanceMatrix dm = new JukesCantorDistanceMatrix(a);

		return getAverageDistance(dm);
	}

	/*
	 * Returns the average distance of the given DistanceMaxtix.
	 */
	public static double getAverageDistance(DistanceMatrix dm)
	{
		double sum = 0;
		for (int i = 0; i < dm.getSize(); i++)
			for (int j = i; j < dm.getSize(); j++)
				sum += dm.getDistance(i, j);

		// Return the average distance
		return sum /= ((dm.getSize() * dm.getSize()) / 2);
	}
}