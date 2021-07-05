package beans;

import java.awt.Image;
import java.math.BigDecimal;

public class Artikal {
	private String naziv;
	private BigDecimal cena;
	private TipArtikla tip;
	private Restoran restoran;
	private int kolicina;
	private String opis;
	private Image slika;
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public BigDecimal getCena() {
		return cena;
	}
	public void setCena(BigDecimal cena) {
		this.cena = cena;
	}
	public TipArtikla getTip() {
		return tip;
	}
	public void setTip(TipArtikla tip) {
		this.tip = tip;
	}
	public Restoran getRestoran() {
		return restoran;
	}
	public void setRestoran(Restoran restoran) {
		this.restoran = restoran;
	}
	public int getKolicina() {
		return kolicina;
	}
	public void setKolicina(int kolicina) {
		this.kolicina = kolicina;
	}
	public String getOpis() {
		return opis;
	}
	public void setOpis(String opis) {
		this.opis = opis;
	}
	public Image getSlika() {
		return slika;
	}
	public void setSlika(Image slika) {
		this.slika = slika;
	}
}
