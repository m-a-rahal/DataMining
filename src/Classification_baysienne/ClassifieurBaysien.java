package Classification_baysienne;

import java.util.HashMap;
import java.util.HashSet;

import javax.swing.table.TableModel;


public class ClassifieurBaysien{
	public Index index;
	public int n,m;
	public ClassifieurBaysien(TableModel model, int n, int m) {
		this.n = n; this.m = m;
		this.index = analyser(model);
	}
	
	public double p_classe(String classe) {
		return index.taille(classe)/(double)n;
	}
	
	public double p(String x, String classe) {
		return index.taille(x, classe)/(double)index.taille(classe);
	}
	
	public double p_laplace(String x, String classe) {
		return (index.taille(x, classe) + 1)/(double)(index.taille(classe) + index.valeurs_possibles.get(x).size());
	}

	private Index analyser(TableModel model) {
		Index index = new Index();
		for (int i = 0; i < n; i++) {
			String classe = model.getValueAt(i, m-1).toString();
			index.incrementer(classe); // compter la taille de la classe
			for (int j = 0; j < m-1; j++) {
				String x = model.getValueAt(i, j).toString();
				index.incrementer(x, classe); // compter la taille de l'attribut dans la classe
			}
		}
		return index;
	}
	
	public static class Index extends HashMap<String, Integer> {
		ValeursPossibles valeurs_possibles = new ValeursPossibles();
		private static final long serialVersionUID = 1L;
		public Integer taille(String x, String classe) {return taille(key(x,classe));}
		public Integer taille(String classe) {
			Integer val = super.get(classe);
			if (val == null) return 0;
			else return val;
		}
		public Integer put(String x, String classe, Integer value) {return super.put(key(x,classe), value);}
		public void incrementer(String x, String classe) {
			incrementer(key(x, classe));
			valeurs_possibles.ajouter(x);
		}
		public void incrementer(String classe) {
			Integer taille = taille(classe);
			if(taille == null) taille = 0;
			put(classe, taille+1);
		}
		private String key(String x, String classe) {return x+" "+classe;}
	}
	static class ValeursPossibles extends HashMap<Integer, HashSet<String>> {
		private static final long serialVersionUID = 1L;

		public void ajouter(String x) {
			int j = attribut_de(x); // détecter l'attribut
			ajouter(j, x);
		}
		
		public HashSet<String> get(String x) {
			// TODO Auto-generated method stub
			return super.get(attribut_de(x));
		}

		public void ajouter(int j, String x) {
			HashSet<String> valeurs = get(x);
			if (valeurs == null) {
				valeurs = new HashSet<>();
				put(j, valeurs);
			}
			valeurs.add(x);
		}
		
		public Integer attribut_de(String x) {
			return Integer.parseInt(""+x.charAt(1));
		}
	}
}
