package repositories;

import beans.Administrator;

public class AdministratorRepository extends Repository<Administrator, String> {

	@Override
	protected String getKey(Administrator administrator) {
		return administrator.getKorisnickoIme();
	}

}
