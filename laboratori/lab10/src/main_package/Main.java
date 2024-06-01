package main_package;

import java.util.ArrayList;

public class Main {

	private final static int NUM_UTENTI = 10;
	public static void main(String[] args) {

		var utenti = new ArrayList<Utente>();
		var m = new Monitor();
		
		for (int i = 0; i < NUM_UTENTI; i++)
			utenti.add(new Utente(m));
		
		
		utenti.forEach(u -> u.start());
		
		for (Utente u : utenti) {
			try {
				u.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		m.stampa();
		
	}

}
