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

public class BaselinePolitician {

	public static void main(String[] args) throws ParseException, IOException {
		//Read gold standard facts
		List<Fact> gs = new ReadFiles().readTabSeparatedFileLS(new File(args[0]));
		List<QualityMeasure> metriclst = new ArrayList<QualityMeasure>();
		
		int referenceLength = (2013-1980)+1;
		
		for (Fact f : gs){
			int solutionLength = 0;
			
			if (!f.get(Fact.Entry.YAGOSTART).equalsIgnoreCase("null") && !f.get(Fact.Entry.YAGOEND).equalsIgnoreCase("null")){
			int referenceStart = Integer.parseInt(f.get(Fact.Entry.YAGOSTART));
	        int referenceEnd = Integer.parseInt(f.get(Fact.Entry.YAGOEND));
	        solutionLength = (referenceEnd -referenceStart)+1;
	      
			double p=TemporalScopingBaseline.getPrecision(referenceLength, solutionLength);
	       
			double r=TemporalScopingBaseline.getRecall(referenceLength, solutionLength);
	       
			double fmeasure=TemporalScopingBaseline.getFScore(referenceLength, solutionLength);
			 System.out.println(p + "\t" + r+ "\t" + fmeasure);
			QualityMeasure metrics=new QualityMeasure();
					metrics.add(QualityMeasure.Entry.SUBJECT, f.get(Fact.Entry.SUBJECT));
					metrics.add(QualityMeasure.Entry.OBJECT, f.get(Fact.Entry.OBJECT));
					metrics.add(QualityMeasure.Entry.PRECISION,Double.toString(p));
					metrics.add(QualityMeasure.Entry.RECALL,Double.toString(r));
					metrics.add(QualityMeasure.Entry.fMEASURE,Double.toString(fmeasure));
		
					metriclst.add(metrics);
			}
		}
		
		
		
		
		File directory = new File (".");
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath()+"/output/"+"evaluation_baseline_politician.csv")));
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
				double overlapRec=avgR/total;
				double microF1=avgF/total;
				double macroF1=2*(overlapPrec*overlapRec)/(overlapPrec+overlapRec);
			
			bw.write("	"+"	"+"	"+"	"+" "+"	"+" "+"	"+overlapPrec+"	"+overlapRec+"	"+microF1+"	"+macroF1+"\n" );
			
			bw.flush();
			bw.close();
		

	}

}
