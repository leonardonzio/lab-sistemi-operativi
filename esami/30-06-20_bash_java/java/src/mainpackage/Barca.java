package mainpackage;

import java.util.Random;

public class Barca extends Thread {
	private Monitor m;
	
	public Barca(Monitor m) {
		this.m = m;
	}
	
	
	
	@Override
	public void run() {
		
		try {
			
			m.entraB();
			sleep(1000);
			m.esceB();
			sleep(1000);
			
		} catch (InterruptedException e){
			e.printStackTrace();
		}
	
	
	
	}

}
