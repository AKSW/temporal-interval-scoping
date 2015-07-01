package it.unimib.disco.Utilities;

import it.unimib.disco.ReadFiles.FactGrouping;
import it.unimib.disco.ReadFiles.ReadFiles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.aksw.distributions.Fact;

public class WebDataCleaning {

	public static void main(String[] args) throws IOException {

		// Read temporalDefacto facts
		List<Fact> temporalDefactoFacts = new ReadFiles().readTabSeparatedFileLS(new File(args[0]));
		HashMap<String,HashMap<String,List<Fact>>> groupTDFacts = new FactGrouping().groupBySubjectObject(temporalDefactoFacts);

		// Read birthdate of subjects
		List<Fact> webdata = new ReadFiles().readTabSeparatedFileLS(new File(args[1]));
		HashMap<String,Fact> webdatabirthdate = new HashMap<String, Fact>();

		for (int i=0; i<webdata.size();i++){
			if (webdata.get(i).get(Fact.Entry.PREDICATE).equalsIgnoreCase("http://dbpedia.org/ontology/birthDate")){
				webdatabirthdate.put(webdata.get(i).get(Fact.Entry.SUBJECT),webdata.get(i));
			}
		}


		BufferedWriter bw;
		File directory = new File (".");

		bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/src/main/resources/webKnowledge/sortbyplayer-labels-with-space_out_small_cleaned.csv")));
		//bw.write("subject"+"	"+"object"+"	"+"start date"+"	"+"end date"+"	"+"occuStart"+"	"+"occuEnd"+"	"+"occuTotGS"+"	"+"occuTotVect"+"	"+"Mean"+"	"+"yearDistance"+"\n" );


		for (String subjGS:groupTDFacts.keySet()){
			if (webdatabirthdate.containsKey(subjGS)){
				
				int birthdate=Integer.parseInt(webdatabirthdate.get(subjGS).get(Fact.Entry.OBJECT));
				birthdate=birthdate+6; //consider only the dates after 6 years from the birthdate
					
				for (String objGS:groupTDFacts.get(subjGS).keySet()){

					for (int i=0; i<groupTDFacts.get(subjGS).get(objGS).size(); i++){


						int date=Integer.parseInt(groupTDFacts.get(subjGS).get(objGS).get(i).get(Fact.Entry.DATE));
					
						if(date<=2013&&date>birthdate){
							bw.write(subjGS+"	"+groupTDFacts.get(subjGS).get(objGS).get(i).get(Fact.Entry.PREDICATE)+"	"+
									objGS+"	"+
									groupTDFacts.get(subjGS).get(objGS).get(i).get(Fact.Entry.YAGOSTART)+"	"+
									groupTDFacts.get(subjGS).get(objGS).get(i).get(Fact.Entry.YAGOEND)+"	"+
									groupTDFacts.get(subjGS).get(objGS).get(i).get(Fact.Entry.DATE)+"	"+
									groupTDFacts.get(subjGS).get(objGS).get(i).get(Fact.Entry.SCORE)+"\n");
						}
					}
				}
			}
		}
		bw.flush();
		bw.close();

	}
}
