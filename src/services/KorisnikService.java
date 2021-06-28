package services;

import beans.Korisnik;
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
}
