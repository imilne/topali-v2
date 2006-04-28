package topali.cluster.jobs;

import javax.xml.namespace.QName;

import topali.cluster.*;
import topali.data.*;
import topali.fileio.*;

public class PDM2RemoteJob extends RemoteJob
{
	private SequenceSet ss;	
		
	public PDM2RemoteJob(PDM2Result result, AlignmentData data)
	{
		super("topali-pdm2");
		
		this.result = result;
		this.data = data;
		this.ss = data.getSequenceSet();
		
		if (result.startTime == 0)
			result.startTime = System.currentTimeMillis();
	}
			
	public String ws_submitJob()
		throws Exception
	{
		call = getCall();			
		call.setOperationName(new QName("topali-pdm2", "submit"));
		
		String alignmentXML = Castor.getXML(ss);
		String resultXML = Castor.getXML(result);
	
		result.jobId = (String) call.invoke(
			new Object[] { alignmentXML, resultXML } );
		
		System.out.println("Job in progress: " + result.jobId);

		result.status = JobStatus.QUEUING;
		return result.jobId;
	}
	
	public float ws_getProgress()
		throws Exception
	{
		call = getCall();
		call.setOperationName(new QName("topali-pdm2", "getPercentageComplete"));
			
		String statusXML = (String) call.invoke(new Object[] { result.jobId } );
		JobStatus status = (JobStatus) Castor.unmarshall(statusXML);
			
		result.status = status.status;
		return status.progress;
	}
	
	public AnalysisResult ws_downloadResult()
		throws Exception
	{
		call = getCall();
		call.setOperationName(new QName("topali-pdm2", "getResult"));
		
		String resultXML = (String) call.invoke(new Object[] { result.jobId } );		
		result = (PDM2Result) Castor.unmarshall(resultXML);
		
		result.status = JobStatus.COMPLETING;
		return result;
	}
}