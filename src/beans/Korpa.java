package beans;

import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;

public class Korpa {
	private Map<String, Integer> artikli;
	private transient Kupac kupac;
	private double cena;
	
	public Korpa(Map<String, Integer> artikli, Kupac kupac, double cena) {
		super();
		this.artikli = artikli;
		this.kupac = kupac;
		this.cena = cena;
	}
	public Korpa(Kupac kupac) {
		super();
		this.artikli = new HashMap<String, Integer>();
		this.cena = 0.0;
		this.kupac = kupac;
	}

	public Map<String, Integer> getArtikli() {
		return artikli;
	}

	public void setArtikli(Map<String, Integer> artikli) {
		this.artikli = artikli;
	}

	public Kupac getKupac() {
		return kupac;
	}

	public void setKupac(Kupac kupac) {
		this.kupac = kupac;
	}

	public double getCena() {
		return cena;
	}

	public void setCena(double cena) {
		this.cena = cena;
	}
	
	
}
