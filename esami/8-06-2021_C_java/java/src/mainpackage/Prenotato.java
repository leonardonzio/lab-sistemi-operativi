package mainpackage;

import java.util.Random;

public class Prenotato extends Thread{
	private Monitor m;
	private Random r;
	
	public Prenotato (Monitor m, Random r) {
		this.m = m;
		this.r = r;
	}
	
	@Override
	public void run() {
		
		try {
			sleep(1000);
			m.entraPR(m.IN);
			sleep(1000);
			m.escePR(m.IN);
			sleep(1000);
			m.entraPR(m.OUT);
			sleep(1000);
			m.escePR(m.OUT);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
