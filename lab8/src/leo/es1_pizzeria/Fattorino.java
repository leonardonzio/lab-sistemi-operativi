package leo.es1_pizzeria;

import java.util.Random;

public class Fattorino extends Thread{
	
	private Tavolo t;
	
	public Fattorino(Tavolo t) {
		this.t = t;
	}
	
	@Override
	public void run() {
		var rand = new Random();
		if (! t.preleva(rand.nextInt(11), rand.nextInt(11), rand.nextInt(11))) {
			System.out.println("Ordine non riuscito");
			return;
		}
		
		System.out.println("Ordine riuscito");
	}
	
	
}
