package mainpackage;

import java.util.Random;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		
			var m = new Monitor();
			var r = new Random();
			var nF = r.nextInt(5) + 1;
			var nC = r.nextInt(5) + 1;
			
			var fornitori = new Fornitore[nF];
			var cittadini = new Cittadino[nC];
			var add = new Addetto(m, r); 
			
			System.out.println("ci sono "+nC+" cittadini, "+nF+" fornitori e 1 addetto.");
			for (int i = 0; i < nC; i++) {
				cittadini[i] = new Cittadino(m, r);
			}
			
			for (int i = 0; i < nF; i++) {
				fornitori[i] = new Fornitore(m, r);
			}
			
			// avvio i thread
			add.start();
			for (Cittadino c : cittadini) {
				c.start();
			}
			for (Fornitore f : fornitori) {
				f.start();
			}
						
			// quando cittadini han finito, interrompo addetto e fornitori
			for (Cittadino c : cittadini) {
				c.join();
			}
			add.interrupt();
			for (Fornitore f : fornitori) {
				f.interrupt();
			}
			
			// aspetto che tutti siano terminati per fare stampa finale
			add.join();
			for (Fornitore f : fornitori) {
				f.join();
			}
			
			System.out.println("terminato il programma");
	}

}
