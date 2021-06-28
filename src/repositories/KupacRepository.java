package repositories;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.reflect.TypeToken;

import beans.Administrator;
import beans.Kupac;

public class KupacRepository extends Repository<Kupac, String> {

	@Override
	protected String getKey(Kupac kupac) {
		return kupac.getKorisnickoIme();
	}
	@Override
	protected Type getTokenType() {
		return new TypeToken<ArrayList<LogicalEntity<Kupac>>>() {}.getType();
	}

}
