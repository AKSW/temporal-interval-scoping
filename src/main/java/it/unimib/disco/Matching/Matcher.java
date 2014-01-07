package it.unimib.disco.Matching;

import it.unimib.disco.FactExtractor.DateOccurrence;
import it.unimib.disco.MatrixCreator.MatrixCreator;
import it.unimib.disco.Reasoning.Interval;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.aksw.distributions.Fact;
import org.aksw.distributions.Fact.Entry;

public class Matcher {

	public HashMap<String,DateOccurrence [][]> diagonalMatrixCreator(HashMap<String, DateOccurrence [][]> reducedMatrix2){
		
		HashMap<String,DateOccurrence [][]> diagonalMatrixURI = new HashMap<String,DateOccurrence [][]>();

		
		for (String Uri: reducedMatrix2.keySet()){
			DateOccurrence [][] diagonalMatrix= reducedMatrix2.get(Uri);
			
			for (int i=1; i<diagonalMatrix.length; i++){
				for(int j=1; j<diagonalMatrix[i].length; j++){
					if(j>=i&&!(diagonalMatrix[i][j].getOccurrence().equalsIgnoreCase("X"))){
						
						double value = (diagonalMatrix.length -j)*1.0/(diagonalMatrix.length-i); 
	
						diagonalMatrix[i][j]= new DateOccurrence("", String.valueOf(value));
					}
					else{

						diagonalMatrix[i][j]= new DateOccurrence("", "0");
					}
				}
			}
			diagonalMatrixURI.put(Uri, diagonalMatrix);
		}
		
		return diagonalMatrixURI;
		
	}
	
	public DateOccurrence [][] matrixYearsDuration(DateOccurrence [][] mRed){
		
		DateOccurrence [][] temporalDCMatrix= new DateOccurrence[mRed.length][mRed.length];
		temporalDCMatrix[0][0]= new DateOccurrence("[Start]/", "End");

		
		for(int l=1; l<temporalDCMatrix.length; l++){
			temporalDCMatrix[l][0] = mRed[l][0];
		}
		
		for(int m=1; m<temporalDCMatrix.length; m++){
			temporalDCMatrix[0][m]= mRed[0][m];
		}
			
		for (int i=1; i<temporalDCMatrix.length; i++){
			for(int j=1; j<temporalDCMatrix[i].length; j++){
					
				if(j>=i){
						
					MatrixCreator mpc= new MatrixCreator();
					Date column = mpc.stringToLong(temporalDCMatrix[0][j].getDate());
					Date row = mpc.stringToLong(temporalDCMatrix[i][0].getDate());
					long millisecDistance = column.getTime()-row.getTime();
					long dayDistance= millisecDistance/(1000 * 60 * 60 * 24);
					long yearDistance= dayDistance/365+1;
						
					temporalDCMatrix[i][j]= new DateOccurrence("", String.valueOf(yearDistance));
				}
				else{
					temporalDCMatrix[i][j]= new DateOccurrence("", "0");
				}
			}
		}
			
		
		/*for (int i=0; i<temporalDCMatrix.length;i++){
			for (int j=0;j<temporalDCMatrix[i].length;j++){
				
				System.out.print(temporalDCMatrix[i][j].getDate()+""+ temporalDCMatrix[i][j].getOccurrence()+";");
			}
			System.out.println();

	}*/
		return temporalDCMatrix;
		
	}
	

	public DateOccurrence [][] matrixManhattanDuration( DateOccurrence [][] mRed){
		
			DateOccurrence [][] temporalDCMatrix= new DateOccurrence[mRed.length][mRed.length];
		temporalDCMatrix[0][0]= new DateOccurrence("[Start]/", "End");

			
			for(int l=1; l<temporalDCMatrix.length; l++){
				temporalDCMatrix[l][0] = mRed[l][0];

			}
			
			
			for(int m=1; m<temporalDCMatrix.length; m++){
				temporalDCMatrix[0][m]= mRed[0][m];
			}

			for (int i=1; i<temporalDCMatrix.length; i++){
				int count=1;
	
			for(int j=1; j<temporalDCMatrix[i].length; j++){
					
					if(j>=i){
											
						temporalDCMatrix[i][j]= new DateOccurrence("", String.valueOf(count));
						count++;
					}
					else{

						temporalDCMatrix[i][j]= new DateOccurrence("", "0");
					}
					
				}
	
			}
			/*for (int i=0; i<temporalDCMatrix.length;i++){
			for (int j=0;j<temporalDCMatrix[i].length;j++){
				
				System.out.print(temporalDCMatrix[i][j].getDate()+""+ temporalDCMatrix[i][j].getOccurrence()+";");
			}
			System.out.println();

	}*/
		return temporalDCMatrix;
		
	}

