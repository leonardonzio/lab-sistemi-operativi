package mainpackage;

import java.util.Random;

public class Guida extends Thread{

	private Monitor m;
	private Random r;
	
	public Guida(Monitor m, Random r) {

		this.m = m;
		this.r = r;
	}
	
	@Override
	public void run() {

		try {
			while (true){
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
			System.out.println("guida interrotta");
		}
	}
	
	
}
