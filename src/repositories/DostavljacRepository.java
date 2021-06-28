package repositories;

import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.gson.reflect.TypeToken;
import beans.Dostavljac;

public class DostavljacRepository extends Repository<Dostavljac, String> {

	@Override
	protected String getKey(Dostavljac dostavljac) {
		return dostavljac.getKorisnickoIme();
	}
	@Override
	protected Type getTokenType() {
		return new TypeToken<ArrayList<LogicalEntity<Dostavljac>>>() {}.getType();
	}

}
