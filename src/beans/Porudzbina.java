package beans;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

	public BigDecimal getCena() {
		return cena;
	}

	public void setCena(BigDecimal cena) {
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
	private BigDecimal cena;
	private String imePrezimeKupca;
	private StatusPorudzbine status;
	
	public Porudzbina(Integer ID, Restoran restoran, BigDecimal cena, Korpa korpa) {
		KupacRepository kupacRepository = new KupacRepository();
		
		this.ID = ID.toString();
		this.restoran = restoran;
		this.datumVreme = LocalDateTime.now();
		this.cena = cena;
		
		Kupac kupac = korpa.getKupac();
		
		this.imePrezimeKupca = kupac.getIme() + " " + kupac.getPrezime();
		this.status = StatusPorudzbine.UPripremi;
		
		kupac.setSakupljeniBodovi((int)(kupac.getSakupljeniBodovi() + this.cena.floatValue()/1000*133));
		kupacRepository.update(kupac.getKorisnickoIme(), kupac);
	}


	public Map<Artikal, Integer> getArtikli() {
		return artikli;
	}

	public void setArtikli(Map<Artikal, Integer> artikli) {
		this.artikli = artikli;
	}
}
