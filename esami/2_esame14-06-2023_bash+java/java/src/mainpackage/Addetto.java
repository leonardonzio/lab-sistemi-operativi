package mainpackage;

import java.util.Random;

public class Addetto extends Thread {

	private Monitor m;
	private Random r;
	private boolean stop;	
	
	public Addetto(Monitor m, Random r) {
		
		this.m = m;
		this.r = r;
		this.stop = false;
	}
	
	@Override
	public void run() {
		
		while(!stop) {
			try {

				sleep(1000);
				m.chiude();
				sleep(3000);
				m.apre();
			} catch (InterruptedException e) {
				this.stop = true;
				System.out.println("addetto interrotto");
			}	

		}
	}
	
	
	
	
}
