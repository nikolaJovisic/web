package repositories;

import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.gson.reflect.TypeToken;
import beans.Dostavljac;
import beans.Porudzbina;

public class DostavljacRepository extends Repository<Dostavljac, String> {

	@Override
	protected String getKey(Dostavljac dostavljac) {
		return dostavljac.getKorisnickoIme();
	}
	@Override
	protected Type getTokenType() {
		return new TypeToken<ArrayList<LogicalEntity<Dostavljac>>>() {}.getType();
	}
	public void updatePorudzbinaStatusToDostavljena(String porudzbinaID) {
		for(Dostavljac dostavljac: getAll() ) {
			for(Porudzbina porudzbina: dostavljac.getPorudzbineZaDostavu()) {
				if(porudzbina.getID().equals(porudzbinaID)) {
					dostavljac.updatePorudzbinaStatusToDostavljena(porudzbinaID);
					update(dostavljac.getKorisnickoIme(), dostavljac);
				}
			}
		}
		
	}

}
