package mainpackage;

import java.util.Random;

public class Full extends Thread {
	
	private Monitor m;
	private Random r;
	
	public Full (Monitor m, Random r) {

		this.m = m;
		this.r = r;
	}
	
	@Override
	public void run() {
		
		int tipo = r.nextBoolean() ? m.A : m.O;
		try {
			
			sleep(r.nextInt(5)+1 * 1000);
			m.entraF_spiag(tipo);
			
			int howManyTimes = r.nextInt(5) + 1;
			for (int i = 0; i < howManyTimes; i++) {	
				
				sleep(r.nextInt(5)+1 * 1000);
				var goPisc = r.nextBoolean();
				
				if (goPisc) {
					m.entraF_pisc(tipo);
					sleep(1000);
					m.esceF_pisc(tipo);
					
				}
				
				sleep(r.nextInt(5)+1 * 1000);				
			}
			
			sleep(r.nextInt(5)+1 * 1000);
			m.esceF_spiag(tipo);

		} catch (Exception e) {
			e.printStackTrace();		}	
	
	}

}
