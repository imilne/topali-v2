// (C) 2003-2007 Biomathematics & Statistics Scotland
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.data.models;

import java.util.*;

import topali.data.DataObject;
import topali.var.MathUtils;

public abstract class Model extends DataObject implements Comparable<Model>
{
	String name = "";
	List<String> aliases = new LinkedList<String>();
	boolean gamma = false;
	boolean inv = false;
	
	double lnl = 0;
	
	//alpha shape parameter for gamma distribution
	double alpha;
	//no. of categories (gamma distribution)
	int gammaCat;
	//proportino of invariant sites
	double invProp;
	
	public Model() {
		super();
	}

	public Model(int id) {
		super(id);
	}
	
	/**
	 * Copy constructor
	 * @param m
	 */
	public Model(Model m) {
		super();
		this.name = new String(m.name);
		for(String s : m.aliases)
			this.aliases.add(new String(s));
		this.gamma = m.gamma;
		this.inv = m.inv;
	}
	
	/**
	 * Each model should return a score like this:
	 * min : least complex DNAModel (or oldest ProteinModel)
	 * max : most complex DNAModel (or most recent ProteinModel)
	 * @return
	 */
	public abstract int getRankingScore();

	/**
	 * Number of free parameters this model has
	 * @return
	 */
	public abstract int getFreeParameters();
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void addAlias(String alias) {
		this.aliases.add(alias);
	}
	
	public List<String> getAliases() {
		return aliases;
	}
	
	public boolean isGamma()
	{
		return gamma;
	}

	public void isGamma(boolean gamma)
	{
		this.gamma = gamma;
	}

	public boolean isInv()
	{
		return inv;
	}

	public void isInv(boolean inv)
	{
		this.inv = inv;
	}

	public double getLnl()
	{
		return lnl;
	}

	public void setLnl(double lnl)
	{
		this.lnl = lnl;
	}

	public double getAlpha()
	{
		return alpha;
	}

	public void setAlpha(double alpha)
	{
		this.alpha = alpha;
	}

	public int getGammaCat()
	{
		return gammaCat;
	}

	public void setGammaCat(int gammaCat)
	{
		this.gammaCat = gammaCat;
	}

	public double getInvProp()
	{
		return invProp;
	}

	public void setInvProp(double invProp)
	{
		this.invProp = invProp;
	}

	public boolean is(String name) {
		name = name.toLowerCase();
		if(this.name.toLowerCase().equals(name))
			return true;
		for(String alias : aliases) {
			if(alias.toLowerCase().equals(name))
				return true;
		}
		return false;
	}
	
	public boolean matches(Model mod) {
		return (mod.getName().equals(this.getName()) && mod.isGamma()==this.isGamma() && mod.isInv()==this.isInv());
	}
	
	@Override
	public int compareTo(Model o)
	{
		if(o.getRankingScore()>this.getRankingScore())
			return 1;
		else if(o.getRankingScore()<this.getRankingScore())
			return -1;
		else 
			return 0;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getName()+" (");
		for(String s : this.aliases)
			sb.append(s+" ");
		sb.append(")\n");
		sb.append("Gamma: "+this.gamma+"\n");
		sb.append("  alpha="+this.alpha+"\n");
		sb.append("      n="+this.gammaCat+"\n");
		sb.append("Inv: "+this.inv+"\n");
		sb.append("      p="+this.invProp+"\n");
		sb.append("Lnl: "+this.lnl+"\n");
		sb.append("Ranking score: "+getRankingScore()+"\n");
		return sb.toString();
	}
	
}
