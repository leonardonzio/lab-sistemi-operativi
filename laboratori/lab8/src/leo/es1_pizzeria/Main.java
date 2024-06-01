package leo.es1_pizzeria;

import java.util.ArrayList;

public class Main {

	private final static int NUM_PIZZAIOLI = 1;
	private final static int NUM_FATTORINI = 10;
	
	public static void main(String[] args) {
		
		var tavolo = new Tavolo();
		var pizzaioli = new ArrayList<Pizzaiolo>();
		var fattorini = new ArrayList<Fattorino>();
		
		for (int i = 0; i < NUM_PIZZAIOLI; i++)
			pizzaioli.add(new Pizzaiolo(tavolo));
		
		for (int i = 0; i < NUM_FATTORINI; i++)
			fattorini.add(new Fattorino(tavolo));
		
		//starto
		for (Pizzaiolo p : pizzaioli)
			p.start();
		
		for (Fattorino f : fattorini)
			f.start();
		
		//attendo terminazione
		for (Pizzaiolo p : pizzaioli) {
			try {
				p.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		for (Fattorino f : fattorini) {
			try {
				f.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		//infine stampo
		tavolo.stampa();
	}

}
