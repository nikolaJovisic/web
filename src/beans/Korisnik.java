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
	
	public String getKorisnickoIme() {
		return korisnickoIme;
	}
	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}
	@Override
	public boolean equals(Object obj) {
		
		 if (obj == this) {
	            return true;
		 }

	        if (!(obj instanceof Korisnik)) {
	            return false;
	        }
	          
	        Korisnik korisnik = (Korisnik) obj;
	          
	        return korisnickoIme.equals(korisnik.korisnickoIme);
	}
	
	protected Korisnik(String korisnickoIme, String lozinka, String ime, String prezime, Pol pol, Date datumRodjenja,
			Uloga uloga) {
		super();
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.ime = ime;
		this.prezime = prezime;
		this.pol = pol;
		this.datumRodjenja = datumRodjenja;
		this.uloga = uloga;
	}
	
	
	
	
	
}
