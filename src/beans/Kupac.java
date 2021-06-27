package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Kupac extends Korisnik {
	private List<Porudzbina> svePorudzbine;
	private Korpa korpa;
	private int sakupljeniBodovi;
	
	public Kupac(String korisnickoIme, String lozinka, String ime, String prezime, Pol pol, Date datumRodjenja) {
		super();
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.ime = ime;
		this.prezime = prezime;
		this.pol = pol;
		this.datumRodjenja = datumRodjenja;
		svePorudzbine = new ArrayList();
		sakupljeniBodovi = 0;
		korpa = new Korpa(this);
	}
}
