// (C) 2003-2007 Biomathematics & Statistics Scotland
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.cluster.jobs.pdm;

import java.io.*;
import java.util.logging.*;

import org.apache.axis.*;

import topali.cluster.*;
import topali.data.*;
import topali.fileio.*;

public class PDMWebService extends WebService
{
	public String submit(String alignmentXML, String resultXML)
		throws AxisFault
	{
		try
		{
			String jobId = getJobId();
			File jobDir = new File(getParameter("job-dir"), jobId);
						
			SequenceSet ss = (SequenceSet) Castor.unmarshall(alignmentXML);
			PDMResult result = (PDMResult) Castor.unmarshall(resultXML);
			
			result.bambePath = webappPath + "/binaries/src/bambe/bambe";
			result.treeDistPath = webappPath + "/binaries/src/treedist/treedist";
			result.tmpDir = getParameter("tmp-dir");
			result.jobId = jobId;
			
			// We put the starting of the job into its own thread so the web
			// service can return as soon as possible
			RunPDM pdm = new RunPDM(jobDir, ss, result);
			pdm.start();
			
			accessLog.info("PDM request from " + jobId);
			logger.info("PDM request from " + jobId);
			return jobId;
		}
		catch (Exception e)
		{
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw AxisFault.makeFault(e);
		}
	}
	
	protected JobStatus getPercentageComplete(File jobDir)
		throws AxisFault
	{
		try
		{
			return new CollatePDM(jobDir).getPercentageComplete();
		}
		catch (Exception e)
		{
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw AxisFault.makeFault(e);
		}
	}
	
	public String getResult(String jobId)
		throws AxisFault
	{
		try
		{
			File jobDir = new File(getParameter("job-dir"), jobId);
			
			PDMResult result = new CollatePDM(jobDir).getResult();
			
			logger.info(jobId + "- returning result");
			return Castor.getXML(result);
		}
		catch (Exception e)
		{
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw AxisFault.makeFault(e);
		}
	}

	/*
	 * Creates the script that each instance of a job running on the cluster
	 * calls to execute that job. In this case, a java command to run an
	 * PDMAnalysis on a given directory.
	 */
	static void runScript(File jobDir, PDMResult result)
		throws Exception
	{
		// Read...
		String template = ClusterUtils.readFile(new File(scriptsDir, "pdm.sh"));
		
		// Replace...
		template = template.replaceAll("\\$JAVA", javaPath);
		template = template.replaceAll("\\$TOPALi", topaliPath);
		template = template.replaceAll("\\$JOB_DIR", jobDir.getPath());
		template = template.replaceAll("\\$RUN_COUNT", "" + result.pdm_runs);
		
		// Write...
		writeFile(template, new File(jobDir, "pdm.sh"));
		
		// Run...
		logger.info(jobDir.getName() + " - submitting to cluster");
		submitJob("pdm.sh", jobDir);
	}
}