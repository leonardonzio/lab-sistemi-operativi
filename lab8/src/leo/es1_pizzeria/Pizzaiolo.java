package leo.es1_pizzeria;

import java.util.Random;

public class Pizzaiolo extends Thread{

	private Tavolo t;

	
	public Pizzaiolo(Tavolo t) {
		this.t = t;
	}
	
	@Override
	public void run() {
		
		var rand = new Random();
		while (rand.nextInt(5) != 0) {
			t.depositaPizze(rand.nextInt(11));
			t.depositaCalzoni(rand.nextInt(11));
			t.depositaBibite(rand.nextInt(11));	
		}
		
	}
	
	
	
	
	
}
