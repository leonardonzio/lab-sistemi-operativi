package mainpackage;

import java.util.Random;

public class Furgone extends Thread {
	private Monitor m;
	private Random r;
	private int tipo;
	
	public Furgone(Monitor m, Random r) {
		this.m = m;
		this.r = r;
		this.tipo = r.nextInt(2);
	}
	
	
	@Override
	public void run() {
		
		try {
			sleep(1000 * r.nextInt(2) + 1);
			m.prelevaFurgone(tipo);
			sleep(1000 * r.nextInt(2) + 1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		

	}
	
	

}
