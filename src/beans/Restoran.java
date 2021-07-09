package beans;

import java.util.ArrayList;
import java.util.List;

import repositories.KomentarRepository;

public class Restoran {
	private String naziv;
	private TipRestorana tip;
	private List<Artikal> dostupniArtikli;
	private boolean status;
	private Lokacija lokacija;
	private String slika;
	private double prosecnaOcena;
	
	private Restoran() {
		dostupniArtikli = new ArrayList<Artikal>();
		prosecnaOcena = 0.0;
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

	public String getSlika() {
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

	public double getProsecnaOcena() {
		return prosecnaOcena;
	}

	public void setProsecnaOcena(double prosecnaOcena) {
		this.prosecnaOcena = prosecnaOcena;
	}

	public void updateOcene() {
		KomentarRepository komentarRepository = new KomentarRepository();
		double sum = 0;
		int count = 0;
		for (Komentar k : komentarRepository.getAll())
		{
			if (k.getRestoran().getNaziv().equals(this.naziv) && k.getOcena() != 0 && k.isOdobren())
			{
				sum += k.getOcena();
				count++;
			}
		}
		setProsecnaOcena(count == 0 ? 0 : sum/count);
		
	}
	
}