	public List<Interval> dcCalculator(int normalizationType, List<Fact> f,DateOccurrence [][] matrixMD,PrintWriter pw){
			
			DateOccurrence [][] m = new DateOccurrence[matrixMD.length][matrixMD.length];
			List<Interval> intervals =new ArrayList<Interval>();
			for (int i=0; i<m.length; i++){
				
				for(int j=0; j<m[i].length; j++){
					
					if(i==0||j==0){
					
						m[0][j]=matrixMD[0][j];
						m[i][0]=matrixMD[i][0];
					}
					
					else if(j>i){
						String occuMatrix = matrixMD[i][j].getOccurrence().trim();
					
						int value = Integer.parseInt(occuMatrix);
						if(value!=0){
						int duration = Integer.valueOf(occuMatrix);
						
						MatrixCreator mpc= new MatrixCreator();
						
						Date column = mpc.stringToLong(m[0][j].getDate());
						Date row = mpc.stringToLong(m[i][0].getDate());
						
						double formula=0d;
						
						if(normalizationType==1){
							//no normalization 
							double hit=hitCountNoNormalization(row,column,f);
							formula= hit/duration; 
						}
						else{
							//normalization
							formula=hitCountNormalization(normalizationType,row,column,f);
							
						}
						
						m[i][j]= new DateOccurrence("", String.valueOf(formula));
						
						//add start and end year and the formula
						Interval interval = new Interval();
						interval.addStart(m[i][0].getDate());
						interval.addEnd(m[0][j].getDate());
						interval.addValue(String.valueOf(formula));
						intervals.add(interval);
					}
					}
					else{

						m[i][j]= new DateOccurrence("", "0");
					}
					

					if (i==0||j==0){
						if(i==0&&j==0){
							pw.print(m[i][j].getDate()+""+ m[i][j].getOccurrence()+"	");}
						else{pw.print(m[i][j].getDate()+"	");}}
					else{
					pw.print(m[i][j].getDate()+"	"+ m[i][j].getOccurrence());}
					
				}pw.println();
				
			}

		return intervals;
		
	}
	
	public double hitCountNoNormalization(Date row, Date column,List<Fact> list ){
		double hit=0;
		MatrixCreator mpc= new MatrixCreator();

		for (Fact f:list){
			
	            Date year= mpc.stringToLong(f.get(Entry.DATE));
	            
	            if((column.after(year)||column.equals(year))&&(row.before(year)||row.equals(year))){
	            
	            	hit=hit+Double.valueOf(f.get(Entry.SCORE).trim());//occurrence value

	            }
		}

		return hit;
		
	}
	
	public double hitCountNormalization(int normalizationType,Date row, Date column, List<Fact> list ){
		double hit=0;
		MatrixCreator mpc= new MatrixCreator();
		int count=0;
		for (Fact f:list){
	            Date year= mpc.stringToLong(f.get(Entry.DATE));

	            if((column.after(year)||column.equals(year))&&(row.before(year)||row.equals(year))){

	            		hit=hit+Double.valueOf(f.get(Entry.SCORE).trim());//occurrence value

	            	count++;
			
	            }
		}

		if (count>0){
			hit=hit/count;
		}
		else{
			hit=0;
		}

		return hit;
		
	}
	
	public int hitWeighted(Date row, Date column,HashSet<ArrayList<String>> yearOccu ){
		int hit=0;
		MatrixCreator mpc= new MatrixCreator();
		for (ArrayList<String> value: yearOccu){

	            Date year= mpc.stringToLong(value.get(0));

	            if((column.after(year)||column.equals(year))&&(row.before(year)||row.equals(year))){

	            	hit=hit+Integer.valueOf(value.get(1).trim());
			
	            }
		}

		return hit;
		
	}
}
