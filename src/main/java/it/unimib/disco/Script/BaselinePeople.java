package it.unimib.disco.Script;

import it.unimib.disco.Evaluation.QualityMeasure;
import it.unimib.disco.ReadFiles.ReadFiles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.aksw.distributions.Fact;
import org.aksw.distributions.TemporalScopingBaseline;

public class BaselinePeople {

	public static void main(String[] args) throws ParseException, IOException {
		//Read gold standard facts
		List<Fact> gs = new ReadFiles().readTabSeparatedFileLS(new File(args[0]));
		List<QualityMeasure> metriclst = new ArrayList<QualityMeasure>();
		
		int referenceLength = (2013-1965)+1;
		int max =0;
		for (Fact f : gs){
			int solutionLength = 0;
			System.out.println(f.get(Fact.Entry.SUBJECT)+ " "+ f.get(Fact.Entry.YAGOSTART)+" " + f.get(Fact.Entry.YAGOEND));
			if (!f.get(Fact.Entry.YAGOSTART).equalsIgnoreCase("null") && !f.get(Fact.Entry.YAGOEND).equalsIgnoreCase("null")){
				
				int referenceStart = Integer.parseInt(f.get(Fact.Entry.YAGOSTART));
	        	int referenceEnd = Integer.parseInt(f.get(Fact.Entry.YAGOEND));
	        	solutionLength = (referenceEnd -referenceStart)+1;
	        	if (max<solutionLength){
	        		max = solutionLength;
	        	}
	        	System.out.println(referenceLength + "\t" + solutionLength);
				
	        	double p=TemporalScopingBaseline.getPrecision(referenceLength, solutionLength);
	       if (p <0 ){p=0;}
				double r=TemporalScopingBaseline.getRecall(referenceLength, solutionLength);
	       if (r <0 ){r=0;}
				double fmeasure=TemporalScopingBaseline.getFScore(referenceLength, solutionLength);
				if (fmeasure <0 ){fmeasure=0;}
				QualityMeasure metrics=new QualityMeasure();
				metrics.add(QualityMeasure.Entry.SUBJECT, f.get(Fact.Entry.SUBJECT));
				metrics.add(QualityMeasure.Entry.OBJECT, f.get(Fact.Entry.OBJECT));
				metrics.add(QualityMeasure.Entry.PRECISION,Double.toString(p));
				metrics.add(QualityMeasure.Entry.RECALL,Double.toString(r));
				metrics.add(QualityMeasure.Entry.fMEASURE,Double.toString(fmeasure));
		
				metriclst.add(metrics);
			}
		}
		System.out.println(max);
		
		
		
		File directory = new File (".");
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/output/"+"evaluation_baseline_people.csv")));
		bw.write("subject"+"	"+"object"+"	"+"precision"+"	"+"recall"+"	"+"microF"+"	"+"macroF"+"\n" );

		
		double avgP=0d, avgR=0d, avgF=0d;
		
		int total=0;
		for (QualityMeasure metrics:metriclst){
					
					double prec = Double.parseDouble(metrics.get(QualityMeasure.Entry.PRECISION));
					double rec = Double.parseDouble(metrics.get(QualityMeasure.Entry.RECALL));
					double fmes = Double.parseDouble(metrics.get(QualityMeasure.Entry.fMEASURE));
					
					bw.write(metrics.get(QualityMeasure.Entry.SUBJECT)+"	"+metrics.get(QualityMeasure.Entry.OBJECT)+"	"+prec+"	"+rec+"	"+fmes+"	"+" "+"\n" );
					if(Double.isNaN(prec)||Double.isNaN(rec)||Double.isNaN(fmes)){}
					else{
						avgP=avgP+prec;
						avgR=avgR+rec;
						avgF=avgF+fmes;
						total++;
					}
				}
				
				double overlapPrec=avgP/total;
				if (overlapPrec <0 ){overlapPrec=0;}
				double overlapRec=avgR/total;
				if (overlapRec <0 ){overlapRec=0;}
				double microF1=avgF/total;
				if (microF1 <0 ){microF1=0;}
				double macroF1=2*(overlapPrec*overlapRec)/(overlapPrec+overlapRec);
				
					
					
			
			bw.write("	"+"	"+"	"+"	"+" "+"	"+" "+"	"+overlapPrec+"	"+overlapRec+"	"+microF1+"	"+macroF1+"\n" );
			
			bw.flush();
			bw.close();
		

	}

}
