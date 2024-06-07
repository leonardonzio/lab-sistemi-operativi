package mainpackage;

import java.util.ArrayList;
import java.util.Random;

public class Main {

	public static void main(String[] args) throws InterruptedException {

		var m = new Monitor();
		var r = new Random();
		
		var autos = new ArrayList<Auto>();
		var barcas = new ArrayList<Barca>();
		
		for (int i = 0; i < r.nextInt(10) + 1; i++)
			autos.add(new Auto(m, r));
		
		for (int i = 0; i < r.nextInt(10) + 1; i++)
			barcas.add(new Barca(m));
		
		System.out.println("ci sono "+autos.size()+" auto e "+barcas.size()+" barche");
		for (Barca b : barcas)
			b.start();
		
		for (Auto a : autos)
			a.start();
		
		for (Auto a : autos)
			a.join();

		for (Barca b : barcas)
			b.join();

		System.out.println("programma terminato");		
	}

}
