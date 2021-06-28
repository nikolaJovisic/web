package repositories;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.reflect.TypeToken;

import beans.Administrator;
import beans.Menadzer;

public class MenadzerRepository extends Repository<Menadzer, String> {

	@Override
	protected String getKey(Menadzer menadzer) {
		return menadzer.getKorisnickoIme();
	}
	@Override
	protected Type getTokenType() {
		return new TypeToken<ArrayList<LogicalEntity<Menadzer>>>() {}.getType();
	}

}
