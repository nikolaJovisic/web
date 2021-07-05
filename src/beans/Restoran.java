package beans;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

public class Restoran {
	private String naziv;
	private TipRestorana tip;
	private List<Artikal> dostupniArtikli;
	private boolean status;
	private Lokacija lokacija;
	private Image slika;
	
	private Restoran() {
		dostupniArtikli = new ArrayList<Artikal>();
	}

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

	public void addArtikal(Artikal artikal) {
		dostupniArtikli.add(artikal);
	}

	public void editArtikal(Artikal noviArtikal) {
		for(int i = 0; i < dostupniArtikli.size(); ++i) {
			if (dostupniArtikli.get(i).getNaziv().equals(noviArtikal.getNaziv())) {
				dostupniArtikli.set(i, noviArtikal);
			}
		}
		
	}

	public Artikal getArtikal(String nazivArtikla) {
		for(Artikal artikal: dostupniArtikli) {
			if (artikal.getNaziv().equals(nazivArtikla)) {
				return artikal;
			}
		}
		return null;
	}
}
