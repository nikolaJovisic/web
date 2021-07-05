package beans;

import java.awt.Image;
import java.util.List;

public class Restoran {
	private String naziv;
	private TipRestorana tip;
	private List<Artikal> dostupniArtikli;
	private boolean status;
	private Lokacija lokacija;
	private Image slika;
	
	public String getNaziv() {
		return naziv;
	}
	public TipRestorana getTip() {
		return tip;
	}
	public List<Artikal> getDostupniArtikli() {
		return dostupniArtikli;
	}
	public boolean isStatus() {
		return status;
	}
	public Lokacija getLokacija() {
		return lokacija;
	}
	public Image getSlika() {
		return slika;
	}
	@Override
	public String toString() {
		return "Restoran [naziv=" + naziv + ", tip=" + tip + ", dostupniArtikli=" + dostupniArtikli + ", status="
				+ status + ", lokacija=" + lokacija + ", slika=" + slika + "]";
	}
}
