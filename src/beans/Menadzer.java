package beans;

import java.util.Date;

public class Menadzer extends Korisnik {
	private Restoran restoran;
	
	public Menadzer(String korisnickoIme, String lozinka, String ime, String prezime, Pol pol, Date datumRodjenja) {
		super(korisnickoIme, lozinka, ime, prezime, pol, datumRodjenja, Uloga.Menadzer);
	}
	
	public Menadzer(Korisnik korisnik) {
		super(korisnik);
	}

	public Restoran getRestoran() {
		return restoran;
	}

	public void setRestoran(Restoran restoran) {
		this.restoran = restoran;
	}

}
