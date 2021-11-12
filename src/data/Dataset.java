package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public class Dataset {
	public Double data [][] = null;
	public int n; // dataset dimentions, lines and columns repectively
	public int m;
	HashMap<String, Integer> col_index; // used to get column index by column name
	public String col_names [];
	private static final String default_names = "area,perimeter,compactness,length of kernel,width of kernel,asymmetry coefficient,length of kernel groove,class";
	
	public Dataset(String[] names, List<String> data_lines) {
		// extract data from string lines
		extract_names(names);
		extract_data(data_lines);
	}
	
	private void extract_names(String[] names) {
		if (names == null) {
			col_names = default_names.split(",");
		}
		// init col_index
		int col = 0;
		col_index = new HashMap<>();
		for (String col_name : col_names) {
			col_index.put(col_name, col++);
		}
	}

	private void extract_data(List<String> data_lines) {
		/** extracts rows from space-separated lines (Strings)
		 * eg: 1.5 1.45 156.2 --> [1.5, 1.45, 156.2]
		 * */
		// detect data dimensions
		n = data_lines.size();
		m = data_lines.get(0).split("[\s\t]+").length;
		// allocate data matrix
		this.data = new Double[n][m];
		// fill data matrix with values
		int i = 0;
		for (String line : data_lines) {
			int j = 0;
			for (String str_value : line.split("[\s\t]+")) {
				data[i][j++] = Double.parseDouble(str_value);
			}i++;
		}

	}

	public double get(int line, String col_name) {
		int col = col_index.get(col_name);
		return get(line, col);
	}
	
	public double get(int line, int col) {
		return data[line][col];
	}
	
	public void changer_ligne() {

	}
	public void supprimer_ligne() {

	}
	public void ajouter_ligne() {
	}

	public void show() {
		for (String name : col_names) {
			System.out.print(name + "\t");
		}
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				System.out.print(data[i][j] + "\t");
			}
			System.out.println(""); // line break
		}
	}
	
	// ---- Mesures ---------------------------------------------------------------------

		public double moyenne(int indice_attribut) {
			double moy = 0.0;
			for (int i = 0; i < n; i++) {
				moy += data[i][indice_attribut];
			}
			moy /= n;
			return moy;
		}
		

		public double moyenne_tronqee(int indice_attribut, double quantile) {
				if (quantile >= 0.5) {
					throw new IllegalArgumentException("ne peut pas tronquer plus que 50% des deux côtés de la liste des valeurs");
				}
				double sup = quantile(indice_attribut,1-quantile);
				double inf = quantile(indice_attribut,quantile);
				
				double somme = 0;
				int count = 0;
				for (int i = 0; i < n; i++) {
					double x = data[i][indice_attribut];
					if (x >= inf && x <= sup) {
						somme += x;
						count++;
					}
				}
				
				return somme / count;
			}
		
		public double quantile(int indice_attribut, double q) {
			/** caculate quantile using R-2 approximation (averaging the two neighbors in case of conflict) (src = 'https://en.wikipedia.org/wiki/Quantile#:~:text=empirical%20distribution%20function.-,R%E2%80%912,-%2C%20SAS%E2%80%915%2C%20Maple')*/
			if (q < 0 || q > 1)
				throw new IllegalArgumentException("Le quantile doit être compris entre 0 et 1");
			if (q == 0)
				return min(indice_attribut);
			if (q == 1)
				return max(indice_attribut);
			ArrayList<Double> valeurs = getSortedValues(indice_attribut);
			double h = n * q + 0.5;
			int left = (int) Math.ceil(h - 0.5); left = Math.min(left, valeurs.size()-1);
			int right = (int) Math.floor(h + 0.5); right = Math.min(right, valeurs.size()-1);
			return (valeurs.get(left) + valeurs.get(right)) / 2;
		}

		public double variance(int indice_attribut) {
			double var;
			double moyenne = moyenne(indice_attribut);
			var = 0.0;
			for (int i = 0; i < n; i++) {
				var += Math.pow(data[i][indice_attribut] - moyenne, 2);
			}
			var /= n;
			return var;
		}

		public double ecartType(int indice_attribut) {
			return Math.sqrt(variance(indice_attribut));
		}
		
		public double coeffitient_de_correlation(int attribut1, int attribut2) {
			double moy1 = moyenne(attribut1);
			double moy2 = moyenne(attribut2);
			double var1 = variance(attribut1);
			double var2 = variance(attribut2);
			double cov = 0.0;
			for (int i = 0; i < n; i++) {
				cov += (data[i][attribut1] - moy1) * (data[i][attribut2] - moy2);
			}
			cov /= n;
			return cov / (Math.sqrt(var1) * Math.sqrt(var2));
		}
		
		public double milieu(int indice_attribut) {
			return (max(indice_attribut)+min(indice_attribut))/2;
		}
		
		public double mode(int indice_attribut) throws Exception {
			Double mod = null;
			Integer mod_freq = null;
			TreeMap<Double, Integer> frequences = new TreeMap<>();
			for (int i = 0; i < n; i++) {
				double val = data[i][indice_attribut];
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

		public double mode_discret(int indice_attribut) throws Exception {
			/** fait une discrétization des donnés avec des intervales de longeure = w
			 * tel que w = (max-min)/k
			 * k = round(   5*log10(nombre de valeurs)  )
			 * le mode est donc le milieu de l'intervale ayant la plus grande frequence
			*/
			double mod_discret;

			int k = (int) Math.round(5 * Math.log10(n));
			double max = max(indice_attribut);
			double min = min(indice_attribut);
			double w = (max - min) / k;
			int[] frequences = new int[k];
			for (int i = 0; i < n; i++) {
				int indice = (int) Math.floor((data[i][indice_attribut] - min) / w);
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

		public double mediane(int indice_attribut) throws Exception {
			double med;
			ArrayList<Double> vecteur = getSortedValues(indice_attribut);
			// si la taile de la liste est pair, retourner l'élement x(n/2), sinon la moyenne des deux élements au milieu
			int n = vecteur.size();
			if (n % 2 == 1) {
				med = (vecteur.get(n/2) + vecteur.get(n/2 - 1)) / 2;
			} else {
				med =  vecteur.get(n/2);
			}
			return med;
		}

		private ArrayList<Double> getSortedValues(int indice_attribut) {
			new ArrayList<>(n);
			ArrayList<Double> vecteur = new ArrayList<Double>();
			for (int i = 0; i < n; i++) {
				vecteur.add(data[i][indice_attribut]);
			}
			Collections.sort(vecteur);
			return vecteur;
		}

		public double min(int indice_attribut) {
			double min = data[0][indice_attribut];
			for (int i = 1; i < n; i++) {
				if (data[i][indice_attribut] < min) {
					min = data[i][indice_attribut];
				}
			}
			return min;
		}

		public double max(int indice_attribut) {
			double max = data[0][indice_attribut];
			for (int i = 1; i < n; i++) {
				if (data[i][indice_attribut] > max) {
					max = data[i][indice_attribut];
				}
			}
			return max;
		}
		
		public double etendu(int indice_attribut) {
			return max(indice_attribut)-min(indice_attribut);
		}
		
		public double milieu_etendu(int indice_attribut) {
			return (min(indice_attribut)+max(indice_attribut))/2;
		}
		
		public double quartile(int indice_attribut, int q) {
			if(q>3 | q<1) {
				throw new IllegalArgumentException("Le quartile doit être compris entre 1 et 3");
			}
			ArrayList<Double> vecteur = new ArrayList<Double>();
			vecteur = getSortedValues(indice_attribut);
			if(q==1) {
				return vecteur.get(n/4);
			}else if(q==2) {
				return vecteur.get(n/2);
			}else {
				return vecteur.get(3*n/4);
			}
			
		}
		
		public double IQR(int indice_attribut) {
			return quartile(indice_attribut,3)-quartile(indice_attribut,1);
		}
		
		public ArrayList<Double> outliers(int indice_attribut) {
			ArrayList<Double> vecteur = new ArrayList<Double>();
			ArrayList<Double> outlier = new ArrayList<Double>();
			int i;
			vecteur = getSortedValues(indice_attribut);
				for(i=0;i<n;i++) {
					if(vecteur.get(i)<=quartile(indice_attribut,1)-1.5*IQR(indice_attribut) | vecteur.get(i)>=quartile(indice_attribut,3)+1.5*IQR(indice_attribut)) {
					outlier.add(vecteur.get(i));
					}
				}

			return outlier;
		}
		
		public double arrondi(double val) {
			return Math.round(val * 10000.0)/10000.0;
		}
		

		public String mesures_string() {
			String text = "";
			for (int indice_attribut = 0; indice_attribut<m; indice_attribut++) {	
				text += "\n"+col_names[indice_attribut] + ":\n";
				try {
					text += "\t - moyenne   = " + arrondi(moyenne(indice_attribut)) + "\n";
					text += "\t - mediane   = " + arrondi(mediane(indice_attribut)) + "\n";
					text += "\t - mode      = " + arrondi(mode(indice_attribut)) + "\n";
					text += "\t - mode_disc = " + arrondi(mode_discret(indice_attribut)) + "\n";
					text += "\t - max       = " + max(indice_attribut) + "\n";
					text += "\t - min       = " + min(indice_attribut) + "\n";
					text += "\t - etendu    = " + arrondi(etendu(indice_attribut)) + "\n";
					text += "\t - mi_etendu = " + arrondi(milieu_etendu(indice_attribut)) + "\n";
					text += "\t - skewness  = " + arrondi(skewness(indice_attribut)) + "\n";
					text += "\t - Q1        = " + quartile(indice_attribut,1) + "\n";
					text += "\t - Q2        = " + quartile(indice_attribut,2) + "\n";
					text += "\t - Q3        = " + quartile(indice_attribut,3) + "\n";
					text += "\t - IQR       = " + arrondi(IQR(indice_attribut)) + "\n";
					text += "\t - outliers  = " + outliers(indice_attribut) + "\n";
					text += "\t - ecartType = " + arrondi(ecartType(indice_attribut)) + "\n";
					text += "\t - variance  = " + arrondi(variance(indice_attribut)) + "\n";
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return text;
		}
		
		private double m(int indice_attribut, int k) {
			/** used to calulate skewness(), ref = "https://pyshark.com/skewness-in-python/"
			 * */
			double m = 0;
			double moyenne = moyenne(indice_attribut);
			for (int i = 0; i < n; i++) {
				m += Math.pow(data[i][indice_attribut] - moyenne, k);
			}
			return m / n;
		}
		
		public double skewness(int indice_attribut) {
			/** using Fisher-Pearson coefficient of skewness, ref = "https://pyshark.com/skewness-in-python/"
			 * skewness = m3 / (m2 ^ (1.5))*/
			return m(indice_attribut, 3) / Math.pow(m(indice_attribut, 2), 1.5);
		}
	
	// ----------------------------------------------------------------------------------

	public int getClassCount() {
		return uniqueValues(m-1).length;
	}
	
	public Double [] uniqueValues(int indice_attribut) {
		TreeSet<Double> unique_values = new TreeSet<>();
		for (int i = 0; i < n; i++) {
			unique_values.add(data[i][indice_attribut]);
		}
		return (Double[]) unique_values.toArray();
	}
}
