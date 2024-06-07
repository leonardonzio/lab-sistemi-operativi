package mainpackage;

import java.util.Random;

public class Fornitore extends Thread{

	private Monitor m;
	private Random r;
	private int tipo;
	private boolean stop;
	
	public Fornitore (Monitor m, Random r) {

		this.m = m;
		this.r = r;
		this.tipo = (r.nextBoolean()) ? Monitor.F : Monitor.S;
		this.stop = false;
	}
	
	@Override
	public void run() {
		
		while(!stop) {
			try {
				sleep(1000 * r.nextInt(5) + 1);
				m.deposita(tipo);
				sleep(1000);
			} catch (InterruptedException e) {
				this.stop = true;
				System.out.println("fornitore interrotto");
			}
		}
	}

	
	
}
