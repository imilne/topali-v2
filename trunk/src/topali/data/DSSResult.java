// (C) 2003-2006 Iain Milne
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.data;

import sbrn.commons.*;

/*
 * Class that stores both the results from running a DSS analysis and the
 * settings required to make the run (although not the data itself).
 */
public class DSSResult extends AlignmentResult
{
	// The location of the Fitch binary
	public String fitchPath;
		
	// The method (JC, F84, etc) - see DSS.java for details
	public int method;
	
	// The power (weighted or unweighted) - see DSS.java for details
	public int power;
	
	// Step size and window size
	public int window;
	public int step;
	
	// One pass or two
	public int passCount;
	
	// Number of DSS runs (including bootstraps)
	public int runs;
	
	// These properties relate to the alignment being analysed
	public double avgDist, tRatio, alpha;
	
	
	// The DSS graph (x and y values)
	public float[][] data;
	
	// Bootstrap information (maximum y found for each run)
	public float[] thresholds;
	// And current threshold cutoff point
	public float thresholdCutoff = 0.95f;
	
	public DSSResult()
	{
	}
	
	// Castor conversion methods so that the 2D data array is saved as a string
	public String getData()
		{ return MatrixXML.arrayToString(data); }
	
	public void setData(String str)
		{ data = MatrixXML.stringTo2DFloatArray(str); }
}