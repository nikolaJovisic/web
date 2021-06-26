package beans;

import java.util.Date;

public class Korisnik {
	private String korisnickoIme;
	private String lozinka;
	private String ime;
	private String prezime;
	private Pol pol;
	private Date datumRodjenja;
	private Uloga uloga; //kršenje SOLID principa
	private TipKupca tip;
}
