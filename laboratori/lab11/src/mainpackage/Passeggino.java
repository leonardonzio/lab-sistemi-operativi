package mainpackage;

import java.util.Random;

public class Passeggino extends Thread {
	private Monitor m;
	private Random r;
	
	public Passeggino (Monitor m, Random r) {

		this.m = m;
		this.r = r;
	}
	
	@Override
	public void run() {

		try {
			sleep(1000);
			m.entraP(Monitor.IN);
			sleep(1000);
			m.esceP(Monitor.IN);
			sleep(1000);
			m.entraP(Monitor.OUT);
			sleep(1000);
			m.esceP(Monitor.OUT);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
