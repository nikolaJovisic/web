package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Kupac extends Korisnik {
	
	private List<Porudzbina> svePorudzbine;
	private Korpa korpa;
	private double sakupljeniBodovi;
	private TipKupca tip;
	
	
	
	public Kupac(String korisnickoIme, String lozinka, String ime, String prezime, Pol pol, Date datumRodjenja, TipKupca tip) {
		super(korisnickoIme, lozinka, ime, prezime, pol, datumRodjenja, Uloga.Kupac);
		svePorudzbine = new ArrayList<Porudzbina>();
		sakupljeniBodovi = 0;
		korpa = new Korpa(this);
		this.tip = new TipKupca(tip.getTip());
	}
	
	public Kupac(Korisnik korisnik) {
		super(korisnik);
		svePorudzbine = new ArrayList<Porudzbina>();
		sakupljeniBodovi = 0;
		korpa = new Korpa(this);
		this.tip = new TipKupca(0);
	}

	public List<Porudzbina> getSvePorudzbine() {
		return svePorudzbine;
	}

	public void setSvePorudzbine(List<Porudzbina> svePorudzbine) {
		this.svePorudzbine = svePorudzbine;
	}

	public Korpa getKorpa() {
		return korpa;
	}

	public void setKorpa(Korpa korpa) {
		this.korpa = korpa;
	}

	public double getSakupljeniBodovi() {
		return sakupljeniBodovi;
	}

	public void setSakupljeniBodovi(double sakupljeniBodovi) {
		this.sakupljeniBodovi = (sakupljeniBodovi > 0 ? sakupljeniBodovi : 0);
		setTip(new TipKupca(sakupljeniBodovi));
	}

	public TipKupca getTip() {
		return tip;
	}

	public void setTip(TipKupca tip) {
		this.tip = tip;
	}

	public void dodajPorudzbinu(Porudzbina porudzbina) {
		if (svePorudzbine == null)
			svePorudzbine = new ArrayList<Porudzbina>();
		svePorudzbine.add(porudzbina);
	}

	public void reducePoints(double cena) {
		setSakupljeniBodovi((cena/1000 * 133 * 4 > 0) ? (sakupljeniBodovi - cena/1000 * 133 * 4) : 0);
		
	}
	
	public double getPopust() {
		return tip.getPopust();
	}
	
	
	
}
