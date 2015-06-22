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

public class OccurrenceGS {

	public static void main(String[] args) throws IOException {

		// Read temporalDefacto facts
		List<Fact> temporalDefactoFacts = new ReadFiles().readTabSeparatedFileLS(new File(args[0]));
		HashMap<String,HashMap<String,List<Fact>>> groupTDFacts = new FactGrouping().groupBySubjectObject(temporalDefactoFacts);


		//Read gold standard facts
		List<Fact> yagoFactsLS = new ReadFiles().readTabSeparatedFileLS(new File(args[1]));
		HashMap<String,HashMap<String,List<Fact>>> groupGSFacts = new FactGrouping().groupBySubjectObject(yagoFactsLS);


		BufferedWriter bw;
		File directory = new File (".");

		bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/output/interval/"+"occurrenceGS.csv")));
		bw.write("subject"+"	"+"object"+"	"+"start date"+"	"+"end date"+"	"+"occuStart"+"	"+"occuEnd"+"	"+"occuTot"+"	"+"yearDistance"+"\n" );


		for (String subjGS:groupGSFacts.keySet()){
			int yeardiff=0;

			for (String objGS:groupGSFacts.get(subjGS).keySet()){
				
				for (int i=0; i<groupGSFacts.get(subjGS).get(objGS).size(); i++){
				bw.write(subjGS+"	"+objGS+"	"+
						groupGSFacts.get(subjGS).get(objGS).get(i).get(Fact.Entry.YAGOSTART)+"	"+
						groupGSFacts.get(subjGS).get(objGS).get(i).get(Fact.Entry.YAGOEND)+"	");

				yeardiff=Integer.parseInt(groupGSFacts.get(subjGS).get(objGS).get(i).get(Fact.Entry.YAGOEND))-
						Integer.parseInt(groupGSFacts.get(subjGS).get(objGS).get(i).get(Fact.Entry.YAGOSTART))+1;
				
				if(groupTDFacts.containsKey(subjGS)){
					
					if(groupTDFacts.get(subjGS).containsKey(objGS)){
						String startOccu = "0", endOccu = "0";
						int sum=0;
						
						for(int j=0; j<groupTDFacts.get(subjGS).get(objGS).size();j++){

							if(Integer.parseInt(groupTDFacts.get(subjGS).get(objGS).get(j).get(Fact.Entry.DATE)) >= 
									Integer.parseInt(groupGSFacts.get(subjGS).get(objGS).get(i).get(Fact.Entry.YAGOSTART)) && 
									Integer.parseInt(groupTDFacts.get(subjGS).get(objGS).get(j).get(Fact.Entry.DATE)) <=
									Integer.parseInt(groupGSFacts.get(subjGS).get(objGS).get(i).get(Fact.Entry.YAGOEND))){

								sum=sum+Integer.parseInt(groupTDFacts.get(subjGS).get(objGS).get(j).get(Fact.Entry.SCORE));
							}
							

							if(Integer.parseInt(groupTDFacts.get(subjGS).get(objGS).get(j).get(Fact.Entry.DATE)) ==
									Integer.parseInt(groupGSFacts.get(subjGS).get(objGS).get(i).get(Fact.Entry.YAGOSTART))){ 

								startOccu=groupTDFacts.get(subjGS).get(objGS).get(j).get(Fact.Entry.SCORE);
							}

							if (Integer.parseInt(groupTDFacts.get(subjGS).get(objGS).get(j).get(Fact.Entry.DATE)) ==
									Integer.parseInt(groupGSFacts.get(subjGS).get(objGS).get(i).get(Fact.Entry.YAGOEND))){ 

								endOccu=groupTDFacts.get(subjGS).get(objGS).get(j).get(Fact.Entry.SCORE);
							}
							
						}
						
						bw.write(startOccu+"	"+endOccu+"	"+sum+"	"+yeardiff+"\n");
					}
					else{
						bw.write(0+"	"+0+"	"+0+"	"+yeardiff+"\n");
					}

				}
				else{
					bw.write(0+"	"+0+"	"+0+"	"+yeardiff+"\n");
				}
			}
			}


		}
		bw.flush();
		bw.close();

	}

}
