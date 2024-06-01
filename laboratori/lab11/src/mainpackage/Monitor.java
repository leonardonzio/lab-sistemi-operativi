package mainpackage;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.lang.model.element.ModuleElement.ExportsDirective;
import javax.naming.directory.DirContext;

public class Monitor {

	public final static int IN = 0;
	public final static int OUT = 1;
	
	private final int MAX_C = 10;
	private final int MAX_T = 15;
	
	private Lock l = new ReentrantLock();
	
	private int[] s = new int[2];
	private int[] p = new int[2];
	private int[] g = new int[2];
	
	private int[] sospS = new int[2];
	private int[] sospP = new int[2];
	private int[] sospG = new int[2];
	
	private Condition[] codaS = new Condition[2];
	private Condition[] codaP = new Condition[2];
	private Condition[] codaG = new Condition[2];
	
	private int totT;
	private int totC;
	private int num_guide_tomba;

	public Monitor() {

		totC = 0;
		totT = 0;
		num_guide_tomba = 0;
		Arrays.fill(s, 0);
		Arrays.fill(p, 0);
		Arrays.fill(g, 0);
		
		Arrays.fill(sospS, 0);
		Arrays.fill(sospP, 0);
		Arrays.fill(sospG, 0);
		
		for (int i = 0; i < 2; i++) {
			codaS[i] = l.newCondition();
			codaP[i] = l.newCondition();
			codaG[i] = l.newCondition();
		}
		
	}
	
	private boolean prioritaUscita() {
		return (sospS[OUT] + sospP[OUT] + sospG[OUT]) > 0;
	}
	
	public void entraS(int dir) throws InterruptedException {
		l.lock();
		try {			
			var dirString = (dir == IN) ? "IN" : "OUT";
			if (dir == IN) {
				while ( totC == MAX_C ||
						totT == MAX_T ||
						sospP[IN] + sospG[IN] > 0 ||
						prioritaUscita() ||
						num_guide_tomba == 0
						) {
					
					sospS[IN]++;
					codaS[IN].await();
					sospS[IN]--;
				}	
				
				totC++;
				totT++;
				s[IN]++;
			}			
			
			else {
				// OUT
				while ( totC == MAX_C || sospP[OUT] > 0 ) {
					
					sospS[OUT]++;
					codaS[OUT].await();
					sospS[OUT]--;
				}
				
				totC++;
				totT--;
				s[OUT]++;
			}
			System.out.println("singolo "+Thread.currentThread().threadId()+" entrato corridoio direzione"+dirString);
		} finally {
			l.unlock();
		}
	}
	
	
	
	public void esceS(int dir) {
		l.lock();
		try {
			
			var dirString = (dir == IN) ? "IN" : "OUT";
			totC--;
			s[dir]--;
			System.out.println("singolo "+Thread.currentThread().threadId()+" uscito corridoio direzione "+dirString);
			
			//ora devo segnalare a eventuali sospesi che si e liberato posto nel corriodio, nell'ordine di priorita
			if (sospP[OUT] > 0)
				codaP[OUT].signal();
			else if (sospS[OUT] > 0)
				codaS[OUT].signal();
			else if (sospG[OUT] > 0)
				codaG[OUT].signal();
			else if (sospG[IN] > 0)
				codaG[IN].signal();
			else if (sospP[IN] > 0)
				codaP[IN].signal();
			else if (sospS[IN] > 0)
				codaS[IN].signal();
			
		} finally {		
			l.unlock();
		}
	}
	
	
	
