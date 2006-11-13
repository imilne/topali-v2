// (C) 2003-2006 Iain Milne
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.cluster.cml;

import java.io.*;
import java.util.logging.*;

import topali.cluster.*;
import topali.data.*;
import topali.fileio.*;
import topali.mod.*;

/**
 * Initializer class for a codeml postitive selection run. An instance of this
 * class must save a copy of the alignment to disk where it can be read by the
 * subjobs, then start a selection of subjobs, each job running a different
 * codeml model on the data.
 */
public class CodeMLInitializer extends Thread
{
	private static Logger logger = Logger.getLogger("topali.cluster");
	
	private SequenceSet ss;
	private CodeMLResult result;
	
	// Directory where the job will run
	private File jobDir;
		
	public CodeMLInitializer(File jobDir, SequenceSet ss, CodeMLResult result)
	{
		this.jobDir = jobDir;
		this.ss = ss;
		this.result = result;
	}

	public void run()
	{
		try { runAnalysis(); }
		catch (Exception e) {
			ClusterUtils.writeError(new File(jobDir, "error.txt"), e);
		}
	}
	
	private void runAnalysis()
		throws Exception
	{
		// Ensure the directory for this job exists
		jobDir.mkdirs();
		
		// Store the CodeMLResult object where it can be read by the sub-job
		Castor.saveXML(result, new File(jobDir, "submit.xml"));
		
		// Sequences that should be selected/saved for processing
		int[] indices = ss.getIndicesFromNames(result.selectedSeqs);
		// Store the sequence data in phylip sequential
		ss.save(new File(jobDir, "seq.phy"), indices, Filters.PHY_S, false);
		
		
		// We want to run each of the models, making *8* runs in total
		for (int i = 1; i <= 8; i++)
		{
			if (LocalJobs.isRunning(result.jobId) == false)
				return;
			
			File runDir = new File(jobDir, "run" + i);
			runDir.mkdirs();
							
			if (result.isRemote == false)
				new CodeMLAnalysis(runDir).startThread(LocalJobs.manager);
		}
				
		if (result.isRemote)
		{
			logger.info("analysis ready: submitting to cluster");
			CodeMLWebService.runScript(jobDir);
		}
	}
}