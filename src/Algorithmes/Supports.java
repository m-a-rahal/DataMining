package Algorithmes;

import java.util.HashMap;

public class Supports extends HashMap<String, Integer>{
	public void ajouter(String item) {
		// compter le support d'un element
		Integer sup = get(item);
		if (sup == null) {
			put(item, 1);
		} else {
			put(item, sup+1);
		}
	}
}