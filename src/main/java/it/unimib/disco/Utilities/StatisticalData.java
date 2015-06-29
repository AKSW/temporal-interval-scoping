package it.unimib.disco.Utilities;

public class StatisticalData {
	public double getSumS() {
		return sumS;
	}

	public void setSumS(double sumS) {
		this.sumS = sumS;
	}

	public double getMeanS() {
		return meanS;
	}

	public void setMeanS(double meanS) {
		this.meanS = meanS;
	}

	double sumS=0d, meanS=0d;
	
	public StatisticalData(double sumScore,double meanScore){
		this.sumS=sumScore;
		this.meanS=meanScore;
	}
	public StatisticalData(){
		this.sumS=0d;
		this.meanS=0d;
	}
}
