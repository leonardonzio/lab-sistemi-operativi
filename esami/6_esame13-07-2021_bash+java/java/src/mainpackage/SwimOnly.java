package mainpackage;

import java.util.Random;

public class SwimOnly extends Thread{

	private Monitor m;
	private Random r;
	
	public SwimOnly (Monitor m, Random r) {

		this.m = m;
		this.r = r;
	}
	
	@Override
	public void run() {
		try {
			sleep(r.nextInt(5)+1 * 1000);
			m.entraS_pisc();
			sleep(r.nextInt(5)+1 * 1000);
			m.esceS_pisc();

		} catch (Exception e) {
			e.printStackTrace();		}	
	
	}
	
	

	
}