package services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;

import beans.Administrator;
import beans.Dostavljac;
import beans.Korisnik;
import beans.Kupac;
import beans.Menadzer;
import beans.Porudzbina;
import beans.StatusPorudzbine;
import repositories.AdministratorRepository;
import repositories.DostavljacRepository;
import repositories.KupacRepository;
import repositories.MenadzerRepository;
import repositories.PorudzbineRepository;

public class KorisnikService {
	private AdministratorRepository administratorRepository = new AdministratorRepository();
	private DostavljacRepository dostavljacRepository = new DostavljacRepository();
	private KupacRepository kupacRepository = new KupacRepository();
	private MenadzerRepository menadzerRepository = new MenadzerRepository();
	private PorudzbineRepository porudzbineRepository = new PorudzbineRepository();

	public Korisnik FindByID(String korisnickoIme) {
		Korisnik retVal;
		retVal = administratorRepository.getOne(korisnickoIme);
		if (retVal != null) {
			return retVal;
		}
		retVal = dostavljacRepository.getOne(korisnickoIme);
		if (retVal != null) {
			return retVal;
		}
		retVal = kupacRepository.getOne(korisnickoIme);
		if (retVal != null) {
			return retVal;
		}
		retVal = menadzerRepository.getOne(korisnickoIme);
		return retVal;
	}

	public boolean register(Korisnik korisnik) {
		switch(korisnik.getUloga()) {
		case Administrator:
			return administratorRepository.addOne(new Administrator(korisnik));
		case Dostavljac:
			return dostavljacRepository.addOne(new Dostavljac(korisnik));
		case Kupac:
			return kupacRepository.addOne(new Kupac(korisnik));
		case Menadzer:
			return menadzerRepository.addOne(new Menadzer(korisnik));
		default:
			return false;
		}
	}

	
	public void update(Korisnik korisnik) {
		switch(korisnik.getUloga()) {
		case Administrator:
			administratorRepository.update(korisnik.getKorisnickoIme(), (Administrator) korisnik);
			break;
		case Dostavljac:
			dostavljacRepository.update(korisnik.getKorisnickoIme(), (Dostavljac) korisnik);
			break;
		case Kupac:
			kupacRepository.update(korisnik.getKorisnickoIme(), (Kupac) korisnik);
			break;
		case Menadzer:
			menadzerRepository.update(korisnik.getKorisnickoIme(), (Menadzer) korisnik);
			break;
		default:
			break;
		}
	}
	public List<Korisnik> getAll() {
		List<Korisnik> retVal = new ArrayList<Korisnik>();
		for(Administrator administrator: administratorRepository.getAll()) {
			retVal.add((Korisnik) administrator);
		}
		for(Dostavljac dostavljac: dostavljacRepository.getAll()) {
			retVal.add((Korisnik) dostavljac);
		}
		for(Kupac kupac: kupacRepository.getAll()) {
			retVal.add((Korisnik) kupac);
		}
		for(Menadzer menadzer: menadzerRepository.getAll()) {
			retVal.add((Korisnik) menadzer);
		}
		return retVal;
	}

	public List<Korisnik> filterUsers(List<Korisnik> unfiltered, String nameSearch, String surnameSearch,
			String usernameSearch) {
		List<Korisnik> filtered = new ArrayList<Korisnik>();
		for (Korisnik korisnik : unfiltered)
		{
			if (korisnik.getIme().toLowerCase().startsWith(nameSearch.toLowerCase()) &&
					korisnik.getPrezime().toLowerCase().startsWith(surnameSearch.toLowerCase())&&
					korisnik.getKorisnickoIme().toLowerCase().startsWith(usernameSearch.toLowerCase()))
			{
				filtered.add(korisnik);
			}
		}
		return filtered;
	}

	public void blokiraj(String korisnickoIme) {
		Korisnik korisnik = FindByID(korisnickoIme);
		korisnik.setBlokiran(true);
		update(korisnik);
	}
	
	public List<Kupac> getSumnjiviKupci() {
		List<Kupac> kupci = kupacRepository.getAll();
		List<Kupac> sumnjiviKupci = new ArrayList<>();
		for (Kupac kupac: kupci) {
			if(sumnjiv(kupac)) {
				sumnjiviKupci.add(kupac);
			}
		}
		return sumnjiviKupci;
	}

	private boolean sumnjiv(Kupac kupac) {
		List<Porudzbina> otkazanePorudzbine = getOtkazanePorudzbine(kupac.getSvePorudzbine());
		for (int i = 0; i < otkazanePorudzbine.size() - 6; ++i) {
			if(manjeOdMesecDanaIzmedju(otkazanePorudzbine.get(i + 6).getDatumVreme(), otkazanePorudzbine.get(i).getDatumVreme())) {
				return true;
			}
		}
		return false;
	}

	private boolean manjeOdMesecDanaIzmedju(LocalDateTime pocetak, LocalDateTime kraj) {
		return pocetak.until(kraj, ChronoUnit.MONTHS) < 1;
	}

	private List<Porudzbina> getOtkazanePorudzbine(List<Porudzbina> svePorudzbine) {
		List<Porudzbina> otkazanePordzbine = new ArrayList<>();
		for (Porudzbina porudzbina: svePorudzbine) {
			if (porudzbina.getStatus() == StatusPorudzbine.Otkazana) {
				otkazanePordzbine.add(porudzbina);
			}
		}
		return otkazanePordzbine;
	}

	public List<Korisnik> filterKupci(List<Kupac> unfiltered, String nameSearch, String surnameSearch,
			String usernameSearch) {
		List<Korisnik> filtered = new ArrayList<Korisnik>();
		for (Kupac kupac : unfiltered)
		{
			Korisnik korisnik = (Korisnik)kupac;
			if (korisnik.getIme().toLowerCase().startsWith(nameSearch.toLowerCase()) &&
					korisnik.getPrezime().toLowerCase().startsWith(surnameSearch.toLowerCase())&&
					korisnik.getKorisnickoIme().toLowerCase().startsWith(usernameSearch.toLowerCase()))
			{
				filtered.add(korisnik);
			}
		}
		return filtered;
	
	
	}
}
