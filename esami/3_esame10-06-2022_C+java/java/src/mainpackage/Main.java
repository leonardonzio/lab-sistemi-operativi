package mainpackage;

import java.util.Random;

public class Main {

	public static void main(String[] args) {
		
		var m = new Monitor();
		var r = new Random();
		var nF = r.nextInt(5) + 1;
		var nC = r.nextInt(5) + 1;
		
		var camions = new Camion[nC];
		var furgonis = new Furgone[nF];
		
		System.out.println("ci sono "+nC+" camion e "+nF+" furgoni.");
		for (int i = 0; i < nF; i++) {
			furgonis[i] = new Furgone(m, r);
		}
		
		for (int i = 0; i < nC; i++) {
			camions[i] = new Camion(m, r);
		}
		
		
		for (Camion c : camions) {
			c.start();
		}
		for (Furgone f : furgonis) {
			f.start();
		}

		for (Camion c : camions) {
			try {
				c.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for (Furgone f : furgonis) {
			try {
				f.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("finito");
		
		
				

	}

}
