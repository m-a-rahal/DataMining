package data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Type {
	public int type = NUMERIQUE | QUANTITATIF ; // continue, par défaut
	public static final int ALL = 32 + 16 + 8 + 4 + 2 + 1;
	// les types dominants sont = 0
	public static final int NUMERIQUE 	= 1;
//	public static final int NON_BINAIRE = 2;
//	public static final int SYM       	= 4;
	public static final int DISCRET  	= 8;
	public static final int QUANTITATIF  = 16;
	public static final int NULL 		= ALL;
	public static final int _NOMINAL 	= ~NUMERIQUE;
//	public static final int _BINAIRE= ~NON_BINAIRE;
//	public static final int _ASYM     	= ~SYM;
	public static final int _CONTINUE  	= ~DISCRET;
	public static final int _QUALITATIF = ~QUANTITATIF;
	
	public Type(Integer type) {
		if (type != null) {
			this.type = type;
		}
	}
	
	public static Type parse(String str_value) {
		int type = 0;
		if (isLike(str_value, "[0-9]+")) {
			//System.out.println(str_value);
			return new Type(0); // entier
		}
		if (isLike(str_value, "[0-9]*\\.[0-9]+")) 
			return new Type(NUMERIQUE | QUANTITATIF); // réel
		if (str_value.toLowerCase().equals("null") || str_value.equals(""))
			return new Type(NUMERIQUE | QUANTITATIF | DISCRET);
		
		return new Type(0); // String
	}
	
	@Override
	public String toString() {
		String type_str = "";
		type_str += !is(DISCRET) ? "continu" : "discret"; type_str+=" | ";
		type_str += !is(NUMERIQUE) ? "nominal" : "numérique"; type_str+=" | ";
		type_str += !is(QUANTITATIF) ? "qualificatif" : "quantitatif";
		return type_str;
	}

	public boolean is(int category) {
		return (type & category) != 0;
	}

	public static boolean isLike(String text, String pattern) {
	    Pattern p = Pattern.compile(pattern);
		Matcher m;
		m = p.matcher(text);
		if(m.matches()) {
			return true;
		}
		else {
			return false;
		}
	}

	public Type combine(Type other) {
		/** retourne le type dominant : 
		 * continu + discret -> continu
		 * quantitatif + qualificatif -> qualificatif
		 */
		if (other == null) {
			return this;
		}
		return new Type(this.type & other.type);
	}

}

