package mainpackage;

import java.util.Random;

public class Guida extends Thread{

	private Monitor m;
	private int quanteVolte;
	private Random r;
	
	public Guida(Monitor m, Random r) {

		this.m = m;
		this.r = r;
		this.quanteVolte = r.nextInt(20) + 1;
	}
	
	@Override
	public void run() {

		try {
			for(;quanteVolte > 0; quanteVolte--) {
				sleep(1000);
				m.entraG(Monitor.IN);
				sleep(1000);
				m.esceG(Monitor.IN);
				sleep(1000);
				m.entraG(Monitor.OUT);
				sleep(1000);
				m.esceG(Monitor.OUT);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
}
