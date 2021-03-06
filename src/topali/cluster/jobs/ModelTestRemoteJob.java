// (C) 2003-2007 Biomathematics & Statistics Scotland
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.cluster.jobs;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import topali.cluster.JobStatus;
import topali.data.*;
import topali.fileio.Castor;
import topali.gui.Tracker;
import topali.var.utils.*;

public class ModelTestRemoteJob extends RemoteJob
{

	Logger log = Logger.getLogger(this.getClass());

	private SequenceSet ss;

	public ModelTestRemoteJob(ModelTestResult result, AlignmentData data) {
		super("topali-mt", result);

		this.data = data;

		ss = data.getSequenceSet();
		// Split (3 runs) results need to have different data used
		if (result.splitType > 0)
			ss = SequenceSetUtils.getCodonPosSequenceSet(data, result.splitType, ss.getSelectedSequences());

		if (result.startTime == 0)
			result.startTime = System.currentTimeMillis();
	}


	public AnalysisResult ws_downloadResult() throws Exception
	{
		call = getCall();
		call.setOperationName(new QName("topali-mt", "getResult"));

		System.out.println("Result before download: " + ((ModelTestResult)result).splitType);

		String resultXML = (String) call.invoke(new Object[] { result.jobId });
		result = (ModelTestResult) Castor.unmarshall(resultXML);

		System.out.println("Result after  download: " + ((ModelTestResult)result).splitType);

		result.status = JobStatus.COMPLETING;
		return result;
	}


	public JobStatus ws_getProgress() throws Exception
	{
		call = getCall();
		call.setOperationName(new QName("topali-mt", "getPercentageComplete"));

		String statusXML = (String) call.invoke(new Object[] { result.jobId });
		JobStatus status = (JobStatus) Castor.unmarshall(statusXML);

		result.status = status.status;
		return status;
	}


	public String ws_submitJob() throws Exception
	{
		determineClusterURL("-parallel");

		call = getCall();
		call.setOperationName(new QName("topali-mt", "submit"));

		String alignmentXML = Castor.getXML(ss);
		String resultXML = Castor.getXML(result);

		result.jobId = (String) call.invoke(new Object[] { alignmentXML, resultXML });

		log.info("Job in progress: " + result.jobId);
		Tracker.log("SUBMITTED", "MT", result.jobId, url);

		result.status = JobStatus.QUEUING;
		return result.jobId;
	}

}
