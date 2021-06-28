package repositories;

import beans.Menadzer;

public class MenadzerRepository extends Repository<Menadzer, String> {

	@Override
	protected String getKey(Menadzer menadzer) {
		return menadzer.getKorisnickoIme();
	}

}
