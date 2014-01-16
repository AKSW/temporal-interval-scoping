/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aksw.distributions;

import java.math.BigInteger;

/**
 *
 * @author ngonga
 */
public class TemporalScopingBaseline {

    public static double getPrecision(int referenceLength, int solutionLength) {
        double p = 0d, prec;
        double count = 0;
        long number;
        for (double misses = 0d; misses <= referenceLength; misses++) {
            count = 0;
            for (double falsePositives = 0; falsePositives <= Math.min(misses, solutionLength); falsePositives++) {
                try {
                    if (solutionLength > falsePositives) {
                        prec = (solutionLength - falsePositives) / ((solutionLength - falsePositives) + (misses - falsePositives));
                        number = comboChoose(solutionLength, (int) falsePositives) * comboChoose(referenceLength - solutionLength, (int) (misses - falsePositives));
                        count = count + prec * number;
                        //System.out.println(misses + "\t" + falsePositives + "\t" + prec + "\t" + count + "\t" + number);
                    }
                } catch (Exception e) {
                }
            }
            p = p + count;
        }
        return p / Math.pow(2, referenceLength);
    }

    public static double getRecall(int referenceLength, int solutionLength) {
        double p = 0d, rec;
        double count = 0;
        long number;
        for (double misses = 0d; misses <= referenceLength; misses++) {
            count = 0;
            for (double falsePositives = 0; falsePositives <= Math.min(misses, solutionLength); falsePositives++) {
                try {
                    if (solutionLength > falsePositives) {
                        rec = (solutionLength - falsePositives) / solutionLength;                        
                        number = comboChoose(solutionLength, (int) falsePositives) * comboChoose(referenceLength - solutionLength, (int) (misses - falsePositives));
                        if(rec < 0 || number < 0)
                            System.out.println(rec+"\t"+number+"\t"+
                            comboChoose(solutionLength, (int) falsePositives)+"\t"+comboChoose(referenceLength - solutionLength, (int) (misses - falsePositives)));
                        count = count + rec * number;
                        //System.out.println(misses + "\t" + falsePositives + "\t" + prec + "\t" + count + "\t" + number);
                    }
                } catch (Exception e) {
                }
            }
            p = p + count;
        }
        System.out.println(p+"==>"+Math.pow(2, referenceLength));
        return p / Math.pow(2, referenceLength);
    }

    public static double getFScore(int referenceLength, int solutionLength) {
        double p = getPrecision(referenceLength, solutionLength);
        double r = getRecall(referenceLength, solutionLength);
        return 2 * p * r / (p + r);
    }

    public static void main(String args[]) {
        for (int i = 1; i < 4; i++) {
//            System.out.println(getPrecision(2, i));
//            System.out.println(getRecall(2, i));
            System.out.println("i = "+i+" P="+getPrecision(40, i)+" R="+getRecall(35, i)+" F="+getFScore(35, i));
        }
    }

    public static BigInteger factorial(int a) { //non-recursive factorial, returns long
        BigInteger answer = new BigInteger("1");
        for (int i = 1; i <= a; i++) { //starts from 1, multiplies up, such as 1*2*3...
            answer = answer.multiply(new BigInteger(i+""));
        }
        return answer;
    }

    public static long comboChoose(int n, int k) { //combinatorics function. takes n,k
        BigInteger result = factorial(n).divide(factorial(n-k).multiply(factorial(k)));
        System.out.println(result+" n="+n+", k="+k);        
        return result.intValue();
    }
}
