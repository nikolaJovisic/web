package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import beans.TipKupca.TipoviKupaca;

public class Kupac extends Korisnik {
	
	private List<Porudzbina> svePorudzbine;
	private Korpa korpa;
	private int sakupljeniBodovi;
	private TipKupca tip;
	
	public Kupac(String korisnickoIme, String lozinka, String ime, String prezime, Pol pol, Date datumRodjenja) {
		super(korisnickoIme, lozinka, ime, prezime, pol, datumRodjenja, Uloga.Kupac);
		svePorudzbine = new ArrayList<Porudzbina>();
		sakupljeniBodovi = 0;
		korpa = new Korpa(this);
		tip = new TipKupca(TipoviKupaca.Bronzani);
	}
	
	public Kupac(Korisnik korisnik) {
		super(korisnik);
		svePorudzbine = new ArrayList<Porudzbina>();
		sakupljeniBodovi = 0;
		korpa = new Korpa(this);
		tip = new TipKupca(TipoviKupaca.Bronzani);
	}
	
}
