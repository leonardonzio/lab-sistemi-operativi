package mainpackage;

import java.util.Random;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		var m = new Monitor();
		var r = new Random();
		var nPR = r.nextInt(5) + 1;
		var nOD = r.nextInt(5) + 1;
		
		var prenotatis = new Prenotato[nPR];
		var opendayss = new OpenDay[nOD];
		
		System.out.println("ci sono "+nPR+" prenotati e "+nOD+" opendays.");
		for (int i = 0; i < nPR; i++) {
			prenotatis[i] = new Prenotato(m, r);
		}
		
		for (int i = 0; i < nOD; i++) {
			opendayss[i] = new OpenDay(m, r);
		}
		
		
		for (Prenotato p : prenotatis) {
			p.start();
		}
		for (OpenDay o : opendayss) {
			o.start();
		}
		
		

	}

}
