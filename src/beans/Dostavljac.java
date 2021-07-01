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
}
