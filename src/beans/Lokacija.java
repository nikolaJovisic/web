package beans;

public class Lokacija {
	private double geografskaDuzina;
	private double geografskaSirina;
	private String adresa;
	private String grad;
	private String drzava;
	@Override
	public String toString() {
		return "Lokacija [geografskaDuzina=" + geografskaDuzina + ", geografskaSirina=" + geografskaSirina + ", adresa="
				+ adresa + "]";
	}
}
