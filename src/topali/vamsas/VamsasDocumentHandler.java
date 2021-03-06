// (C) 2003-2007 Biomathematics & Statistics Scotland
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.vamsas;

import java.io.IOException;
import java.util.*;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import topali.analyses.MakeNA;
import topali.cluster.JobStatus;
import topali.data.*;
import topali.data.Sequence;
import topali.fileio.AlignmentLoadException;
import topali.gui.*;
import uk.ac.vamsas.client.IClientDocument;
import uk.ac.vamsas.objects.core.*;
import uk.ac.vamsas.objects.core.Map;
import uk.ac.vamsas.objects.utils.SymbolDictionary;

public class VamsasDocumentHandler
{
	 Logger log = Logger.getLogger(this.getClass());

	private Project project;

	private IClientDocument doc;

	ObjectMapper map;

	// These fields are dynamically set during reading/writing process (be
	// carefull!)
	Alignment currentVAlignment = null;

	AlignmentData currentTAlignment = null;

	// tmp stuff for cdnas
	LinkedList<AlignmentData> cdnaDatasets = new LinkedList<AlignmentData>();

	SequenceSet cdnaSS = new SequenceSet();

	public VamsasDocumentHandler(VamsasManager vman, IClientDocument doc) throws IOException
	{
		this.project = vman.project;
		this.doc = doc;
		this.map = vman.mapper;
		this.map.registerClientDocument(doc);
	}

