package mainpackage;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Monitor {
	
	public final static int PUB = 0;
	public final static int PRI = 1;
	
	private final int MAX_A = 5;
	
	private Lock l = new ReentrantLock();

	private int auto;
	private int barche;
	private boolean up;
	
	private Condition[] codaA = new Condition[2];
	private Condition codaB;
	
	private int[] sospA = new int[2];
	private int sospB;
	
	public Monitor() {
	
		auto = 0;
		barche = 0;
		up = true; // partono le barche
		
		Arrays.fill(codaA, l.newCondition());
		codaB = l.newCondition();
		
		Arrays.fill(sospA, 0);
		sospB = 0;
	}
	
	
	public void entraB() throws InterruptedException {
		
		l.lock();
		try {
			while (auto > 0) {
				sospB++;
				System.out.println("barca "+Thread.currentThread().threadId()+" sospesa");
				codaB.await();
				sospB--;
			}
			up = true; //alzo il ponte
			barche++;
			
		} finally {
			l.unlock();
		}
		System.out.println("alzo ponte: barca "+Thread.currentThread().threadId()+" entrata");

	}
	
	
	public void entraA(int tipo) throws InterruptedException {
		
		l.lock();
		try {
			
			var tipoStr = (tipo == PUB) ? "pubblica" : "privata";
			while ( barche > 0 || 
					sospB > 0 || 
					auto == MAX_A ||
					(tipo == PRI && sospA[PUB] > 0)
					) {
				sospA[tipo]++;
				System.out.println("auto "+Thread.currentThread().threadId()+" di tipo "+tipoStr+" sospesa");
				codaA[tipo].await();
				sospA[tipo]--;
			}			
			up = false;
			auto++;
			System.out.println("abbasso ponte: auto "+Thread.currentThread().threadId()+" di tipo "+tipoStr+" entrata");
		} finally {
			l.unlock();
		}
		
	}
	
	
	
	public void esceB() {
		
		l.lock();
		try {
			barche--;
			System.out.println("barca "+Thread.currentThread().threadId()+" uscita");
			if (sospB > 0)
				codaB.signalAll();
			else if (sospA[PUB] > 0)
				codaA[PUB].signalAll();
			else if (sospA[PRI] > 0)
				codaA[PRI].signalAll();
			
		} finally {
			l.unlock();
		}
	}
	
	public void esceA(int tipo) {
		
		l.lock();
		try {
			var tipoStr = (tipo == PUB) ? "pubblica" : "privata";
			auto--;
			System.out.println("auto "+Thread.currentThread().threadId()+" di tipo "+tipoStr+" uscita");
			if (sospB > 0)
				codaB.signalAll();
			else if (sospA[PUB] > 0)
				codaA[PUB].signal();
			else if (sospA[PRI] > 0)
				codaA[PRI].signal();
			
		} finally {
			l.unlock();
		}
	}

}
