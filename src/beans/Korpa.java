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
	public boolean checkCena(Restoran restoran) {

		double popust = kupac.getPopust();
		double ukupnaCena = 0;
		
		for(Map.Entry<String, Integer> entry : artikli.entrySet())  {
			String nazivArtikla = entry.getKey();
			int kolicina = entry.getValue();
			double cenaArtikla = restoran.getCenaArtikla(nazivArtikla).doubleValue();
			ukupnaCena += cenaArtikla * kolicina;
		}
		
		ukupnaCena *= (1 - popust/100);
		
		System.out.println(cena);
		System.out.println(ukupnaCena);
		
		return Math.abs(ukupnaCena - cena) < 1;
	}
	
	
}
