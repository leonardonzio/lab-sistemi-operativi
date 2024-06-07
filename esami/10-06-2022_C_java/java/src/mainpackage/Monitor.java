package mainpackage;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
	
	private final int MAX_PACCHI = 20, NF = 1;
	private final static int N = 0;
	private final static int S = 1;
	
	private final static int A = 0;
	private final static int E = 1;

	private int[] scarichi = new int[2];
	private int numP;
	
	private Lock l = new ReentrantLock();
	private Condition[] codaC = new Condition[2];
	private Condition[] codaF = new Condition[2];
	private int[] sospC = new int[2];
	private int[] sospF = new int[2];
	
	
	public Monitor() {
		for (int i = 0; i < 2; i++) {
			scarichi[i] = 0;
			sospF[i] = 0;
			sospC[i] = 0;
			codaF[i] = l.newCondition();
			codaC[i] = l.newCondition();
		}
		numP = 0;
	}

	
	public void scaricaCamion(int dir, int nc) throws InterruptedException {
		l.lock();
		var dirStr = (dir == N) ? "Nord" : "Sud";
		var dirOpp = (dir == N) ? S : N;
		try {	
			// se ci stanno i pacchi nel deposito
			// se ci sono camion piu prioritari in attesa
			while (	nc + numP > MAX_PACCHI ||
					(scarichi[dirOpp] > scarichi[dir] && sospC[dirOpp] > 0)
					) {
				
				sospC[dir]++;
				System.out.println("sospeso camion con "+nc+" pacchi per "+dirStr);
				codaC[dir].await();
				sospC[dir]--;
			}
		
			System.out.println("deposito di "+nc+" pacchi per "+dirStr);
			scarichi[dir]++;
			numP += nc;
			
			// segnalo i furgoni
			if (numP >= NF) {
				if (sospF[A] > 0)
					codaF[A].signalAll();
				if (sospF[E] > 0)
					codaF[E].signalAll();
			}
			
			// segnalo anche camion con from opposta nel caso sia cambiata priorita subito dopo lo scarico
			if (sospC[dirOpp] > 0 && scarichi[dir] >= scarichi[dirOpp] + 1)
				codaC[dirOpp].signalAll();
			
		} finally {
			l.unlock();
		}
	}
	
	
	
	public void prelevaFurgone(int tipo) throws InterruptedException {
		l.lock();
		var tipoStr = (tipo == A) ? "Aziendale" : "Esterno";
		try {
			
			while (	numP < NF ||
					(tipo == E && sospF[A] > 0)
					) {
				sospF[tipo]++;
				System.out.println("sospeso furgone di tipo "+tipoStr);
				codaF[tipo].await();
				sospF[tipo]--;
			}
		
			System.out.println("prelievo di tipo "+tipoStr+" di "+NF+" pacchi");
			numP -= NF;
			
			//signal ai camion
			if (scarichi[S] > scarichi[N]) {
				if (sospC[N] > 0)
					codaC[N].signalAll();
				if (sospC[S] > 0)
					codaC[S].signalAll();
			}
			else {
				if (sospC[S] > 0)
					codaC[S].signalAll();
				if (sospC[N] > 0)
					codaC[N].signalAll();
			}
			
		} finally {
			l.unlock();
		}
	}
	
	
}
