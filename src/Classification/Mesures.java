package Classification;

import Classification.Classifieur.Classification;
import Classification.Classifieur.Instance;

public class Mesures {
    public int TP,TN,FP,FN;
    String classe;
    
    public Mesures(Classification resultats, String classe) {
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
    public Mesures() {
    }
    
    
    public double accuracy() {
        return (TP+TN)/(TP+TN+FP+FN);
    }
    public double sensitivity() {
        return (TP)/(TP+FN);
    }
    public double specificity() {
        return (TN)/(TN+FP);
    }
    
    public double precision() {
        return (TP)/(TP+FP);
    }
    public double rappel() {
        return (TP)/(TP+FN);
    }
    public double f_mesure() {
        double P = precision();
        double R = rappel();
        return (2*P*R)/(P+R);
    }
}
