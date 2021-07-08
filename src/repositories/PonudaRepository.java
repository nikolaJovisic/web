package repositories;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.reflect.TypeToken;

import beans.Ponuda;

public class PonudaRepository extends Repository<Ponuda, String> {

	@Override
	protected String getKey(Ponuda ponuda) {
		return ponuda.getKey();
	}
	@Override
	protected Type getTokenType() {
		return new TypeToken<ArrayList<LogicalEntity<Ponuda>>>() {}.getType();
	}

}