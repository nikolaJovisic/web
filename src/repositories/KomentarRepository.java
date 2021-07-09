package repositories;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.reflect.TypeToken;

import beans.Dostavljac;
import beans.Komentar;

public class KomentarRepository extends Repository<Komentar, String> {

	@Override
	protected String getKey(Komentar komentar) {
		return komentar.getID();
	}
	@Override
	protected Type getTokenType() {
		return new TypeToken<ArrayList<LogicalEntity<Komentar>>>() {}.getType();
	}
	public String GetNewID()
	{
		String ID =String.valueOf(getAll().size());
		while (ID.length() != 10)
			ID = "0" + ID;
		return ID;
	}

}
