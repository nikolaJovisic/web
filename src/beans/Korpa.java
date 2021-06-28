package beans;

import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;

public class Korpa {
	private Map<Artikal, Integer> artikli;
	private transient Korisnik korisnik;
	private BigDecimal cena;
	
	public Korpa(Korisnik korisnik) {
		artikli = new HashMap<Artikal, Integer>();
		this.korisnik = korisnik;
		cena = new BigDecimal(0);
	}
}
