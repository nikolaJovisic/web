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

public class KupacRepository extends Repository<Kupac, String> {

	@Override
	protected String getKey(Kupac kupac) {
		return kupac.getKorisnickoIme();
	}
	
}
