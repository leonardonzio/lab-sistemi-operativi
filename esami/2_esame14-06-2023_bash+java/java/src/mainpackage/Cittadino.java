package mainpackage;

import java.util.Random;

public class Cittadino extends Thread {

	private Monitor m;
	private Random r;
	
	public Cittadino(Monitor m, Random r) {

		this.m = m;
		this.r = r;
	}
	
	@Override
	public void run(){
		
		try {
			var tipo = r.nextBoolean() ? Monitor.S : Monitor.F;
			sleep(1000);
			m.preleva(tipo);
			sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
}
