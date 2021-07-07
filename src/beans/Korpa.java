package beans;

import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;

public class Korpa {
	private Map<Artikal, Integer> artikli;
	private transient Kupac kupac;
	private BigDecimal cena;
	
	public Korpa(Map<Artikal, Integer> artikli, Kupac kupac, BigDecimal cena) {
		super();
		this.artikli = artikli;
		this.kupac = kupac;
		this.cena = cena;
	}

	public Map<Artikal, Integer> getArtikli() {
		return artikli;
	}

	public void setArtikli(Map<Artikal, Integer> artikli) {
		this.artikli = artikli;
	}

	public Kupac getKupac() {
		return kupac;
	}

	public void setKupac(Kupac kupac) {
		this.kupac = kupac;
	}

	public BigDecimal getCena() {
		return cena;
	}

	public void setCena(BigDecimal cena) {
		this.cena = cena;
	}
	
	
}
