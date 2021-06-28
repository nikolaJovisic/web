package repositories;

import java.util.ArrayList;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


import beans.Administrator;

public class AdministratorRepository extends Repository<Administrator, String> {

	@Override
	protected String getKey(Administrator administrator) {
		return administrator.getKorisnickoIme();
	}

	@Override
	protected Type getTokenType() {
		return new TypeToken<ArrayList<LogicalEntity<Administrator>>>() {}.getType();
	}

}
