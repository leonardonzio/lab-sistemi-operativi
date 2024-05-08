package leo.es1_pizzeria;

public class Tavolo {

	private int pizze;
	private int calzoni;
	private int bibite;
	private double incasso;
	
	public Tavolo(int pizze, int calzoni, int bibite, double incasso) {

		if (pizze < 0 || calzoni < 0 || bibite < 0 || incasso < 0)
			throw new IllegalArgumentException("impossibile avere parametri negativi");
		
		this.pizze = pizze;
		this.calzoni = calzoni;
		this.bibite = bibite;
		this.incasso = incasso;
	}

	public Tavolo() {
		this(0, 0, 0, 0);
	}
	
	public synchronized boolean preleva (int pizze, int calzoni, int bibite) {
		
		if (this.pizze < pizze || this.calzoni < calzoni || this.bibite < bibite) {
			return false;
		}
		
		this.pizze -= pizze;
		this.calzoni -= calzoni;
		this.bibite -= calzoni;
		this.incasso +=	
				Prodotto.PIZZA.getPrezzo() * pizze +
				Prodotto.CALZONE.getPrezzo() * calzoni +
				Prodotto.BIBITA.getPrezzo() * bibite;
		return true;
	}
	
	public synchronized void depositaPizze(int pizze) {
		this.pizze += pizze;
	}
	
	public synchronized void depositaCalzoni(int calzoni) {
		this.calzoni += calzoni;
	}
	
	public synchronized void depositaBibite(int bibite) {
		this.bibite += bibite;
	}
	
	public synchronized void stampa(){
		 var sb = new StringBuilder();	
		 sb.append("Incasso totale: ").append(this.incasso).append(System.lineSeparator());
		 sb.append("Numero di pizze non vendute: ").append(this.pizze).append(System.lineSeparator());
		 sb.append("Numero di calzoni non venduti: ").append(this.calzoni).append(System.lineSeparator());
		 sb.append("Numero di bibite non vendute: ").append(this.bibite).append(System.lineSeparator());
		 
		 System.out.print(sb.toString());
	}
	
	
}
