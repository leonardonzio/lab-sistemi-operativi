package mainpackage;

import java.util.Random;

public class OpenDay extends Thread{

	private Monitor m;
	private Random r;
	
	public OpenDay(Monitor m, Random r) {
		this.m = m;
		this.r = r;
	}
	
	@Override
	public void run() {
		
		try {
			sleep(1000);
			boolean entrato = m.entraOD(m.IN);
			if (entrato) {
				sleep(1000);
				m.esceOD(m.IN);
				sleep(1000);
				m.entraOD(m.OUT);
				sleep(1000);
				m.esceOD(m.OUT);
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	

}