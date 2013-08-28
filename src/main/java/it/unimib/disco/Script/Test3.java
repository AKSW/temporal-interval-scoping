package it.unimib.disco.Script;

import it.unimib.disco.FactExtractor.DateOccurrence;
import it.unimib.disco.ReadFiles.ReadFiles;
import it.unimib.disco.TemporalIntervalCreator.MatrixCreator;
import it.unimib.disco.TemporalIntervalCreator.NormalizationSelection;
import it.unimib.disco.TemporalIntervalCreator.TemporalIntervalFactAssociator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.aksw.distributions.Fact;
import org.aksw.distributions.FactGrouping;

public class Test3 {

	public static void test() throws FileNotFoundException{
		HashMap<String,ArrayList<String>> dateRepository=new HashMap<String,ArrayList<String>>();
		ArrayList<String> date = new ArrayList<String>();
		date.add("2008");
		date.add("1995");
		date.add("1987");
		date.add("2013");
		date.add("2004");
		date.add("2005");
		date.add("2003");
		date.add("2000");
		dateRepository.put("r1", date);
		
		HashMap<String, DateOccurrence [][]> maximalRIM =  new MatrixCreator().createMaximalRIM(dateRepository);
		
		for (String u: maximalRIM.keySet()){

			for (int i=0; i<maximalRIM.get(u).length;i++){
				for (int j=0;j<maximalRIM.get(u)[i].length;j++){
					
					System.out.print(maximalRIM.get(u)[i][j].getDate()+""+ maximalRIM.get(u)[i][j].getOccurrence()+";");
				}
				System.out.println();

			}
		}
		
		List<String> temporalDefactoFacts =new ArrayList<String>();
		
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/FC_Barcelona, 2004, 2003, 2009, 9, 0.03614457831325301]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/FC_Barcelona, 2004, 2003, 2016, 1, 0.004016064257028112]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/FC_Barcelona, 2004, 2003, 1995, 1, 0.004016064257028112]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/FC_Barcelona, 2004, 2003, 2004, 3, 0.012048192771084338]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/FC_Barcelona, 2004, 2003, 2013, 56, 0.2248995983935743]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/FC_Barcelona, 2004, 2003, 2011, 33, 0.13253012048192772]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/FC_Barcelona, 2004, 2003, 2003, 2, 0.008032128514056224]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/FC_Barcelona, 2004, 2003, 2018, 4, 0.01606425702811245]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/FC_Barcelona, 2004, 2003, 2010, 10, 0.040160642570281124]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/FC_Barcelona, 2004, 2003, 2001, 1, 0.004016064257028112]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/FC_Barcelona, 2004, 2003, 1987, 3, 0.012048192771084338]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/FC_Barcelona, 2004, 2003, 2012, 122, 0.4899598393574297]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/FC_Barcelona, 2004, 2003, 2005, 4, 0.01606425702811245]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/Newell's_Old_Boys, 1995, 2000, 1995, 3, 0.3]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/Newell's_Old_Boys, 1995, 2000, 2005, 1, 0.1]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/Newell's_Old_Boys, 1995, 2000, 2000, 2, 0.2]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/Newell's_Old_Boys, 1995, 2000, 2012, 2, 0.2]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/Newell's_Old_Boys, 1995, 2000, 2013, 2, 0.2]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/FC_Barcelona_C, 2003, 2004, 2003, 1, 0.2]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/FC_Barcelona_C, 2003, 2004, 2004, 3, 0.6]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/FC_Barcelona_C, 2003, 2004, 2005, 1, 0.2]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/FC_Barcelona_B, 2004, 2005, 2000, 1, 0.07692307692307693]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/FC_Barcelona_B, 2004, 2005, 2003, 2, 0.15384615384615385]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/FC_Barcelona_B, 2004, 2005, 1995, 1, 0.07692307692307693]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/FC_Barcelona_B, 2004, 2005, 2012, 2, 0.15384615384615385]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/FC_Barcelona_B, 2004, 2005, 2005, 1, 0.07692307692307693]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/FC_Barcelona_B, 2004, 2005, 2004, 6, 0.46153846153846156]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/Argentina_national_football_team, 2005, null, 2009, 3, 0.5]");
		temporalDefactoFacts.add("[r1, p, http://dbpedia.org/resource/Argentina_national_football_team, 2005, null, 2012, 1, 0.5]");
		
		List<Fact> factNormalized= new NormalizationSelection().normalize(1,new ReadFiles().creatListOfFacts(temporalDefactoFacts));
		for (Fact f: factNormalized){
			System.out.println(f);
		}
		FileOutputStream fos = new FileOutputStream("temporalIntervalCreator_out2.csv");
		PrintWriter pw = new PrintWriter(fos);
		
		HashMap<String,HashMap<String,List<Fact>>> groupFacts = new FactGrouping().groupBySubjectObject(factNormalized);
		
		for (String uri: maximalRIM.keySet()){

			HashMap<String,List<Fact>> objbasedgroupfacts = groupFacts.get(uri);
					
			for (String obj: objbasedgroupfacts.keySet()){
				List<Fact> f = objbasedgroupfacts.get(obj);
				
				DateOccurrence [][] matrixManhattanDuration = new TemporalIntervalFactAssociator().matrixYearsDuration(maximalRIM.get(uri));
				new TemporalIntervalFactAssociator().dcCalculator(1,f,matrixManhattanDuration,pw);
				}
			}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		test();

	}

}
