package mainpackage;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Monitor {
	
	private final static int PR = 0, OD = 1;
	public final static int IN = 0, OUT = 1;
	private final int TOT_JJ = 10;
	private final int N_P = 3;
	private final int N_OD = 3;
	private final int MAX_C = 3;
	
	private int vaccini;
	private int[] operat_liber = new int[2];
	private int[] utentiC = new int[2];
	
	private int[] sospPR = new int[2]; 
	private int[] sospOD = new int[2]; 

	private Condition[] codaPR = new Condition[2];
	private Condition[] codaOD = new Condition[2];

	private Lock l = new ReentrantLock();
	
	public Monitor() {
		
		operat_liber[PR] = N_P;
		operat_liber[OD] = N_OD;
		vaccini = TOT_JJ;
		
		for (int i = 0; i < 2; i++) {
			utentiC[i] = 0;
			sospPR[i] = 0;
			sospOD[i] = 0;
			codaPR[i] = l.newCondition();
			codaOD[i] = l.newCondition();
		}
		
	}
	
	private void signalPriority() {
		
		// segnalo posto nel corridoio in ordine di priorita
		if (sospPR[OUT] > 0)
			codaPR[OUT].signalAll();
		if (sospOD[OUT] > 0)
			codaOD[OUT].signalAll();
		if (sospPR[IN] > 0)
			codaPR[IN].signalAll();
		if (sospOD[IN] > 0)
			codaOD[IN].signalAll();
	}
	
	public void entraPR(int dir) throws InterruptedException {
		l.lock();
		try {
			if (dir == IN) {
				
				while (	utentiC[OUT] > 0 ||
						utentiC[IN] + utentiC[OUT] == MAX_C ||
						operat_liber[PR] == 0 ||
						sospOD[OUT] + sospPR[OUT] > 0
						) {
					
					sospPR[dir]++;
					System.out.println("sospeso PR direzione in");
					codaPR[dir].await();				
					sospPR[dir]--;
				}
				
				System.out.println("entrato PR direzione in");
				utentiC[dir]++;
				operat_liber[PR]--;
			}
			
			else {
				// dir == OUT
				while (	utentiC[IN] > 0 || utentiC[IN] + utentiC[OUT] == MAX_C ) {
					
					sospPR[dir]++;
					codaPR[dir].await();
					System.out.println("sospeso PR direzione out");
					sospPR[dir]--;
				}
				
				System.out.println("entrato PR direzione out");
				utentiC[dir]++;
				operat_liber[PR]++;
			}
		} finally {
			l.unlock();
		}
	}
	
	
	
	public void escePR(int dir) {
		l.lock();
		try {
			
			utentiC[dir]--;
			var dirStr = (dir == IN) ? "in" : "out";
			System.out.println("uscito PR direzione "+dirStr);

			
			// segnalo !!
			signalPriority();

		} finally {
			l.unlock();
		}
		
		
		
	}
	
	public boolean entraOD(int dir) throws InterruptedException {
		l.lock();
		try {
			if (dir == IN) {
				
				if (vaccini == 0) {
					System.out.println("OD va a casa per mancanza di vaccini");
					
					// segnalo !!
					signalPriority();				
					l.unlock();
					return false; // ritrono falso perche non e riuscito ad entrare, cosi il main non fa i metodi succesivi
				}
				
				while (	utentiC[OUT] > 0 ||
						utentiC[IN] + utentiC[OUT] == MAX_C ||
						operat_liber[OD] == 0 ||
						sospOD[OUT] + sospPR[OUT] + sospPR[IN] > 0
						) {
					
					sospOD[dir]++;
					System.out.println("sospeso OD direzione in");
					codaOD[dir].await();					
					sospOD[dir]--;
				}
				
				System.out.println("entrato OD direzione in");
				utentiC[dir]++;
				operat_liber[OD]--;
				vaccini--;
			}
			
			else {
				// dir == OUT
				while (	utentiC[IN] > 0 || 
						utentiC[IN] + utentiC[OUT] == MAX_C ||
						sospPR[OUT] > 0
						) {
					
					sospOD[dir]++;
					System.out.println("sospeso OD direzione out");
					codaOD[dir].await();
					sospOD[dir]--;
				}
				
				System.out.println("entrato OD direzione out");
				utentiC[dir]++;
				operat_liber[OD]++;
			}
			
			return true;
		} finally {
			l.unlock();
		}
	}
	
	
	public void esceOD(int dir) {
		l.lock();
		try {
			
			utentiC[dir]--;
			var dirStr = (dir == IN) ? "in" : "out";
			System.out.println("uscito OD direzione "+dirStr);

			// segnalo !!
			signalPriority();

		} finally {
			l.unlock();
		}
	}


	
	
}
