package mainpackage;

import java.util.Random;

public class Singolo extends Thread{

	private Monitor m;
	private Random r;
	
	public Singolo (Monitor m, Random r) {

		this.m = m;
		this.r = r;
	}
	
	@Override
	public void run() {

		try {
			sleep(1000);
			m.entraS(Monitor.IN);
			sleep(1000);
			m.esceS(Monitor.IN);
			sleep(1000);
			m.entraS(Monitor.OUT);
			sleep(1000);
			m.esceS(Monitor.OUT);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	
}
