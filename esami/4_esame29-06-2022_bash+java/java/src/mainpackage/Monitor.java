package mainpackage;

import java.util.concurrent.locks.Condition;

public class Monitor {
	
	private final static int A = 0, B = 1;
	private final int MAX_V = 10;
	private int nV, nG;
	private int[] sospV_IN = new int[2];
	private int[] sospV_OUT = new int[2];
	
	private int sospG_IN, sospG_OUT;
	private Condition[] codaV_IN = new Condition[2];
	private Condition[] codaV_OUT = new Condition[2];

	private Condition codaG_IN, codaG_OUT;
	
	private Lock l = new ReentrantLock();
	
	public Monitor() {
		
		nG = 0;
		nV = 0;
		sospG_IN = 0;
		sospG_OUT = 0;
		for (int i = 0; i < sospV_IN.length; i++) { // 2 volte
			sospV_IN[i] = 0;
			codaV_IN[i] = l.newCondition();
			sospV_OUT[i] = 0;
			codaV_OUT[i] = l.newCondition();
		}
		
		codaG_IN = l.newCondition();
		codaG_OUT = l.newCondition();
	}
	
	
	public void entraV(int tipo) throws InterruptedException {
		l.lock();
		var tipoStr = (tipo == A) ? "Adulto" : "Bambino";
		try {
			while ( nG < (nV + 1)/5 ||
					(tipo == B && sospV_IN[A] > 0) ||
					nV == MAX_V
					) {

				sospV_IN[tipo]++;
				System.out.println("sospeso entrata di "+tipoStr);
				codaV_IN[tipo].await();
				sospV_IN[tipo]--;
			}
			
			System.out.println("entrato "+tipoStr);
			nV++;
			
		} finally {
			l.unlock();
		}	
	}
	
	
	
	public void esceV(int tipo) throws InterruptedException {
		l.lock();
		try {
			while (	(tipo == A && sospV_OUT[B] > 0) ||
					nG < (nV - 1)/5
					) {
				
				sospV_OUT[tipo]++;
				codaV_OUT[tipo].await();
				sospV_OUT[tipo]--;
			}
			
			nV--;
			
			if (sospV_IN[A] > 0)
				codaV_IN[A].signal();
			else if (sospV_IN[B] > 0)
				codaV_IN[B].signal();
			
			if (sospG_OUT > 0)
				codaG_OUT.signal();
			
		} finally {
			l.unlock();
		}	
	}

	public void entraG() throws InterruptedException {
		l.lock();
		try {
			while ( nG + 1 < (nV)/5 ) {
				sospG_IN++;
				System.out.println("sospesa entrata di Guida");
				codaG_IN.await();
				sospG_IN--;
			}
			
			System.out.println("entrata Guida");
			nG++;
			
		} finally {
			l.unlock();
		}	
	}
	
	public void esceG() throws InterruptedException {
		l.lock();
		try {
			while ( sospV_OUT[B] > 0 ||
					sospV_OUT[A] > 0 ||
					nG - 1 < (nV - 1)/5
					) {
				
				sospG_OUT++;
				codaG_OUT.await();
				sospG_OUT--;
			}
			
			nG--;
			
			if (sospG_IN > 0)
				codaG_IN.signal();
			if (sospG_OUT > 0)
				codaG_OUT.signal();
			
		} finally {
			l.unlock();
		}	
	}

	
	
}
