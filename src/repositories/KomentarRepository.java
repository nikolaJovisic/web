package repositories;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
	public List<Komentar> getAllOdobreni(String nazivRestorana)
	{
		List<Komentar> odobreni = new ArrayList<Komentar>();
		for (Komentar k : getAll())
			if (k.isOdobren() && k.getRestoran().getNaziv().equals(nazivRestorana))
				odobreni.add(k);
		return odobreni;
	}
	public Object getAll(String nazivRestorana) {
		List<Komentar> svi = new ArrayList<Komentar>();
		for (Komentar k : getAll())
			if (k.getRestoran().getNaziv().equals(nazivRestorana))
				svi.add(k);
		return svi;
	}

}
