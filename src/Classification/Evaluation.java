package Classification;

import java.util.ArrayList;
import java.util.TreeSet;

import Classification.Classifieur.Classification;
import Classification.Classifieur.Instance;

public class Evaluation {
    public int TP,TN,FP,FN;
    public String classe;
    
    public Evaluation(Classification resultats, String classe) {
    	 this.classe = classe;
         for(Instance instance : resultats.values()) {
             if (instance.classe_predite.equals(classe)) {
                 if(instance.classe_correcte.equals(classe)) {
                    TP++; 
                 }else {
                    FP++;
                 }
             } else {
                 if(instance.classe_correcte.equals(classe)) {
                    FN++; 
                 }else {
                    TN++;
                 }
             }
         }
    }
    public Evaluation() {
    }
    
    
    public double accuracy() {
        return (TP+TN)/(double)(TP+TN+FP+FN);
    }
    public double sensitivity() {
        return (TP)/(double)(TP+FN);
    }
    public double specificity() {
        return (TN)/(double)(TN+FP);
    }
    
    public double precision() {
        return (TP)/(double)(TP+FP);
    }
    public double rappel() {
        return (TP)/(double)(TP+FN);
    }
    public double f_mesure() {
        double P = precision();
        double R = rappel();
        return (2*P*R)/(P+R);
    }
    
    public static class Evaluations extends ArrayList<Evaluation> {
		private Classification classifications;
		public ArrayList<String> classes;

		public Evaluations(Classification resultats) {
			this.classifications = resultats;
			this.classes = resultats.classes;
		}
    	
    }
}
