package mesures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import data.Dataset;

public class Mesures_attribut {
	/** Mesures relatives a une colonne du dataset*/
	Dataset dataset;
	int indice_attribut;
	String nom_attribut;

	public Mesures_attribut(Dataset dataset, int indice_attribut) {
		this.dataset = dataset;
		this.indice_attribut = indice_attribut;
		this.nom_attribut = dataset.col_names[indice_attribut];
	}
	
	// calculer la moyenne de l'attribut 
	public double moyenne() {
		double moy = 0.0;
		for (int i = 0; i < dataset.n; i++) {
			moy += dataset.data[i][indice_attribut];
		}
		moy /= dataset.n;
		return moy;
	}
	
	// calculer la moyenne tronquée de l'attribut 
		public double moyenne_tronqee(double quantile) {
			if (quantile >= 0.5) {
				throw new IllegalArgumentException("ne peut pas tronquer plus que 50% des deux côtés de la liste des valeurs");
			}
			double sup = quantile(1-quantile);
			double inf = quantile(quantile);
			
			double somme = 0;
			int count = 0;
			for (int i = 0; i < dataset.n; i++) {
				double x = dataset.data[i][indice_attribut];
				if (x >= inf && x <= sup) {
					somme += x;
					count++;
				}
			}
			
			return somme / count;
		}
	
	// calculer le quantile q de l'attribut
	public double quantile(double q) {
		/** caculate quantile using R-2 approximation (averaging the two neighbors in case of conflict) (src = 'https://en.wikipedia.org/wiki/Quantile#:~:text=empirical%20distribution%20function.-,R%E2%80%912,-%2C%20SAS%E2%80%915%2C%20Maple')*/
		if (q < 0 || q > 1)
			throw new IllegalArgumentException("Le quantile doit être compris entre 0 et 1");
		if (q == 0)
			return min();
		if (q == 1)
			return max();
		ArrayList<Double> valeurs = getSortedValues();
		double h = dataset.n * q + 0.5;
		int left = (int) Math.ceil(h - 0.5);
		int right = (int) Math.floor(h + 0.5);
		return (valeurs.get(left) + valeurs.get(right)) / 2;
	}

	// calculer la variance de l'attribut 
	public double variance() {
		double var;
		double moyenne = moyenne();
		var = 0.0;
		for (int i = 0; i < dataset.n; i++) {
			var += Math.pow(dataset.data[i][indice_attribut] - moyenne, 2);
		}
		var /= dataset.n;
		return var;
	}

	// calculer l'écart type de l'attribut 
	public double ecartType() {
		return Math.sqrt(variance());
	}

	// calculer le milieu de l'étendu de l'attribut
	public double milieu() {
		return (max()+min())/2;
	}
	
	public double mode() throws Exception {
		Double mod = null;
		Integer mod_freq = null;
		TreeMap<Double, Integer> frequences = new TreeMap<>();
		for (int i = 0; i < dataset.n; i++) {
			double val = dataset.get(i, indice_attribut);
			Integer freq = frequences.get(val);
			freq = freq != null ? freq+1 : 1;
			frequences.put(val, freq);
			if (mod == null || mod_freq < freq) {
				mod = val;
				mod_freq = freq;
				continue;
			}
		}
		return mod;
	}

	// calculer le mode de l'attribut avec discrétization 
	public double mode_discret() throws Exception {
		/** fait une discrétization des donnés avec des intervales de longeure = w
		 * tel que w = (max-min)/k
		 * k = round(   5*log10(nombre de valeurs)  )
		 * le mode est donc le milieu de l'intervale ayant la plus grande frequence
		*/
		double mod_discret;

		int k = (int) Math.round(5 * Math.log10(dataset.n));
		double max = max();
		double min = min();
		double w = (max - min) / k;
		int[] frequences = new int[k];
		for (int i = 0; i < dataset.n; i++) {
			int indice = (int) Math.floor((dataset.data[i][indice_attribut] - min) / w);
			frequences[Math.min(indice,k-1)]++; // l'indice doit être < k, ça peut arriver dans le cas ou valeure = max
		}
		int indice_max = 0;
		for (int i = 1; i < frequences.length; i++) {
			if (frequences[i] > frequences[indice_max]) {
				indice_max = i;
			}
		}
		mod_discret = min + (indice_max + 0.5) * w;
		return mod_discret;
	}

