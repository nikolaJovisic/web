package repositories;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.reflect.TypeToken;

import beans.Artikal;
import beans.Menadzer;
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
	
	public Artikal getArtikal(String nazivRestorana, String nazivArtikla) {
		return getOne(nazivRestorana).getArtikal(nazivArtikla);
	}
	public void editArtikal(String nazivRestorana, Artikal artikal) {
		Restoran restoran = getOne(nazivRestorana);
		restoran.editArtikal(artikal);
		update(restoran.getNaziv(), restoran);
	}
	
	public void addArtikal(String nazivRestorana, Artikal artikal) {
		Restoran restoran = getOne(nazivRestorana);
		restoran.addArtikal(artikal);
		update(restoran.getNaziv(), restoran);
	}

}
