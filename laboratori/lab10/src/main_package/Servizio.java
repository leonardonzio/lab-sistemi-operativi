package main_package;

import java.util.Arrays;
import java.util.Collection;

public enum Servizio {
	
	RINNOVO(0), PASSAGGIO(1);
	
	private int val;
	
	Servizio(int val) {
		this.val = val;
	}
	
	public int getVal() {
		return val;
	}
	
	
	public static Servizio getFromIndex(int indice) {
		for (Servizio s : Servizio.values()) {
			if (s.getVal() == indice)
				return s;
		}
		
		throw new IllegalArgumentException("Indice non valido: " + indice);
	}
}
