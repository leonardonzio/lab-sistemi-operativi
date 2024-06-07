package mainpackage;

import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Monitor {

	public final static int S = 0; //formato Singolo
	public final static int F = 1; //formato Famiglia
	private final int PMAX = 10;

	private Lock l = new ReentrantLock();
	private int pacchi;
	
	private int[] sospF = new int[2];
	private int[] sospC = new int[2];
	private Condition[] codaF = new Condition[2];
	private Condition[] codaC = new Condition[2];
	
	private boolean chiuso;
	
	public Monitor() {
		
		pacchi = 0;
		chiuso = false;
		Arrays.fill(sospF, 0);
		Arrays.fill(sospC, 0);
		
		for (int i = 0; i < 2; i++) {
			codaC[i] = l.newCondition();
			codaF[i] = l.newCondition();
		}
	}
	
	
	public void deposita (int tipo) throws InterruptedException {
		l.lock();
		try {
			var tipoStr = (tipo == S) ? "singolo" : "famiglia";
			while ( chiuso ||
					pacchi == PMAX ||
					(tipo == F && pacchi + 2 == PMAX) ||
					(tipo == F && sospF[S] > 0)
					) {
				
				sospF[tipo]++;
				System.out.println("sospeso deposito di tipo "+tipoStr);
				codaF[tipo].await();
				sospF[tipo]--;
			}
			System.out.println("deposito di tipo "+tipoStr);
			pacchi = (tipo == S) ? (pacchi + 1) : (pacchi + 3);
			
			if (tipo == S) {
				if (sospC[S] > 0 && sospC[F] == 0)
					codaC[S].signal();	
			} else {
				if (sospC[F] > 0)
					codaC[F].signal();
			}
			
		} finally {
			l.unlock();
		}
	}
	
	
	public void preleva (int tipo) throws InterruptedException {
		l.lock();
		try {
			var tipoStr = (tipo == S) ? "singolo" : "famiglia";
			while ( chiuso ||
					(tipo == S && sospC[F] > 0) ||
					(pacchi < ((tipo == S) ? 1 : 3))
					) {
				
				sospC[tipo]++;
				System.out.println("sospeso prelievo di tipo "+tipoStr);
				codaC[tipo].await();
				sospC[tipo]--;
			}
			System.out.println("prelievo di tipo "+tipoStr);
			pacchi = (tipo == S) ? (pacchi - 1) : (pacchi - 3);
			
			if (tipo == S) {
				if (sospF[S] > 0)
					codaF[S].signal();
				else if (sospF[F] > 0)
					codaF[F].signal();
			}else {
				// se ho prelevato un pacco famiglia, allora
				// si sono liberati 3 posti, quindi faccio
				// signalAll di tutti i fornitori singoli, dato
				// che si e' liberato piu' di un posto
				if (sospF[S] > 0)
					codaF[S].signalAll();
				if (sospF[F] > 0)
					codaF[F].signal();
			}
		} finally {
			l.unlock();
		}
	}

	
	public void chiude() {
		l.lock();
		try {
			System.out.println("!!!!!!!! chiudo tutto !!!!!!!!");
			chiuso = true;
			
		} finally {
			l.unlock();
		}

	}
	
	public void apre() {
		
		l.lock();		
		try {
			System.out.println("!!!!!!!! apro tutto !!!!!!!!");
			chiuso = false;
							
			if (sospC[F] > 0) 	codaC[F].signalAll();
			else 				codaC[S].signalAll();
			
			if (sospF[S] > 0)	codaF[S].signalAll();
			else 				codaF[F].signalAll();
		
		} finally {
			l.unlock();
		}
	}
	
	
	
}
	
