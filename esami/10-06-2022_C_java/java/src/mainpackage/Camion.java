package mainpackage;

import java.util.Random;

public class Camion extends Thread {
		
	private Monitor m;
	private Random r;
	private int from;
	
	public Camion(Monitor m, Random r) {
		this.m = m;
		this.r = r;
		this.from = r.nextInt(2);
	}
	
	
	@Override
	public void run() {
		
		try {
			sleep(1000 * r.nextInt(2) + 1);
			m.scaricaCamion(from, r.nextInt(5) + 1);
			sleep(1000 * r.nextInt(2) + 1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		

	}
	
	
}
