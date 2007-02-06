package topali.cluster.jobs.cml.parser;

import java.util.LinkedList;
import java.util.List;

import topali.data.*;
import topali.cluster.jobs.cml.Models;

public abstract class CMLResultParser
{
	protected CodeMLModel model = new CodeMLModel();
	
	List<String> pss;
	
	public CMLResultParser() {
		pss = new LinkedList<String>();
	}

	public static CMLResultParser getParser(int model)
	{
		CMLResultParser parser = null;
		
		switch(model)
		{
			case Models.MODEL_M0: parser = new Model0Parser(); break;
			case Models.MODEL_M1: parser = new Model0Parser(); break;	// dummy parsers
			case Models.MODEL_M2: parser = new Model0Parser(); break;	// dummy parsers
			case Models.MODEL_M1a: parser = new Model1aParser(); break;
			case Models.MODEL_M2a: parser = new Model2aParser(); break;
			case Models.MODEL_M3: parser = new Model3Parser(); break;
			case Models.MODEL_M7: parser = new Model7Parser(); break;
			case Models.MODEL_M8: parser = new Model8Parser(); break;
		}
		
		if(parser==null)
			throw new RuntimeException("Unknown Model or Parser not implemented yet!");
				
		return parser;
	}
	
	public abstract void parse(String resultFile, String rstFile);

	public CodeMLModel getModelResult()
		{ return model; }
}