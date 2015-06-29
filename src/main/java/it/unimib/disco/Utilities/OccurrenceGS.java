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

		//Read the vector 
		List<Fact> vectorFacts = new ReadFiles().readTabSeparatedIntervals(new File(args[2]));
		HashMap<String,HashMap<String,List<Fact>>> groupfactvector= new FactGrouping().groupBySubjectObject(vectorFacts);
		HashMap<String,HashMap<String,StatisticalData>> statVector= statisticalDataFact(groupfactvector); 
		
		BufferedWriter bw;
		File directory = new File (".");

		bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/output/interval/"+"occurrenceGS.csv")));
		bw.write("subject"+"	"+"object"+"	"+"start date"+"	"+"end date"+"	"+"occuStart"+"	"+"occuEnd"+"	"+"occuTotGS"+"	"+"occuTotVect"+"	"+"Mean"+"	"+"yearDistance"+"\n" );


		for (String subjGS:groupGSFacts.keySet()){
			int yeardiff=0;
			
			for (String objGS:groupGSFacts.get(subjGS).keySet()){
				
				for (int i=0; i<groupGSFacts.get(subjGS).get(objGS).size(); i++){
				bw.write(subjGS+"	"+objGS+"	"+
						groupGSFacts.get(subjGS).get(objGS).get(i).get(Fact.Entry.YAGOSTART)+"	"+
						groupGSFacts.get(subjGS).get(objGS).get(i).get(Fact.Entry.YAGOEND)+"	");

				yeardiff=Integer.parseInt(groupGSFacts.get(subjGS).get(objGS).get(i).get(Fact.Entry.YAGOEND))-
						Integer.parseInt(groupGSFacts.get(subjGS).get(objGS).get(i).get(Fact.Entry.YAGOSTART))+1;
				
				if(groupTDFacts.containsKey(subjGS)&&statVector.containsKey(subjGS)){
					
					if(groupTDFacts.get(subjGS).containsKey(objGS)&&statVector.get(subjGS).containsKey(objGS)){
						String startOccu = "0", endOccu = "0";
						int sum=0;
						
						double sumS=statVector.get(subjGS).get(objGS).getSumS();
						double meanS=statVector.get(subjGS).get(objGS).getMeanS();
						
					
						
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
						
						bw.write(startOccu+"	"+endOccu+"	"+sum+"	"+sumS+"	"+meanS+"	"+yeardiff+"\n");
					}
					else{
						bw.write(0+"	"+0+"	"+0+"	"+0.0+"	"+0.0+"	"+yeardiff+"\n");
					}

				}
				else{
					bw.write(0+"	"+0+"	"+0+"	"+0.0+"	"+0.0+"	"+yeardiff+"\n");
				}
			}
			}


		}
		bw.flush();
		bw.close();

	}
	
	public static HashMap<String,HashMap<String,StatisticalData>> statisticalDataFact(HashMap<String,HashMap<String,List<Fact>>> groupfactvector){
		
		HashMap<String,HashMap<String,StatisticalData>> factStat= new HashMap<String,HashMap<String,StatisticalData>>(); 
		
		for (String subj: groupfactvector.keySet()){
			HashMap<String,StatisticalData> factStatObj= new HashMap<String,StatisticalData>();
			
			for (String obj:groupfactvector.get(subj).keySet()){
				
				factStatObj.put(obj, statisticalCalculation(groupfactvector.get(subj).get(obj)));
				
			}
			factStat.put(subj, factStatObj);
		}
		
		/*for (String subjGS:factStat.keySet()){
			
			
			for (String objGS:factStat.get(subjGS).keySet()){
				System.out.println(subjGS+" "+objGS+" "+factStat.get(subjGS).get(objGS).getSumS()+" "+ factStat.get(subjGS).get(objGS).getMeanS());
			}
		}*/

		return factStat;
	}
	
	public static StatisticalData statisticalCalculation(List<Fact> lsFact){
		StatisticalData statistical=new StatisticalData();
		double sumS = 0d;
		int total=0;
		
		for (int i=0; i<lsFact.size();i++){
			
			sumS=sumS+Double.parseDouble(lsFact.get(i).get(Fact.Entry.SCORE));
			total++;
		}
		
		double meanS=sumS/total;
		
		statistical.setSumS(sumS);
		statistical.setMeanS(meanS);
		
		return statistical;
	}

}
