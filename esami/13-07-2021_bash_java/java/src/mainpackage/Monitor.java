package mainpackage;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
	
	public final static int A = 0, O = 1;
	private final int MAX_SPIAG = 3, MAX_PISC = 3;
	
	private int n_pis, n_spi;
	
	private int sospS;
	private int[] sospF_pisc = new int[2]; // F_A e F_O
	private int[] sospF_spiag = new int[2]; // F_A e F_O

	private Condition[] codaF_pisc = new Condition[2]; // F_A e F_O
	private Condition[] codaF_spiag = new Condition[2]; // F_A e F_O
	private Condition codaS;
	
	private Lock l = new ReentrantLock();
	
	
	public Monitor() {
	
		n_pis = 0;
		n_spi = 0;
		sospS = 0;
		codaS = l.newCondition();
		
		for (int i = 0; i < 2; i++) {
			sospF_pisc[i] = 0;
			sospF_spiag[i] = 0;
			codaF_pisc[i] = l.newCondition();
			codaF_spiag[i] = l.newCondition();
		}
	
	}
	
	public void entraS_pisc() throws InterruptedException {
		l.lock();
		try {
			while (	n_pis == MAX_PISC ) {
				sospS++;
				System.out.println("sospeso swim-only da entrata piscina");
				codaS.await();
				sospS--;
			}
			n_pis++;
			System.out.println("entrato swim-only in piscina");
			
		} finally {
			l.unlock();
		}
	}
	
	
	public void esceS_pisc() {
		l.lock();
		try {
			
			n_pis--;
			System.out.println("uscito swim-only in piscina");

			// segnalo che si puo entrare in piscina ad eventuali sospesi, in ordine di priorita'
			if (sospS > 0)
				codaS.signal();
			else if (sospF_pisc[A] > 0 && sospS == 0)// devo verific che non ci siano pero ancora swimonly sospesi, perche prima facciamo signal di solo 1
				codaF_pisc[A].signal();
			else if (sospF_pisc[O] > 0 && sospS == 0 && sospF_pisc[A] == 0)// anche qua, oltre a swimonly == 0 anche i sospF_spiag[A], perche prima abbiamo fatto una singola signal
				codaF_pisc[O].signal();
			
		} finally {
			l.unlock();
		}
	}
	
	
	public void entraF_pisc(int tipo) throws InterruptedException {
		l.lock();
		var tipoStr = (tipo == A) ? "abbonato" : "occasionale";
		try {		
			while ( n_pis == MAX_PISC ||
					sospS > 0 ||
					(tipo == O && sospF_pisc[A] > 0)
					) {
				sospF_pisc[tipo]++;
				System.out.println("sospeso full "+tipoStr+" da entrata piscina");
				codaF_pisc[tipo].await();
				sospF_pisc[tipo]--;
			}
			
			n_pis++;
			System.out.println("entrato full "+tipoStr+" in piscina");
			
		} finally {
			l.unlock();
		}
	}
	
	
	
	public void esceF_pisc(int tipo) {
		l.lock();
		var tipoStr = (tipo == A) ? "abbonato" : "occasionale";
		try {
			
			n_pis--;
			System.out.println("uscito full "+tipoStr+" da piscina");

			// segnalo che si puo entrare in piscina ad eventuali sospesi, in ordine di priorita'
			if (sospS > 0)
				codaS.signal();
			else if (sospF_pisc[A] > 0 && sospS == 0)
				codaF_pisc[A].signal();
			else if (sospF_pisc[O] > 0 && sospS == 0 && sospF_pisc[A] == 0)
				codaF_pisc[O].signal();
			
		} finally {
			l.unlock();
		}
	}
	
	
	
	public void entraF_spiag(int tipo) throws InterruptedException {
		l.lock();
		var tipoStr = (tipo == A) ? "abbonato" : "occasionale";
		try {		
			while ( n_spi == MAX_SPIAG ||
					(tipo == O && sospF_spiag[A] > 0)
					) {
			
				sospF_spiag[tipo]++;
				System.out.println("sospeso full "+tipoStr+" da entrata spiaggia");
				codaF_spiag[tipo].await();
				sospF_spiag[tipo]--;
			}
		
			n_spi++;
			System.out.println("entrato full "+tipoStr+" in spiaggia");
		
		} finally {
			l.unlock();
		}
	}

	
	public void esceF_spiag(int tipo) {
		l.lock();
		var tipoStr = (tipo == A) ? "abbonato" : "occasionale";
		try {
			
			n_spi--;
			System.out.println("uscito full "+tipoStr+" da spiaggia");

			// segnalo che si puo entrare in spiaggia ad eventuali sospesi, in ordine di priorita'
			if (sospF_spiag[A] > 0)
				codaF_spiag[A].signal();
			else if (sospF_spiag[O] > 0 && sospF_spiag[A] == 0)// controllo che non ci siano neanche sospesi di itpo A, perche abbimao fatto uan singola signal, quindi potrebbero essercene altri
				codaF_spiag[O].signal();
			
		} finally {
			l.unlock();
		}
	}	
	
	
	
}
