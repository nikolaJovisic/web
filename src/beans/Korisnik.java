package beans;

import java.util.Date;

public class Korisnik {
	protected String korisnickoIme;
	protected String lozinka;
	protected String ime;
	protected String prezime;
	protected Pol pol;
	protected Date datumRodjenja;
	protected Uloga uloga; //kr�enje SOLID principa
	protected TipKupca tip;
}
