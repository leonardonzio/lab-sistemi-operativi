package mainpackage;

import java.util.Arrays;
import java.util.Random;

import javax.management.loading.PrivateClassLoader;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		
		var m = new Monitor();
		var r = new Random();
		int nS = r.nextInt(10) + 1;
		int nP = r.nextInt(10) + 1;
		int nG = r.nextInt(10) + 1;
		var singoli = new Singolo[nS];
		var passeggini = new Passeggino[nP];
		var guide = new Guida[nG];
		
		System.out.println("ci sono "+nS+" singoli, "+nP+" passeggini e "+nG+" guide.");
		
		for (int i = 0; i < nS; i++)
			singoli[i] = new Singolo(m, r);
		for (int i = 0; i < nP; i++)
			passeggini[i] = new Passeggino(m, r);
		for (int i = 0; i < nG; i++)
			guide[i] = new Guida(m, r);
		
		for (Guida g : guide)
			g.start();
		for (Singolo s : singoli) 
			s.start();
		for (Passeggino p : passeggini)
			p.start();
		
		
		for (Guida g : guide)
			g.join();
		for (Singolo s : singoli) 
			s.join();
		for (Passeggino p : passeggini)
			p.join();

		System.out.println("testo per verificare se ha terminato");
		
	}

}
