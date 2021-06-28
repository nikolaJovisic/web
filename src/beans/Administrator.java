package beans;

import java.util.Date;

public class Administrator extends Korisnik {

	public Administrator(String korisnickoIme, String lozinka, String ime, String prezime, Pol pol, Date datumRodjenja) {
		super(korisnickoIme, lozinka, ime, prezime, pol, datumRodjenja, Uloga.Administrator);
	}

}
