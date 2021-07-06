package repositories;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import beans.Administrator;
import beans.Artikal;
import beans.Menadzer;
import beans.Restoran;

public class MenadzerRepository extends Repository<Menadzer, String> {

	@Override
	protected String getKey(Menadzer menadzer) {
		return menadzer.getKorisnickoIme();
	}
	@Override
	protected Type getTokenType() {
		return new TypeToken<ArrayList<LogicalEntity<Menadzer>>>() {}.getType();
	}
	
	public List<String> getSlobodniMenadzeriUsernames() {
		List<String> retVal = new ArrayList<String>();
		for (Menadzer menadzer: getAll()) {
			if(menadzer.getRestoran() == null) {
				retVal.add(menadzer.getKorisnickoIme());
			}
		}
		return retVal;
	}
	
	public void setRestoranForMenadzerUsername(Restoran restoran, String menadzerUsername) {
		Menadzer menadzer = getOne(menadzerUsername);
		menadzer.setRestoran(restoran);
		update(menadzerUsername, menadzer);
	}
	public void setRestoranForMenadzerUsername(String nazivRestorana, Menadzer menadzer) {
		Restoran restoran = new RestoranRepository().getOne(nazivRestorana);
		menadzer.setRestoran(restoran);
		update(menadzer.getKorisnickoIme(), menadzer);
	}
	
	public String getNazivRestorana(String username) {
		Menadzer menadzer = getOne(username);
		return menadzer.getRestoran().getNaziv();
	}
	
}
