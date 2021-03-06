package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Dostavljac extends Korisnik {
	private List<Porudzbina> porudzbineZaDostavu;

	public Dostavljac(String korisnickoIme, String lozinka, String ime, String prezime, Pol pol, Date datumRodjenja) {
		super(korisnickoIme, lozinka, ime, prezime, pol, datumRodjenja, Uloga.Dostavljac);
		porudzbineZaDostavu = new ArrayList<Porudzbina>();
	}

	public Dostavljac(Korisnik korisnik) {
		super(korisnik);
		porudzbineZaDostavu = new ArrayList<Porudzbina>();
	}

	public List<Porudzbina> getPorudzbineZaDostavu() {
		return porudzbineZaDostavu;
	}

	public void setPorudzbineZaDostavu(List<Porudzbina> porudzbineZaDostavu) {
		this.porudzbineZaDostavu = porudzbineZaDostavu;
	}

	public void dodajPorudzbinu(Porudzbina porudzbina) {
		porudzbineZaDostavu.add(porudzbina);
	}

	public void updatePorudzbinaStatusToDostavljena(String porudzbinaID) {
		for(Porudzbina porudzbina: porudzbineZaDostavu) {
			if(porudzbina.getID().equals(porudzbinaID)) {
				porudzbina.setStatus(StatusPorudzbine.Dostavljena);
				return;
			}
			
		}
		
	}
	
}
