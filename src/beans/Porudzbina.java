package beans;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repositories.KupacRepository;
import services.KorisnikService;

public class Porudzbina {
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}


	public Restoran getRestoran() {
		return restoran;
	}

	public void setRestoran(Restoran restoran) {
		this.restoran = restoran;
	}

	public LocalDateTime getDatumVreme() {
		return datumVreme;
	}

	public void setDatumVreme(LocalDateTime datumVreme) {
		this.datumVreme = datumVreme;
	}

	public double getCena() {
		return cena;
	}

	public void setCena(double cena) {
		this.cena = cena;
	}

	public String getImePrezimeKupca() {
		return imePrezimeKupca;
	}

	public void setImePrezimeKupca(String imePrezimeKupca) {
		this.imePrezimeKupca = imePrezimeKupca;
	}

	public StatusPorudzbine getStatus() {
		return status;
	}

	public void setStatus(StatusPorudzbine status) {
		this.status = status;
	}

	private String ID;
	private Map<Artikal, Integer> artikli;
	private Restoran restoran;
	private LocalDateTime datumVreme;
	private double cena;
	private String imePrezimeKupca;
	private StatusPorudzbine status;
	
	public Porudzbina(String ID, Restoran restoran, double cena, Korpa korpa) {
		KupacRepository kupacRepository = new KupacRepository();
		
		this.ID = ID;
		this.restoran = restoran;
		this.datumVreme = LocalDateTime.now();
		this.cena = cena;
		this.artikli = new HashMap<Artikal, Integer>();
		for (String nazivArtikla : korpa.getArtikli().keySet())
		{
			artikli.put(restoran.getArtikal(nazivArtikla), korpa.getArtikli().get(nazivArtikla));
		}
		Kupac kupac = korpa.getKupac();
		
		this.imePrezimeKupca = kupac.getIme() + " " + kupac.getPrezime();
		this.status = StatusPorudzbine.UPripremi;
		
		kupac.setSakupljeniBodovi((int)(kupac.getSakupljeniBodovi() + this.cena/1000*133));
		kupacRepository.update(kupac.getKorisnickoIme(), kupac);
	}


	public Map<Artikal, Integer> getArtikli() {
		return artikli;
	}

	public void setArtikli(Map<Artikal, Integer> artikli) {
		this.artikli = artikli;
	}
}
