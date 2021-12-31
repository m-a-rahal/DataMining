package data;
import java.util.HashMap;

public class Frequences extends HashMap<Double, Integer>{
	/** cette classe s'agit d'un dictionnaire pour stocker des valeurs et leurs frequences
	 * */
	private static final long serialVersionUID = 1L;

	public Double[] pourcentages() {
		/** retournes les frequences en terme de pourcentages, exemple:
		 * [10,20,10,5,5] ---> [20%,40%,20%,10%,10%]
		 * */
		Double p[] = new Double[size()];
		int i = 0;
		double somme = 0;
		for (Integer freq : values()) {
			p[i++] = freq/1.0;
			somme += freq;
		}
		for (int j = 0; j < size(); j++) {
			p[j] /= somme;
		}
		return p;
	}

	public Double pourcentage(Double val) {
		/** retournes le pourcentage de la valeure val*/
		double somme = 0;
		for (Integer freq : values()) {
			somme += freq;
		}
		return frequence_de(val) / somme;
	}

	public Integer ajouter(double val) {
		/** Compter la frequence d'une valeure*/
		Integer frequence = get(val);
		frequence = frequence != null ? frequence+1 : 1;
		put(val, frequence);
		return frequence;
	}

	public Integer frequence_de(double val) {
		/** retourne la fréquence de l'element val, ou 0 si il n'existe pas*/
		Integer freq = get(val);
		return freq == null ? 0 : freq;
	}
}
