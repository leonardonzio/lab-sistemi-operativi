package leo.es1_pizzeria;

public enum Prodotto {

	PIZZA(10),
	CALZONE(7),
	BIBITA(3);
	
	private final int prezzo;
	
	private Prodotto(int prezzo) {
		this.prezzo = prezzo;
	}

	public int getPrezzo() {
		return this.prezzo;
	}
	
	
}
