package services;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;

import beans.Administrator;
import beans.Dostavljac;
import beans.Korisnik;
import beans.Kupac;
import beans.Menadzer;
import repositories.AdministratorRepository;
import repositories.DostavljacRepository;
import repositories.KupacRepository;
import repositories.MenadzerRepository;

public class KorisnikService {
	private AdministratorRepository administratorRepository = new AdministratorRepository();
	private DostavljacRepository dostavljacRepository = new DostavljacRepository();
	private KupacRepository kupacRepository = new KupacRepository();
	private MenadzerRepository menadzerRepository = new MenadzerRepository();

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

	public void register(Korisnik korisnik) {
		switch(korisnik.getUloga()) {
		case Administrator:
			administratorRepository.addOne(new Administrator(korisnik));
			break;
		case Dostavljac:
			dostavljacRepository.addOne(new Dostavljac(korisnik));
			break;
		case Kupac:
			kupacRepository.addOne(new Kupac(korisnik));
			break;
		case Menadzer:
			menadzerRepository.addOne(new Menadzer(korisnik));
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
}