	public void entraP(int dir) throws InterruptedException {
		l.lock();
		try {			
			var dirString = (dir == IN) ? "IN" : "OUT";
			if (dir == IN) {
				while ( totC + 2 == MAX_C ||
						totT + 2 == MAX_T ||
						sospG[IN] > 0 ||
						prioritaUscita() ||
						num_guide_tomba == 0 ||
						p[OUT] > 0
						) {
					
					sospP[IN]++;
					codaP[IN].await();
					sospP[IN]--;
				}	
				
				totC+=2;
				totT+=2;
				p[IN]++;
			}			
			
			else {
				// pOUT (il piu prioritario)
				while ( totC + 2 == MAX_C || p[IN] > 0) {
					
					sospP[OUT]++;
					codaP[OUT].await();
					sospP[OUT]--;
				}
				
				totC+=2;
				totT-=2;
				p[OUT]++;
			}
			System.out.println("passeggino "+Thread.currentThread().threadId()+" entrato corridoio direzione "+dirString);
		} finally {
			l.unlock();
		}
	}

	
	
	
	
	public void esceP(int dir) {
		l.lock();
		
		try {
			
			var dirString = (dir == IN) ? "IN" : "OUT";
			totC-=2;
			p[dir]--;
			System.out.println("passeggino "+Thread.currentThread().threadId()+" uscito corridoio direzione "+dirString);
			
			//ora devo segnalare a eventuali sospesi che si e liberato posto nel corriodio, nell'ordine di priorita
			
			// se ci sono passeggini sospesi in dir out, e non ci sono passeggini in dir opposta nel corr, allora sveglio "n" passeggini
			if (sospP[OUT] > 0 && p[IN] == 0)
				codaP[OUT].signalAll();
			else if (sospS[OUT] > 0) {
				codaS[OUT].signal();
				codaS[OUT].signal();
			}
			else if (sospG[OUT] > 0) {
				codaG[OUT].signal();
				codaG[OUT].signal();
			}
			else if (sospG[IN] > 0) {
				codaG[IN].signal();
				codaG[IN].signal();
			}
			else if (sospP[IN] > 0 && p[OUT] == 0)
				codaP[IN].signalAll();
			else if (sospS[IN] > 0) {
				codaS[IN].signal();
				codaS[IN].signal();
			}
			
		} finally {		
			l.unlock();
		}
	}
	
	
	public void entraG(int dir) throws InterruptedException {
		l.lock();
		try {			
			var dirString = (dir == IN) ? "IN" : "OUT";
			if (dir == IN) {
				while ( totC == MAX_C ||
						totT == MAX_T ||
						prioritaUscita()
						) {
					
					sospG[IN]++;
					codaG[IN].await();
					sospG[IN]--;
				}	
				
				totC++;
				totT++;
				g[IN]++;
				num_guide_tomba++;
			}
			
			else {
				// OUT
				while ( (num_guide_tomba == 1 && totT - num_guide_tomba > 0) ||
						totC == MAX_C ||
						sospP[OUT] + sospS[OUT] > 0
						) {
					
					sospG[OUT]++;
					codaG[OUT].await();
					sospG[OUT]--;
				}
				
				totC++;
				totT--;
				num_guide_tomba--;
				g[OUT]++;
			}
			System.out.println("guida "+Thread.currentThread().threadId()+" entrato corridoio direzione "+dirString);

		} finally {
			l.unlock();
		}
	}
	
	
	public void esceG(int dir) {
		l.lock();
		try {
		
			var dirString = (dir == IN) ? "IN" : "OUT";
			totC--;
			g[dir]--;
			System.out.println("guida "+Thread.currentThread().threadId()+" uscita corridoio direzione "+dirString);
			
			// ora devo segnalare a eventuali sospesi che si e liberato posto nel corriodio, nell'ordine di priorita
			if (sospP[OUT] > 0)
				codaP[OUT].signal();
			else if (sospS[OUT] > 0)
				codaS[OUT].signal();
			else if (sospG[OUT] > 0)
				codaG[OUT].signal();
			else if (sospG[IN] > 0)
				codaG[IN].signal();
			else if (sospP[IN] > 0)
				codaP[IN].signal();
			else if (sospS[IN] > 0)
				codaS[IN].signal();
			
		} finally {		
			l.unlock();
		}
	}


	
	
}
