package main_package;

import java.util.OptionalInt;
import java.util.Random;

public class Utente extends Thread {

	private Monitor m;
	Random r = new Random();
	
	public Utente(Monitor m) {
		this.m = m;
	}
	
	@Override
	public void run() {
		
		Servizio s = Servizio.getFromIndex(r.nextInt(2));
		try {
			
			sleep(1000);
			m.entraSala();
			sleep(1000);
			OptionalInt s_occupato = m.richiedeSportello(s);
			sleep(1000);
			m.esce(s_occupato.getAsInt());
			sleep(1000);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
	}
}
