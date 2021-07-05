package repositories;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.reflect.TypeToken;

import beans.Restoran;

public class RestoranRepository extends Repository<Restoran, String> {

	@Override
	protected String getKey(Restoran restoran) {
		return restoran.getNaziv();
	}
	@Override
	protected Type getTokenType() {
		return new TypeToken<ArrayList<LogicalEntity<Restoran>>>() {}.getType();
	}

}
