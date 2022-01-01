package Classification;

import java.util.ArrayList;
import java.util.TreeMap;
import Classification.Classifieur.Classification;
import Classification.Classifieur.Instance;

public class MatriceConfusion extends TreeMap<String, Integer> {
	private static final long serialVersionUID = 1L;
	public ArrayList<String> classes;
	
	public MatriceConfusion(Classification resultats) {
		this.classes = resultats.classes;
        for(Instance instance : resultats.values()) {
            ajouter(instance.classe_predite, instance.classe_correcte);
        }
	}

	public Integer get(int i, int j) {
		return get(classes.get(i), classes.get(j));
	}
		
	public void ajouter(String c1, String c2) {
		String key = key(c1,c2);
		put(key, getOrDefault(key, 0) + 1);
	}
	public Integer get(String c1, String c2) {
		return super.getOrDefault(key(c1, c2), 0);
	}

	private String key(String c1, String c2) {
		return c1 + " "+ c2;
	}
}
