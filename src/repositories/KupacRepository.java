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
	
	public List<Kupac> getSumnjiviKorisnici() {
		List<Kupac> kupci = getAll();
		List<Porudzbina> porudzbine = new PorudzbineRepository().getAll();
		List<Kupac> sumnjiviKorisnici = new ArrayList<>();
		for (Kupac kupac: kupci) {
			int brojOtkazivanja = 0;
			for(Porudzbina porudzbina: porudzbine) {
				
			}
		}
		return null;
		
	}
	public void dodajPorudzbinu(String username, Porudzbina porudzbina) {
		Kupac kupac = getOne(username);
		kupac.dodajPorudzbinu(porudzbina);
		update(kupac.getKorisnickoIme(), kupac);
	}

}
