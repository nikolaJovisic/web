package beans;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import repositories.KupacRepository;
import services.KorisnikService;

public class Porudzbina {
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public List<Artikal> getArtikli() {
		return artikli;
	}

	public void setArtikli(List<Artikal> artikli) {
		this.artikli = artikli;
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
	private List<Artikal> artikli;
	private Restoran restoran;
	private LocalDateTime datumVreme;
	private BigDecimal cena;
	private String imePrezimeKupca;
	private StatusPorudzbine status;
	
	public Porudzbina(Integer ID, Restoran restoran, String username) {
		KupacRepository kupacRepository = new KupacRepository();
		
		this.ID = ID.toString();
		this.artikli = restoran.getDostupniArtikli();
		this.restoran = restoran;
		this.datumVreme = LocalDateTime.now();
		this.cena = BigDecimal.ZERO;
		for(Artikal artikal: artikli) {
			this.cena = this.cena.add(artikal.getCena().multiply(BigDecimal.valueOf(artikal.getKolicina())));
		}
		
		Kupac kupac = kupacRepository.getOne(username);
		this.imePrezimeKupca = kupac.getIme() + " " + kupac.getPrezime();
		this.status = StatusPorudzbine.UPripremi;
		
		kupac.setSakupljeniBodovi((int)(kupac.getSakupljeniBodovi() + this.cena.floatValue()/1000*133));
		kupacRepository.update(kupac.getKorisnickoIme(), kupac);
	}
}
