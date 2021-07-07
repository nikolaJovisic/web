package repositories;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import beans.Administrator;
import beans.Kupac;
import beans.Porudzbina;

public class KupacRepository extends Repository<Kupac, String> {

	@Override
	protected String getKey(Kupac kupac) {
		return kupac.getKorisnickoIme();
	}
	@Override
	protected Type getTokenType() {
		return new TypeToken<ArrayList<LogicalEntity<Kupac>>>() {}.getType();
	}
	
	
	public void dodajPorudzbinu(String username, Porudzbina porudzbina) {
		Kupac kupac = getOne(username);
		kupac.dodajPorudzbinu(porudzbina);
		update(kupac.getKorisnickoIme(), kupac);
	}

}
