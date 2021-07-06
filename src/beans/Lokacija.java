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
	public double getGeografskaDuzina() {
		return geografskaDuzina;
	}
	public void setGeografskaDuzina(double geografskaDuzina) {
		this.geografskaDuzina = geografskaDuzina;
	}
	public double getGeografskaSirina() {
		return geografskaSirina;
	}
	public void setGeografskaSirina(double geografskaSirina) {
		this.geografskaSirina = geografskaSirina;
	}
	public String getAdresa() {
		return adresa;
	}
	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}
	public String getGrad() {
		return grad;
	}
	public void setGrad(String grad) {
		this.grad = grad;
	}
	public String getDrzava() {
		return drzava;
	}
	public void setDrzava(String drzava) {
		this.drzava = drzava;
	}
	
	
}
