package repositories;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.reflect.TypeToken;
import beans.Porudzbina;

public class PorudzbineRepository extends Repository<Porudzbina, String> {

	@Override
	protected String getKey(Porudzbina entity) {
		return entity.getID();
	}

	@Override
	protected Type getTokenType() {
		return new TypeToken<ArrayList<LogicalEntity<Porudzbina>>>() {}.getType();
	}

}
