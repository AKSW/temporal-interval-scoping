package it.unimib.disco.TemporalIntervalCreator;

import it.unimib.disco.FactExtractor.DateOccurrence;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class TemporalIntervalFactAssociator {

	public HashMap<String,DateOccurrence [][]> diagonalMatrixCreator(HashMap<String, DateOccurrence [][]> reducedMatrix2){
		
		HashMap<String,DateOccurrence [][]> diagonalMatrixURI = new HashMap<String,DateOccurrence [][]>();
		ArrayList<String> occurrence = new ArrayList<String>();
		
		for (String Uri: reducedMatrix2.keySet()){
			DateOccurrence [][] diagonalMatrix= reducedMatrix2.get(Uri);
			
			for (int i=1; i<diagonalMatrix.length; i++){
				for(int j=1; j<diagonalMatrix[i].length; j++){
					if(j>=i&&!(diagonalMatrix[i][j].getOccurrence().get(0).equalsIgnoreCase("X"))){
						occurrence = new ArrayList<String>();
						
						double value = (diagonalMatrix.length -j)*1.0/(diagonalMatrix.length-i); 
						occurrence.add(String.valueOf(value));
						diagonalMatrix[i][j]= new DateOccurrence("", occurrence);
					}
					else{
						occurrence = new ArrayList<String>();
						occurrence.add("0");
						diagonalMatrix[i][j]= new DateOccurrence("", occurrence);
					}
				}
			}
			diagonalMatrixURI.put(Uri, diagonalMatrix);
		}
		
		return diagonalMatrixURI;
		
	}
	
	public DateOccurrence [][] matrixYearsDuration(DateOccurrence [][] mRed){
		
		ArrayList<String> occurrence = new ArrayList<String>();

		DateOccurrence [][] temporalDCMatrix= new DateOccurrence[mRed.length][mRed.length];
		
		occurrence.add("End");
		temporalDCMatrix[0][0]= new DateOccurrence("[Start]/", occurrence);

		
		for(int l=1; l<temporalDCMatrix.length; l++){
			temporalDCMatrix[l][0] = mRed[l][0];

		}
		
		
		for(int m=1; m<temporalDCMatrix.length; m++){
			temporalDCMatrix[0][m]= mRed[0][m];
		}
			
		for (int i=1; i<temporalDCMatrix.length; i++){
				for(int j=1; j<temporalDCMatrix[i].length; j++){
					
					if(j>=i){
						
						MatrixPruningCreator mpc= new MatrixPruningCreator();
						Date column = mpc.stringToLong(temporalDCMatrix[0][j].getDate());
						Date row = mpc.stringToLong(temporalDCMatrix[i][0].getDate());
						long millisecDistance = column.getTime()-row.getTime();
						long dayDistance= millisecDistance/(1000 * 60 * 60 * 24);
						long yearDistance= dayDistance/365+1;
						occurrence = new ArrayList<String>();
						occurrence.add(String.valueOf(yearDistance));
						
						temporalDCMatrix[i][j]= new DateOccurrence("", occurrence);
					}
					else{
						occurrence = new ArrayList<String>();
						occurrence.add("0");
						temporalDCMatrix[i][j]= new DateOccurrence("", occurrence);
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
		
		ArrayList<String> occurrence = new ArrayList<String>();

			DateOccurrence [][] temporalDCMatrix= new DateOccurrence[mRed.length][mRed.length];
			
			occurrence.add("End");
			temporalDCMatrix[0][0]= new DateOccurrence("[Start]/", occurrence);

			
			for(int l=1; l<temporalDCMatrix.length; l++){
				temporalDCMatrix[l][0] = mRed[l][0];

			}
			
			
			for(int m=1; m<temporalDCMatrix.length; m++){
				temporalDCMatrix[0][m]= mRed[0][m];
			}

			
			for (int i=1; i<temporalDCMatrix.length; i++){
				int count=1;
				for(int j=1; j<temporalDCMatrix[i].length; j++){
		
					if(j>=i&&!(mRed[i][j].getOccurrence().get(0).equalsIgnoreCase("X"))){
						
						occurrence = new ArrayList<String>();
						occurrence.add(String.valueOf(count));
						count++;
						temporalDCMatrix[i][j]= new DateOccurrence("", occurrence);
					}
					else{
						occurrence = new ArrayList<String>();
						occurrence.add("0");
						temporalDCMatrix[i][j]= new DateOccurrence("", occurrence);
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



	public HashSet<ArrayList<String>> dcCalculator(int normalizationType, HashSet<ArrayList<String>> yearOccu,DateOccurrence [][] matrixMD,PrintWriter pw){
		
	
		ArrayList<String> occurrence = new ArrayList<String>();

			
			DateOccurrence [][] m = new DateOccurrence[matrixMD.length][matrixMD.length];
			HashSet<ArrayList<String>> intervals =new HashSet<ArrayList<String>>();
			for (int i=0; i<m.length; i++){
				
				for(int j=0; j<m[i].length; j++){
					ArrayList<String> interval = new ArrayList<String>();
					if(i==0||j==0){
						m[0][j]=matrixMD[0][j];
						m[i][0]=matrixMD[i][0];
					}
					
					else if(j>i&&!(matrixMD[i][j].getOccurrence().get(0).contains("0"))){
						
						String occuMatrix = matrixMD[i][j].getOccurrence().get(0).trim();
						
						int duration = Integer.valueOf(occuMatrix);
						
						MatrixPruningCreator mpc= new MatrixPruningCreator();
						
						Date column = mpc.stringToLong(m[0][j].getDate());
						Date row = mpc.stringToLong(m[i][0].getDate());
						
						double formula=0d;
						
						if(normalizationType==1){
							//no normalization 
							double hit=hitCountNoNormalization(row,column,yearOccu);
							formula= hit/duration; 
						}
						else{
							//normalization
							formula=hitCountNormalization(normalizationType,row,column,yearOccu);
							
						}

						occurrence = new ArrayList<String>();
						occurrence.add(String.valueOf(formula));
						
						m[i][j]= new DateOccurrence("", occurrence);
					
						//add start and end year and the formula
						interval.add(m[i][0].getDate());
						interval.add(m[0][j].getDate());
						interval.add(String.valueOf(formula));
					}
					else{
						occurrence = new ArrayList<String>();
						occurrence.add("0");
						m[i][j]= new DateOccurrence("", occurrence);
					}
					if(interval.size()!=0){
					//System.out.println(interval);
						intervals.add(interval);
					
					}

					pw.print(m[i][j].getDate()+""+ m[i][j].getOccurrence()+";");
					
				}pw.println();
				
			}


		return intervals;
		
	}
	
	public double hitCountNoNormalization(Date row, Date column,HashSet<ArrayList<String>> yearOccu ){
		double hit=0;
		MatrixPruningCreator mpc= new MatrixPruningCreator();

		for (ArrayList<String> value: yearOccu){
			
	            Date year= mpc.stringToLong(value.get(0));

	            if((column.after(year)||column.equals(year))&&(row.before(year)||row.equals(year))){

	            	hit=hit+Double.valueOf(value.get(1).trim());//occurrence value

	            }
		}

		return hit;
		
	}
	
	public double hitCountNormalization(int normalizationType,Date row, Date column, HashSet<ArrayList<String>> yearOccu ){
		double hit=0;
		MatrixPruningCreator mpc= new MatrixPruningCreator();
		int count=0;
		for (ArrayList<String> value: yearOccu){
	            Date year= mpc.stringToLong(value.get(0));
	            //System.out.println(row+" "+column+" "+value.get(1).trim());
	            if((column.after(year)||column.equals(year))&&(row.before(year)||row.equals(year))){
	            	if(normalizationType==2){
	            		hit=hit+Double.valueOf(value.get(2).trim());//local normalization value
	            	}
	            	else{
	            		hit=hit+Double.valueOf(value.get(1).trim());//occurrence value
	            	}
	            	
	            	//
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
		MatrixPruningCreator mpc= new MatrixPruningCreator();
		for (ArrayList<String> value: yearOccu){

	            Date year= mpc.stringToLong(value.get(0));

	            if((column.after(year)||column.equals(year))&&(row.before(year)||row.equals(year))){

	            	hit=hit+Integer.valueOf(value.get(1).trim());
			
	            }
		}

		return hit;
		
	}
}