	// calculer la médiane de l'attribut 
	public double mediane() throws Exception {
		double med;
		ArrayList<Double> vecteur = getSortedValues();
		// si la taile de la liste est pair, retourner l'élement x(n/2), sinon la moyenne des deux élements au milieu
		int n = vecteur.size();
		if (n % 2 == 1) {
			med = (vecteur.get(n/2) + vecteur.get(n/2 - 1)) / 2;
		} else {
			med =  vecteur.get(n/2);
		}
		return med;
	}

	private ArrayList<Double> getSortedValues() {
		new ArrayList<>(dataset.n);
		ArrayList<Double> vecteur = new ArrayList<Double>();
		for (int i = 0; i < dataset.n; i++) {
			vecteur.add(dataset.data[i][indice_attribut]);
		}
		Collections.sort(vecteur);
		return vecteur;
	}

	public double min() {
		double min = dataset.data[0][indice_attribut];
		for (int i = 1; i < dataset.n; i++) {
			if (dataset.data[i][indice_attribut] < min) {
				min = dataset.data[i][indice_attribut];
			}
		}
		return min;
	}

	public double max() {
		double max = dataset.data[0][indice_attribut];
		for (int i = 1; i < dataset.n; i++) {
			if (dataset.data[i][indice_attribut] > max) {
				max = dataset.data[i][indice_attribut];
			}
		}
		return max;
	}
	
	public double etendu() {
		return max()-min();
	}
	
	public double milieu_etendu() {
		return (min()+max())/2;
	}
	
	public double quartile(int q) {
		if(q>3 | q<1) {
			throw new IllegalArgumentException("Le quartile doit être compris entre 1 et 3");
		}
		ArrayList<Double> vecteur = new ArrayList<Double>();
		vecteur = getSortedValues();
		if(q==1) {
			return vecteur.get(dataset.n/4);
		}else if(q==2) {
			return vecteur.get(dataset.n/2);
		}else {
			return vecteur.get(3*dataset.n/4);
		}
		
	}
	
	public double IQR() {
		return quartile(3)-quartile(1);
	}
	
	public ArrayList<Double> outliers() {
		ArrayList<Double> vecteur = new ArrayList<Double>();
		ArrayList<Double> outlier = new ArrayList<Double>();
		int i;
		vecteur = getSortedValues();
			for(i=0;i<dataset.n;i++) {
				if(vecteur.get(i)<=quartile(1)-1.5*IQR() | vecteur.get(i)>=quartile(3)+1.5*IQR()) {
				outlier.add(vecteur.get(i));
				}
			}

		return outlier;
	}
	
	public double arrondi(double val) {
		return Math.round(val * 10000.0)/10000.0;
	}
	
	
	@Override
	public String toString() {
		String text = nom_attribut + ":\n";
		try {
			text += "\t - moyenne   = " + arrondi(moyenne()) + "\n";
			text += "\t - ecartType = " + arrondi(ecartType()) + "\n";
			text += "\t - variance  = " + arrondi(variance()) + "\n";
			text += "\t - mediane   = " + arrondi(mediane()) + "\n";
			text += "\t - mode      = " + arrondi(mode()) + "\n";
			text += "\t - mode_disc = " + arrondi(mode_discret()) + "\n";
			text += "\t - max       = " + max() + "\n";
			text += "\t - min       = " + min() + "\n";
			text += "\t - etendu    = " + arrondi(etendu()) + "\n";
			text += "\t - mi_etendu = " + arrondi(milieu_etendu()) + "\n";
			text += "\t - skewness  = " + arrondi(skewness()) + "\n";
			text += "\t - Q1        = " + quartile(1) + "\n";
			text += "\t - Q2        = " + quartile(2) + "\n";
			text += "\t - Q3        = " + quartile(3) + "\n";
			text += "\t - IQR       = " + arrondi(IQR()) + "\n";
			text += "\t - outliers  = " + outliers() + "\n";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}
	
	private double m(int k) {
		/** used to calulate skewness(), ref = "https://pyshark.com/skewness-in-python/"
		 * */
		double m = 0;
		double moyenne = moyenne();
		for (int i = 0; i < dataset.n; i++) {
			m += Math.pow(dataset.data[i][indice_attribut] - moyenne, k);
		}
		return m / dataset.n;
	}
	
	public double skewness() {
		/** using Fisher-Pearson coefficient of skewness, ref = "https://pyshark.com/skewness-in-python/"
		 * skewness = m3 / (m2 ^ (1.5))*/
		return m(3) / Math.pow(m(2), 1.5);
	}
}
