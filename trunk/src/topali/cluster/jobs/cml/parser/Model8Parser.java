package topali.cluster.jobs.cml.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Model8Parser extends CMLResultParser {

	@Override
	public void parse(String resultFile, String rstFile) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(resultFile));
			String line = null;
			while((line=in.readLine())!=null) {
				String a;
				String[] b;
				
				line = line.trim();
				
				if(line.startsWith("lnL(ntime:")) {
					a = line.substring(line.lastIndexOf(':')+1);
					a = a.trim();
					b = a.split("\\s+");
					model.likelihood = Float.parseFloat(b[0]);
					continue;
				}
				
				if(line.startsWith("p0=")) {
					b = line.split("\\s+");
					model.p0 = Float.parseFloat(b[1]);
					model.p = Float.parseFloat(b[3]);
					model.q = Float.parseFloat(b[5]);
					continue;
				}
				
				if(line.startsWith("(p1=")) {
					b = line.split("\\s+");
					model.p1 = Float.parseFloat(b[1].replaceAll("\\)", ""));
					model._w = Float.parseFloat(b[3]);
				}
			}
			
//			parse rst file
			in = new BufferedReader(new FileReader(rstFile));
			line = null;
			Pattern p = Pattern.compile("\\d+ \\w .+");
			while((line=in.readLine())!=null) {
				line = line.trim();
				Matcher m = p.matcher(line);
				if(m.matches()) {
					String[] tmp = line.split("\\s+");
					pss.add(tmp[0]+" "+tmp[1]+" "+tmp[tmp.length-1]);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
