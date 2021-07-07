package beans;

import java.util.Date;

public class Korisnik {
	
	protected String korisnickoIme;
	protected String lozinka;
	protected String ime;
	protected String prezime;
	protected Pol pol;
	protected Date datumRodjenja;
	protected Uloga uloga;
	protected boolean blokiran;
	
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
		this.blokiran = false;
	}
	
	public Korisnik (Korisnik korisnik) {
		this.korisnickoIme = korisnik.korisnickoIme;
		this.lozinka = korisnik.lozinka;
		this.ime = korisnik.ime;
		this.prezime = korisnik.prezime;
		this.pol = korisnik.pol;
		this.datumRodjenja = korisnik.datumRodjenja;
		this.uloga = korisnik.uloga;
		this.blokiran = false;
	}
	
	
	public String getUlogaString() {
		return uloga.name();
	}
	public String getLozinka() {
		return lozinka;
	}
	public Uloga getUloga() {
		return uloga;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getPrezime() {
		return prezime;
	}
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	public Pol getPol() {
		return pol;
	}
	public void setPol(Pol pol) {
		this.pol = pol;
	}
	public Date getDatumRodjenja() {
		return datumRodjenja;
	}
	public void setDatumRodjenja(Date datumRodjenja) {
		this.datumRodjenja = datumRodjenja;
	}
	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}
	public void setUloga(Uloga uloga) {
		this.uloga = uloga;
	}
	
	
	
}
