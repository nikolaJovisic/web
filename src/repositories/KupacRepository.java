package repositories;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import beans.Kupac;

public class KupacRepository {
	private Gson gson = new Gson();
	private String filePath = Paths.get("").toAbsolutePath() + File.separator + "data" + File.separator + "kupci.txt";
	
	public void addOne(Kupac kupac) {
		List<Kupac> kupci;
		kupci = getAllWithDeleted();
		kupci.add(kupac);
		save(kupci);
	}
	
	public List<Kupac> getAllWithDeleted() {
		Reader reader;
		List<Kupac> kupci;
		try {
			reader = Files.newBufferedReader(Paths.get(filePath));
			kupci = gson.fromJson(reader, new TypeToken<List<Kupac>>() {}.getType());
		    reader.close();
		    if (kupci == null)
		    	kupci = new ArrayList<Kupac>();
			return kupci;
		} catch (IOException e) {
			return new ArrayList<Kupac>();
		}
	}
	public List<Kupac> getAll()
	{
		List<Kupac> filtriraniKupci = new ArrayList<Kupac>();
		for (Kupac kupac : getAllWithDeleted())
			if (!kupac.isDeleted())
				filtriraniKupci.add(kupac);
		return filtriraniKupci;
	}
	
	public Kupac getOne(String korisnickoIme)
	{
		for (Kupac kupac : getAll())
		{
			if (korisnickoIme.equals(kupac.getKorisnickoIme()))
				return kupac;
		}
		return null;
	}
	public void update(String korisnickoIme, Kupac noviKupac)
	{
		List<Kupac> kupci;
		kupci = getAllWithDeleted();
		for (int i = 0; i < kupci.size(); i++)
		{
			if (kupci.get(i).getKorisnickoIme().equals(korisnickoIme))
			{
				kupci.set(i, noviKupac);
			}
		}
		save(kupci);
	}
	public void delete(String korisnickoIme)
	{
		List<Kupac> kupci;
		kupci = getAllWithDeleted();
		for (int i = 0; i < kupci.size(); i++)
		{
			if (kupci.get(i).getKorisnickoIme().equals(korisnickoIme))
			{
				Kupac tmp = kupci.get(i);
				tmp.setDeleted(true);
				kupci.set(i, tmp);
			}
		}
		save(kupci);
	}
	
	private void save(List<Kupac> kupci)
	{
		try {
			FileWriter fw = new FileWriter(filePath);
			gson.toJson(kupci, fw);
			fw.close();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
