package mesures;

import java.util.ArrayList;

import data.Dataset;

public class Mesures extends ArrayList<Mesures_attribut> {
	/** c'est juste un arrayliste de la Classe Mesure_attribut 
	 * */
	private static final long serialVersionUID = 1L;
	
	Dataset dataset;

	public Mesures(Dataset dataset) {
		super(dataset.m);
		this.dataset = dataset;
		for (int att = 0; att < dataset.m; att++) {
			this.add(new Mesures_attribut(dataset, att));
		}
	}
	
	@Override
	public String toString() {
		String text = "";
		for (int i = 0; i < dataset.m; i++) {
			text += get(i).toString() + "\n";
		}
		return text;
	}
}
