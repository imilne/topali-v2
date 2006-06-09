// (C) 2003-2006 Iain Milne
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.data;

import java.io.*;
import java.util.*;

/* Represents an Alignment and the results/analyses run upon it. */
public class AlignmentData
{
	// The alignment's name
	public String name;
			
	// And the data for it
	private SequenceSet sequenceSet;
	// This object can also be used to reference a list of alignments
	// (not held in memory)
	private LinkedList<AlignmentFileStat> refs = new LinkedList<AlignmentFileStat>();
	private boolean isReferenceList = false;
		
	// All the AnalysisResults created and associated with this alignment
	private LinkedList<AnalysisResult> results = new LinkedList<AnalysisResult>();
	// And the tracker to mark how many of each has been run
	private ResultsTracker tracker = new ResultsTracker();
		
	// A set of alignment annotations (partitions, coding regions, etc)
	private TOPALiAnnotations topaliAnnotations = new TOPALiAnnotations(1);

	
	public AlignmentData()
	{
	}
	
	public AlignmentData(String name, SequenceSet sequenceSet)
	{
		this.name = name;
		this.sequenceSet = sequenceSet;
		
		topaliAnnotations = new TOPALiAnnotations(sequenceSet.getLength());
	}
	
	public SequenceSet getSequenceSet()
		{ return sequenceSet; }
	
	public void setSequenceSet(SequenceSet sequenceSet)
		{ this.sequenceSet = sequenceSet; }
	
	public LinkedList<AlignmentFileStat> getReferences()
		{ return refs; }
	
	public void setReferences(LinkedList<AlignmentFileStat> refs)
		{ this.refs = refs; }
	
	public boolean isReferenceList()
		{ return isReferenceList; }
		
	public void setIsReferenceList(boolean isReferenceList)
		{ this.isReferenceList = isReferenceList; }
	
	public void setTopaliAnnotations(TOPALiAnnotations topaliAnnotations)
		{ this.topaliAnnotations = topaliAnnotations; }
	
	public TOPALiAnnotations getTopaliAnnotations()
		{ return topaliAnnotations; }
	
	public LinkedList<AnalysisResult> getResults()
		{ return results; }
	
	public void setResults(LinkedList<AnalysisResult> results)
		{ this.results = results; }
	
	
	public void replaceResult(AnalysisResult oldR, AnalysisResult newR)
	{
		int index = results.indexOf(oldR);
		
		System.out.println("Index is " + index);
		
		results.remove(index);
		results.add(index, newR);
	}
	
	public void removeResult(AnalysisResult result)
		{ results.remove(result); }
	
	public ResultsTracker getTracker()
		{ return tracker; }
	public void setTracker(ResultsTracker tracker)
		{ this.tracker = tracker; }
	
	public void addReference(String path, SequenceSet ss)
	{
		AlignmentFileStat stat = new AlignmentFileStat(path);
		
		stat.length = ss.getLength();
		stat.size = ss.getSize();
		stat.isDna = ss.isDNA();
		stat.fileSize = new File(path).length();
		
		refs.add(stat);		
	}
}