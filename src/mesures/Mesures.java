package mesures;

import java.util.Iterator;

import data.Dataset;

public class Mesures {
	Dataset dataset;
	Mesures_attribut attribut[]; 

	public Mesures(Dataset dataset) {
		this.dataset = dataset;
		attribut = new Mesures_attribut[dataset.m];
		for (int att = 0; att < dataset.m; att++) {
			attribut[att] = new Mesures_attribut(dataset, att);
		}
	}
	
	public Mesures_attribut getAttribut(int i) {
		return attribut[i];
	}
	
	@Override
	public String toString() {
		String text = "";
		for (int i = 0; i < dataset.m; i++) {
			text += attribut[i].toString() + "\n";
		}
		return text;
	}
}
