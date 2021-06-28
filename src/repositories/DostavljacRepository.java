package repositories;

import beans.Dostavljac;

public class DostavljacRepository extends Repository<Dostavljac, String> {

	@Override
	protected String getKey(Dostavljac dostavljac) {
		return dostavljac.getKorisnickoIme();
	}

}
