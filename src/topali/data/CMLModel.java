// (C) 2003-2007 Biomathematics & Statistics Scotland
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.data;

import java.util.*;


/**
 * Holds all data of a CodeML model, predefined settings as well as the results after CodeML had been run.
 */
public class CMLModel 
{

	//Predefined models
	public final static String MODEL_M0 = "M0";
	public final static String MODEL_M1a = "M1a";
	public final static String MODEL_M2a = "M2a";
	public final static String MODEL_M1 = "M1";
	public final static String MODEL_M2 = "M2";
	public final static String MODEL_M3 = "M3";
	public final static String MODEL_M7 = "M7";
	public final static String MODEL_M8 = "M8";
	
	private static String nl = System.getProperty("line.separator");
	
	//Init Parameters
	public String model;
	public String name;
	public int nParameter;
	public boolean supportsPSS;
	public boolean fixedOmega;
	public Map<String, String> settings = new HashMap<String, String>();
	public Vector<Double> wStart = new Vector<Double>();
	
	//Result values
	public double p0 = -1, p1 = -1, p2 = -1;
	public double w0 = -1, w1 = -1, w2 = -1;
	public double p = -1, q = -1;
	public double likelihood = -1;
	public Collection<PSSite> pssList = new LinkedList<PSSite>(); 
	
	//Just for castor. Don't use this constructor!
	public CMLModel() {	
	}
	
	/**
	 * Instantiate a certain predefined model
	 * @param model
	 */
	public CMLModel(String model) {
		
		this.model = model;

		settings.put("seqtype","1");
		settings.put("CodonFreq","2");
		settings.put("model","0");
		settings.put("icode","0");
		settings.put("fix_kappa","0");
		settings.put("kappa","2");
		
		if(model.equals(MODEL_M0)) {
			name = "M0 (one-ratio)";
			nParameter = 1;
			supportsPSS = false;
			fixedOmega = false;
			wStart.add(5.0);
			settings.put("fix_omega","0");
			settings.put("NSsites","0");
		}
		else if(model.equals(MODEL_M1a)) {
			name = "M1a (NearlyNeutral)";
			nParameter = 2;
			supportsPSS = false;
			fixedOmega = false;
			wStart.add(5.0);
			settings.put("fix_omega","0");
			settings.put("NSsites","1");
		}
		else if(model.equals(MODEL_M2a)) {
			name = "M2a (PositiveSelection)";
			nParameter = 4;
			supportsPSS = true;
			fixedOmega = false;
			wStart.add(0.1);
			wStart.add(0.2);
			wStart.add(0.4);
			wStart.add(0.8);
			wStart.add(1.6);
			wStart.add(3.2);
			settings.put("fix_omega","0");
			settings.put("NSsites","2");
		}
		else if(model.equals(MODEL_M1)) {
			name = "M1 (NearlyNeutral)";
			nParameter = 1;
			supportsPSS = false;
			fixedOmega = true;
			settings.put("fix_omega","1");
			settings.put("omega","1");
			settings.put("NSsites","1");
		}
		else if(model.equals(MODEL_M2)) {
			name = "M2 (PositiveSelection)";
			nParameter = 3;
			supportsPSS = true;
			fixedOmega = true;
			settings.put("fix_omega","1");
			settings.put("omega","1");
			settings.put("NSsites","2");
		}
		else if(model.equals(MODEL_M3)) {
			name = "M3 (discrete (3 categories))";
			nParameter = 5;
			supportsPSS = true;
			fixedOmega = false;
			wStart.add(5.0);
			settings.put("fix_omega","0");
			settings.put("NSsites","3");
			settings.put("ncatG","3");
		}
		else if(model.equals(MODEL_M7)) {
			name = "M7 (beta (10 categories))";
			nParameter = 2;
			supportsPSS = false;
			fixedOmega = false;
			wStart.add(5.0);
			settings.put("fix_omega","0");
			settings.put("NSsites","7");
			settings.put("ncatG","10");
		}
		else if(model.equals(MODEL_M8)) {
			name = "M8 (beta&w>1 (11 categories))";
			nParameter = 4;
			supportsPSS = true;
			fixedOmega = false;
			wStart.add(0.1);
			wStart.add(0.2);
			wStart.add(0.4);
			wStart.add(0.8);
			wStart.add(1.6);
			wStart.add(3.2);
			settings.put("fix_omega","0");
			settings.put("NSsites","8");
			settings.put("ncatG","10");
		}
	}
	