	public void read()
	{
		Project tmp = new Project();

		for (VAMSAS vamsas : doc.getVamsasRoots())
			for (DataSet dataset : vamsas.getDataSet())
				for (Alignment alignment : dataset.getAlignment()) {
					AlignmentData d = readAlignment(alignment);
					tmp.addDataSet(d);
				}

		for (AlignmentData cdnas : cdnaDatasets)
			if (tmp.containsDatasetBySeqs(cdnas)==null)
			{
				int option = JOptionPane
						.showConfirmDialog(
								null,
								"Found DNA with a corresponding protein alignment\n("+cdnas.getName()+").\n\nCreate a protein guided DNA alignment?",
								"Guided alignment", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (option == JOptionPane.YES_OPTION) {
					tmp.addDataSet(cdnas);
					ProjectState.setDataChanged();
				}
			}
		cdnaDatasets.clear();

		this.project.merge(tmp);
	}

	public void write()
	{
		for (AlignmentData align : project.getDatasets())
		{
			currentTAlignment = align;
			writeAlignment();
		}

		//clean();
	}

	public void clean() {
		HashSet<Object> tobjs = new HashSet<Object>();
		for(AlignmentData data : this.project.getDatasets()) {
			tobjs.add(data);
			for(AnalysisResult res : data.getResults()) {
				tobjs.add(res);
			}
		}

		for (VAMSAS vamsas : doc.getVamsasRoots())
			for (DataSet dataset : vamsas.getDataSet())
				for (Alignment alignment : dataset.getAlignment()) {
					if(!tobjs.contains(alignment)) {
						dataset.removeAlignment(alignment);
					}
					else {
						for(AlignmentAnnotation anno : alignment.getAlignmentAnnotation()) {
							if(!tobjs.contains(anno))
								alignment.removeAlignmentAnnotation(anno);
						}
					}
				}
	}

	private AlignmentData readAlignment(Alignment vAlign)
	{
		int id = readTID(vAlign);
		AlignmentData tAlign = (id>-1) ? new AlignmentData(id) : new AlignmentData();
		String alignName = "VAMSAS alignment";
		Property[] props = vAlign.getProperty();
		for (Property prop : props)
			if (prop.getName().endsWith("itle"))
			{
				alignName = prop.getContent();
				break;
			}
		tAlign.setName(alignName);

		SequenceSet ss = new SequenceSet();
		ss.setOverview("");
		for (AlignmentSequence vSeq : vAlign.getAlignmentSequence())
		{
			Sequence tSeq = readSequence(vSeq);
			ss.addSequence(tSeq);
			map.registerObjects(tSeq, vSeq);
		}
		try
		{
			ss.checkValidity(true);
		} catch (AlignmentLoadException e)
		{
			log.warn("Alignment '" + tAlign.getName() + "' is not valid!", e);
			return null;
		}

		tAlign.setSequenceSet(ss);

		if (cdnaSS.getSize() > 0)
		{
			String name = getAlignmentName(vAlign);
			MakeNA mna = new MakeNA(cdnaSS, ss, name + " (cDNA)");
			log.info("Generating protein guided alignment for " + name);
			if (mna.doConversion(false, map.linkedObjects))
			{
				AlignmentData cdnaData = mna.getAlignmentData();
				for (Sequence s : cdnaData.getSequenceSet().getSequences())
					s.setName(s.getName() + "_cDNA");
				cdnaDatasets.add(cdnaData);
			} else
				log.warn("Could not create guided alignment!");
			cdnaSS = new SequenceSet();
		}

		readResults(vAlign);

		map.registerObjects(tAlign, vAlign);
		return tAlign;
	}

	private Sequence readSequence(AlignmentSequence vSeq)
	{
		log.info("Creating new topali sequence for "+vSeq);
		Object tmp = vSeq.getRefid();
		uk.ac.vamsas.objects.core.Sequence refSeq = null;
		if (tmp != null)
		{
			refSeq = (uk.ac.vamsas.objects.core.Sequence) tmp;
			// check the referenced dataset sequence, if we deal with a
			// protein sequence.
			if (refSeq.getDictionary().equals(SymbolDictionary.STANDARD_AA))
				// if so, check if we have a corresponding cdna for it
				readCDNA(refSeq, vSeq);
		}

		String seq = vSeq.getSequence().replaceAll("\\W", "-");
		String name = vSeq.getName().replaceAll("\\s+", "_");
		int id = readTID(vSeq);
		Sequence tSeq = (id>-1) ? new Sequence(id) : new Sequence();
		tSeq.setName(name);
		tSeq.setSequence(seq);

		// if the sequence is associated with a DatasetSequence, take a note of
		// that
		if (refSeq != null)
			map.linkedObjects.put(tSeq, refSeq);

		map.registerObjects(tSeq, vSeq);
		return tSeq;
	}

	private void readResults(Alignment vAlign) {
		for(AlignmentAnnotation vAnno : vAlign.getAlignmentAnnotation()) {
			int tid = readTID(vAnno);
			if(tid>-1) {
				map.registerObjects(new Integer(tid), vAnno);
			}
		}
		for(Tree vTree : vAlign.getTree()){
			int tid = readTID(vTree);
			if(tid>-1) {
				map.registerObjects(new Integer(tid), vTree);
			}
		}
	}

	private void writeAlignment()
	{
		//Check if this alignment already exists in the vamsas doc
		currentVAlignment = (Alignment) map.getVamsasObject(currentTAlignment);

		// create new Alignment
		if (currentVAlignment == null)
		{
			currentVAlignment = new Alignment();
			currentVAlignment.setGapChar("-");
			currentVAlignment.setProvenance(getProvenance(null));
			Property title = new Property();
			title.setName("title");
			title.setType("string");
			title.setContent(currentTAlignment.getName());
			currentVAlignment.addProperty(title);
			currentVAlignment.addProperty(createTIDProp(currentTAlignment));
			for (Sequence seq : currentTAlignment.getSequenceSet()
					.getSequences())
				writeSequence(seq);

			for (AnalysisResult res : currentTAlignment.getResults())
				writeResult(res);

			getDataset().addAlignment(currentVAlignment);
			map.registerObjects(currentTAlignment, currentVAlignment);
		}

		// Alignment already exists
		else
		{
			int tid = readTID(currentVAlignment);
			if(tid<0) {
				currentVAlignment.addProperty(createTIDProp(currentTAlignment));
			}

			//just modifiy sequences if alignment is not locked
			if(currentVAlignment.getModifiable()==null)
			{
				for (Sequence seq : currentTAlignment.getSequenceSet().getSequences())
					writeSequence(seq);
			}

			for (AnalysisResult res : currentTAlignment.getResults())
				writeResult(res);

			map.registerObjects(currentTAlignment, currentVAlignment);
		}
	}

	private void writeSequence(Sequence seq)
	{

		AlignmentSequence valSeq = (AlignmentSequence) map.getVamsasObject(seq);

		if (valSeq == null)
		{
			log.info("Creating new vamsas sequence for "+seq);
			// If neccessary create a new DatasetSequence
			uk.ac.vamsas.objects.core.Sequence vdsSeq = null;
			for(Object tmp : map.linkedObjects.get(seq)) {
				if(tmp instanceof uk.ac.vamsas.objects.core.Sequence) {
					vdsSeq = (uk.ac.vamsas.objects.core.Sequence)tmp;
					break;
				}
			}
			if (vdsSeq == null)
			{
				log.info("No DatasetSequence found, creating a new one.");
				vdsSeq = new uk.ac.vamsas.objects.core.Sequence();
				vdsSeq.setName(seq.getName());
				vdsSeq.setSequence(seq.getSequence().replaceAll("\\W", ""));
				vdsSeq.setStart(1);
				vdsSeq.setEnd(vdsSeq.getSequence().length());
				if (currentTAlignment.getSequenceSet().getProps().isNucleotides())
					vdsSeq.setDictionary(SymbolDictionary.STANDARD_NA);
				else
					vdsSeq.setDictionary(SymbolDictionary.STANDARD_AA);
				vdsSeq.addProperty(createTIDProp(seq));
				getDataset().addSequence(vdsSeq);
				map.registerObjects(seq, vdsSeq);
				map.linkedObjects.put(seq, vdsSeq);
			} else
			{
				log.info("DatasetSequence for this AlignmentSequence exists.");
			}

			// Create the alignment sequence
			valSeq = new AlignmentSequence();
			valSeq.setName(seq.getName());
			valSeq.setSequence(seq.getSequence());
			valSeq.setStart(1);
			valSeq.setEnd(valSeq.getSequence().length());
			valSeq.setRefid(vdsSeq);
			valSeq.addProperty(createTIDProp(seq));
			map.registerObjects(seq, valSeq);
			currentVAlignment.addAlignmentSequence(valSeq);

			// Check if there is a linked sequence
			if (map.linkedObjects.contains(seq))
			{
				LinkedList<Object> linked = map.linkedObjects.getAll(seq);
				Sequence sequence = null;
				//one mapping for seq. -> dna seq. and one for seq. -> protein seq.
				SequenceMapping map1 = null;
				SequenceMapping map2 = null;

				for (Object o : linked)
					if (o instanceof Sequence)
						sequence = (Sequence) o;
					else if ((o instanceof SequenceMapping) && (map1 == null))
						map1 = (SequenceMapping) o;
					else if ((o instanceof SequenceMapping) && (map2 == null))
						map2 = (SequenceMapping) o;

				if (sequence != null)
				{
					if (map1 != null)
					{
						map1.setLoc(vdsSeq);
						getDataset().addSequenceMapping(map1);
					}
					if (map2 != null)
					{
						map2.setLoc(vdsSeq);
						getDataset().addSequenceMapping(map2);
					}
					map.linkedObjects.remove(seq);
				}
			}
		} else
		{
			int tid = readTID(valSeq);
			if(tid<0) {
				valSeq.addProperty(createTIDProp(seq));
			}

			log.info("Vamsas sequence already exists. Updating sequence "+valSeq);
			valSeq.setName(seq.getName());
		}
	}

	private void writeResult(AnalysisResult res)
	{

		if(res.status!=JobStatus.COMPLETED)
			return;

		Object tmp = map.getVamsasObject(new Integer(res.getID()));
		if (tmp == null)
		{
			log.info("Creating new vamsas annotation for "+res);

			if(res instanceof TreeResult) {
				TreeResult tree = (TreeResult) res;
				Tree vTree = VAMSASUtils.createVamsasTree(tree, currentTAlignment, map);
				vTree.addProperty(createTIDProp(res));
				currentVAlignment.addTree(vTree);
				map.registerObjects(new Integer(res.getID()), vTree);
			}

			else {
				LinkedList<AlignmentAnnotation> annos = VAMSASUtils.createAlignmentAnnotation(res, currentTAlignment);
				for(AlignmentAnnotation anno : annos) {
					currentVAlignment.addAlignmentAnnotation(anno);
					map.registerObjects(new Integer(res.getID()), anno);
				}
			}

		}

		//if there are results based on this alignment, lock it
		if(currentVAlignment.getModifiable()==null)
			currentVAlignment.setModifiable(VamsasManager.client.getClientUrn());
	}

	private void readCDNA(uk.ac.vamsas.objects.core.Sequence dsProtSeq,
			AlignmentSequence protSeq)
	{
		Sequence tSeq = null;

		log.info("Try to get cdna for " + dsProtSeq);

		for (DataSet dataset : doc.getVamsasRoots()[0].getDataSet())
		{
			// either find corresponding DNA dbref
			boolean found = false;
			for (DbRef dbref : dsProtSeq.getDbRef())
			{
				for (uk.ac.vamsas.objects.core.Sequence dnaSeq : dataset
						.getSequence())
				{
					if (!dnaSeq.getDictionary().equals(
							SymbolDictionary.STANDARD_NA))
						continue;
					for (DbRef dnadbref : dnaSeq.getDbRef())
						// compare the dbref with dnadbref
						if (compareDBRef(dnadbref, dbref))
						{

							if (dnadbref.getMap().length < 1)
								continue;

							Map map = dnadbref.getMap()[0];
							Seg[] cDnaSegs = map.getLocal().getSeg(); // correct if dnadbref is a protein db reference
							if (dbref.getMapCount()>=1) {
							  // check to see if we are actually dealing with a dna accession reference
							  if (map.getLocal().getUnit()==map.getMapped().getUnit())
							  {
							    // this is probably a dna-dna mapping.
							    // look at the protein reference to see if it has a mapping instead
							    if (dbref.getMapCount()<1)
							    {
							      // no map on the protein, either.
							      continue;
							    }
							    Map pmap = dbref.getMap(0);
							    if (pmap.getLocal().getUnit()==1 && pmap.getMapped().getUnit()==3)
							    {
							      int boundp[] = uk.ac.vamsas.objects.utils.Range.getBounds(pmap.getMapped());
							      int boundd[] = uk.ac.vamsas.objects.utils.Range.getBounds(map.getMapped());
							      // 1. verify that protein coding region is covered by the coverage of the dna seuqence reference
							      if (boundp[0]<boundd[0] || boundp[1]>boundd[1])
							      {
							        // exon boundaries are not covered by the dna sequence.
							        continue;
							      }
							      // 2. construct map from local p to pmap.getMapped() intervals via map.getMapped() to map.getLocal() range.
							      cDnaSegs = pmap.getMapped().getSeg();
							      uk.ac.vamsas.objects.utils.MapList remap = uk.ac.vamsas.objects.utils.Range.parsemapType(map);
							      ArrayList<Seg> vSeg = new ArrayList<Seg>();
							      for (Seg pseg : pmap.getMapped().getSeg()) {
							        int rng[] = remap.locateInFrom(pseg.getStart(), pseg.getEnd());
							        if (rng==null)
							        {
							          // can't map this range - so we give up
							          continue;
							        }
							        for (int p=0;p<rng.length;p+=2)
							        {
							          Seg nseg = new Seg();
							          nseg.setStart(rng[p]);
							          nseg.setEnd(rng[p+1]);
							          nseg.setInclusive(true);
							          vSeg.add(nseg);
							        }
							      }
							      // 3. Make the Seg array for the dna sequence
							      vSeg.toArray(cDnaSegs = new Seg[vSeg.size()]);
							    }
							  }
							}
							int start;
							int end;
							String complSeq = dnaSeq.getSequence().replaceAll(
									"\\W", "-");
							StringBuffer concatSeq = new StringBuffer();

							SequenceMapping dnaMapping = new SequenceMapping();
							dnaMapping.setMap(dnaSeq);
							Mapped dnaMap = new Mapped();
							dnaMap.setUnit(3);
							dnaMapping
									.setProvenance(getProvenance("Protein guided cDNA alignment"));

							for (Seg seg : cDnaSegs)
							{
								start = seg.getStart() - 1;
								end = seg.getEnd() - 3;
								concatSeq
										.append(complSeq.substring(start, end));
								Seg segm = new Seg();
								segm.setInclusive(true);
								segm.setStart(seg.getStart());
								segm.setEnd(seg.getEnd());
								dnaMap.addSeg(segm);
							}
							dnaMapping.setMapped(dnaMap);
							Local dnaLoc = new Local();
							dnaLoc.setUnit(3);
							Seg segm = new Seg();
							segm.setInclusive(true);
							segm.setStart(1);
							segm.setEnd(concatSeq.length());
							dnaLoc.addSeg(segm);
							dnaMapping.setLocal(dnaLoc);

							SequenceMapping protMapping = new SequenceMapping();
							protMapping.setMap(dsProtSeq);
							Local protLoc = new Local();
							protLoc.setUnit(3);
							protMapping
									.setProvenance(getProvenance("Protein guided cDNA alignment"));
							segm = new Seg();
							segm.setInclusive(true);
							segm.setStart(1);
							segm.setEnd(concatSeq.length());
							protLoc.addSeg(segm);
							protMapping.setLocal(protLoc);
							Mapped protMap = new Mapped();
							protMap.setUnit(1);
							segm = new Seg();
							segm.setInclusive(true);
							segm.setStart(1);
							segm.setEnd(dsProtSeq.getSequence().length());
							protMap.addSeg(segm);
							protMapping.setMapped(protMap);

							if (concatSeq.length() > 0)
							{
								tSeq = new Sequence();
								tSeq.setSequence(concatSeq.toString());
								tSeq.setName(protSeq.getName().replaceAll(
										"\\s+", "_"));

								this.map.linkedObjects.put(tSeq, dnaMapping);
								this.map.linkedObjects.put(tSeq, protMapping);
								found = true;
								log.info("Found matching dbrefs: "
										+ dnaSeq.getName());
								break;
							}
						}

					if (found)
						break;
				}

				if (found)
					break;
			}

			if (found)
				break;

			if (!found)
				// or search sequence mapping
				for (SequenceMapping smap : dataset.getSequenceMapping())
				{
					uk.ac.vamsas.objects.core.Sequence map = (uk.ac.vamsas.objects.core.Sequence) smap
							.getMap();
					if (map.equals(dsProtSeq))
					{
						log.info("Found matching sequence mapping: "
								+ dsProtSeq);
						uk.ac.vamsas.objects.core.Sequence dna = (uk.ac.vamsas.objects.core.Sequence) smap
								.getLoc();
						if (smap.getLocal().getSegCount()>1)
						{
						  log.warn("Skipping matching sequence mapping which has more than one contig.");
						  continue;
						}
						int s1 = (int) dna.getStart();
						// TODO: extend to cope with mappings with more than one contig.
						int s2 = smap.getLocal().getSeg()[0].getStart();
						// int e1 = (int)dna.getEnd();
						int e2 = smap.getLocal().getSeg()[0].getEnd();
						int start = s2 - s1;
						int end = start + (e2 - s2) + 1;
						String seqString = dna.getSequence().replaceAll("\\W",
								"-");
						String sequence = seqString.substring(start, end);
						tSeq = new Sequence();
						tSeq.setSequence(sequence);
						tSeq.setName(protSeq.getName().replaceAll("\\s+", "_"));
						found = true;
					}

					if (found)
						break;
				}
		}

		// if a cdna was found, add it to the sequenceset
		if (tSeq != null)
			cdnaSS.addSequence(tSeq);
	}

	private boolean compareDBRef(DbRef ref1, DbRef ref2)
	{
		if ((ref1 == null) || (ref2 == null))
			return false;
		else
			return (ref1.getSource().equals(ref2.getSource()) && ref1
					.getAccessionId().equals(ref2.getAccessionId()));
	}

	private String getAlignmentName(Alignment vAlign)
	{
		Property[] props = vAlign.getProperty();
		for (Property prop : props)
			if (prop.getName().endsWith("itle"))
				return prop.getContent();
		return "Vamsas";
	}

	private DataSet getDataset()
	{
		for (VAMSAS vamsas : doc.getVamsasRoots())
			for (DataSet ds : vamsas.getDataSet())
				return ds;

		DataSet ds = new DataSet();
		ds.setProvenance(getProvenance(null));
		doc.getVamsasRoots()[0].addDataSet(ds);
		return ds;
	}

	private Provenance getProvenance(String action)
	{
		Provenance p = new Provenance();
		p.addEntry(getEntry(action));

		return p;
	}

	private Entry getEntry(String action)
	{
		Entry e = new Entry();

		e.setApp(VamsasManager.client.getClientUrn());
		e.setUser(VamsasManager.user.getFullName());
		e.setDate(new Date());

		if (action != null)
			e.setAction(action);
		else
			e.setAction("created");

		return e;
	}

	private Property createTIDProp(DataObject tObj) {
		Property tid = new Property();
		tid.setName("topaliID");
		tid.setType("int");
		tid.setContent(""+tObj.getID());
		return tid;
	}

	private int readTID(Object vObj) {
		Property prop = null;
		if(vObj instanceof Alignment) {
			Alignment a = (Alignment)vObj;
			for(Property p : a.getProperty()) {
				if(p.getName().equals("topaliID")) {
					prop = p;
					break;
				}
			}
		}
		else if(vObj instanceof AlignmentSequence) {
			AlignmentSequence a = (AlignmentSequence)vObj;
			for(Property p : a.getProperty()) {
				if(p.getName().equals("topaliID")) {
					prop = p;
					break;
				}
			}
		}
		else if(vObj instanceof AlignmentAnnotation) {
			AlignmentAnnotation a = (AlignmentAnnotation)vObj;
			for(Property p : a.getProperty()) {
				if(p.getName().equals("topaliID")) {
					prop = p;
					break;
				}
			}
		}
		else if(vObj instanceof Tree) {
			Tree a = (Tree)vObj;
			for(Property p : a.getProperty()) {
				if(p.getName().equals("topaliID")) {
					prop = p;
					break;
				}
			}
		}

		if(prop!=null) {
			return Integer.parseInt(prop.getContent());
		}
		return -1;
	}
}
