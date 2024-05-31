package mainpackage;

import java.util.Random;

public class Auto extends Thread {

	private Monitor m;
	private int tipo;
	private Random r;
	
	public Auto(Monitor m, Random r) {

		this.m = m;
		this.r = r;
		this.tipo = r.nextBoolean() ? Monitor.PUB : Monitor.PRI;
	}
	
	
	
	@Override
	public void run() {
		
		try {
			
			m.entraA(tipo);
			sleep(1000);
			m.esceA(tipo);
			sleep(1000);
			
		} catch (InterruptedException e){
			e.printStackTrace();
		}
	
	
	
	}
}