	/**
	 * Use this method, if start omega is not fixed, and there are
	 * different start omega values.
	 * @return A list of models, each with a certain start omage value
	 */
	public List<CMLModel> generateModels() {
		List<CMLModel> res = new ArrayList<CMLModel>();
		if(fixedOmega) {
			res.add(this);
			return res;
		}
		
		for(double d : wStart) {
			CMLModel m = new CMLModel(model);
			m.wStart = this.wStart;
			m.settings.put("omega", String.valueOf(d));
			res.add(m);
		}
		
		return res;
	}
	
	/**
	 * Get the settings as formatted string (to build codeml command line arguments)
	 * @return
	 */
	public String codemlSettings() {
		StringBuffer sb = new StringBuffer();
		sb.append("seqfile = seq.phy" + nl);
		sb.append("treefile = tree.txt" + nl);
		sb.append("outfile = results.txt" + nl);
		sb.append("noisy = 9" + nl);
		sb.append("verbose = 0" + nl);
		sb.append("runmode = 0" + nl);
		
		Set<String> keys = settings.keySet();
		for(String key : keys) {
			sb.append(key+" = "+settings.get(key)+nl);
		}
		
		return sb.toString();
	}

	/**
	 * Get a list of positive selected sites with p > minP
	 * @param minP
	 * @return List of PSSite, or null if this model doesn't support p. s. sites.
	 */
	public List<PSSite> getPSS(double minP)
	{
		List<PSSite> res = new LinkedList<PSSite>();
		for (PSSite ps : pssList)
		{
			if (ps.getP() > minP)
				res.add(ps);
		}
		return res;
	}
	
	public float[][] getGraph() {
		if(supportsPSS) {
			List<PSSite> sites = getPSS(0);
			float[][] result = new float[sites.size()][3];
			for(int i=0; i<sites.size(); i++) {
				PSSite s = sites.get(i);
				result[i][0] = s.pos;
				result[i][1] = (float)s.p;
				result[i][2] = s.aa;
			}
			return result;
		}
		return null;
	}

//	/**
//	 * Constructs a <code>String</code> with all attributes
//	 * in name = value format.
//	 *
//	 * @return a <code>String</code> representation 
//	 * of this object.
//	 */
//	public String toString()
//	{
//	    final String TAB = "    ";
//	    
//	    String retValue = "";
//	    
//	    retValue = "CMLModel ( "
//	        + super.toString() + TAB
//	        + "model = " + this.model + TAB
//	        + "name = " + this.name + TAB
//	        + "nParameter = " + this.nParameter + TAB
//	        + "supportsPSS = " + this.supportsPSS + TAB
//	        + "fixedOmega = " + this.fixedOmega + TAB
//	        + "settings = " + this.settings + TAB
//	        + "wStart = " + this.wStart + TAB
//	        + "p0 = " + this.p0 + TAB
//	        + "p1 = " + this.p1 + TAB
//	        + "p2 = " + this.p2 + TAB
//	        + "w0 = " + this.w0 + TAB
//	        + "w1 = " + this.w1 + TAB
//	        + "w2 = " + this.w2 + TAB
//	        + "p = " + this.p + TAB
//	        + "q = " + this.q + TAB
//	        + "likelihood = " + this.likelihood + TAB
//	        + "pssList = " + this.pssList + TAB
//	        + " )";
//	
//	    return retValue;
//	}
	
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Model: "+name+"\n");
		sb.append("Settings: \n");
		Set<String> keys = settings.keySet();
		for(String s : keys) {
			sb.append(s+" = "+settings.get(s)+"\n");
		}
		return sb.toString();
	}
	
	
}