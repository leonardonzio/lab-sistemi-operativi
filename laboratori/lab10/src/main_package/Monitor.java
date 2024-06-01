package main_package;

import java.util.ArrayList;
import java.util.OptionalInt;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
	
	private final static int NUM_SPORTELLI = 3; // NS
	private final static int NUM_SEDIE = 6; // MAX UTENTI

	private Lock lock = new ReentrantLock();
	private Condition coda_sedie, coda_sportelli;
	private int sospesi_sala, sospesi_sportello;
	private int n_rinnovi, n_passaggi, sportelli_liberi, sedie_libere;
	private ArrayList<Sportello> sportelli = new ArrayList<>(); 
	
	public Monitor() {

		for (int i = 0; i < NUM_SPORTELLI; i++)
			sportelli.add(Sportello.LIBERO);

		n_passaggi = 0;
		n_rinnovi = 0;
		sportelli_liberi = NUM_SPORTELLI;
		sedie_libere = NUM_SEDIE;
		sospesi_sala = 0;
		sospesi_sportello = 0;
		coda_sedie = lock.newCondition();
		coda_sportelli = lock.newCondition();
	}
	
	public void entraSala() {

		lock.lock();
		try {
			while (sedie_libere == 0) {
				sospesi_sala++;
				System.out.println("utente in attesa...");
				coda_sedie.await();
				sospesi_sala--;
			}
			System.out.println("utente entrato in sala");
			sedie_libere--;
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return;
	}
	
	
	public OptionalInt richiedeSportello(Servizio s) {
		
		lock.lock();
		OptionalInt indiceOccupato = OptionalInt.empty();
		try {

			while (sportelli_liberi == 0) {
				sospesi_sportello++;
				System.out.println("Utente in attesa di sportello per il servizio "+ s.toString());
				coda_sportelli.await();
				sospesi_sportello--;
			}
			
			// gli do lo sportello:
			for (int i = 0; i < sportelli.size(); i++) {
				
				if (sportelli.get(i) == Sportello.LIBERO) {		
					sportelli.set(i, Sportello.OCCUPATO);
					indiceOccupato = OptionalInt.of(i);
					break;
				}
			}
						
			sportelli_liberi--;
			sedie_libere++;
			if (s == Servizio.PASSAGGIO)
				n_passaggi++;
			else
				n_rinnovi++;
			
			System.out.println("Utente  mi e' stato assegnato lo sportello " + indiceOccupato.orElse(-1));
			
			// risveglio thread che si era spento a causa di mancanza di posti,
			// dato che ora si e liberato un posto
			if (sospesi_sala > 0)
				coda_sedie.signal();
			
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return indiceOccupato;
	
	}
	
	
	
	public void esce(int indice) {
		
		lock.lock();
		
		sportelli_liberi++;
		sportelli.set(indice, Sportello.LIBERO);
		if (sospesi_sportello > 0)
			coda_sportelli.signal();
	
		System.out.println("Utente sono uscito liberando lo sportello "+ indice);
		
		lock.unlock();
	}
	
	

	public void stampa() {
		
		lock.lock();
		System.out.println("Situazione finale:\n Sono stati serviti " + (n_passaggi + n_rinnovi) + " utenti");
		System.out.println("Servizi erogati: \n Rinnovi = "+ n_rinnovi +", \t Passaggi = "+n_passaggi + ".");
		
		if (sospesi_sportello > 0)
			coda_sportelli.signal();

		lock.unlock();
	}
	
	
}
