package repositories;

import beans.Kupac;

public class KupacRepository extends Repository<Kupac, String> {

	@Override
	protected String getKey(Kupac kupac) {
		return kupac.getKorisnickoIme();
	}
	
}
