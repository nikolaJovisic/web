package repositories;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public abstract class Repository<Entity, Key> {

	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
	private String filePath = Paths.get("").toAbsolutePath() + File.separator + "data" + File.separator
			+ this.getClass().getSimpleName() + ".json";

	protected abstract String getKey(Entity entity);

	protected abstract Type getTokenType();

	private List<LogicalEntity<Entity>> getAllLogical() {
		Reader reader;
		ArrayList<LogicalEntity<Entity>> entites;
		try {
			reader = Files.newBufferedReader(Paths.get(filePath));
			entites = gson.fromJson(reader, getTokenType());
			reader.close();
			if (entites == null)
				entites = new ArrayList<LogicalEntity<Entity>>();
			return entites;
		} catch (IOException e) {
			return new ArrayList<LogicalEntity<Entity>>();
		}
	}

	public List<Entity> getAll() {
		List<Entity> filtriraniKupci = new ArrayList<Entity>();
		for (LogicalEntity<Entity> entity : getAllLogical())
			if (!entity.deleted)
				filtriraniKupci.add(entity.entity);
		return filtriraniKupci;
	}

	public void addOne(Entity newEntity) {
		List<LogicalEntity<Entity>> logicalEntites = getAllLogical();
		logicalEntites.add(new LogicalEntity<Entity>(newEntity));
		save(logicalEntites);
	}

	public Entity getOne(Key key) {
		for (Entity entity : getAll()) {
			if (key.equals(getKey(entity)))
				return entity;
		}
		return null;
	}
	
	public boolean contains(Key key) {
		return getOne(key) != null;
	}

	public void update(Key key, Entity newEntity) {
		List<LogicalEntity<Entity>> entites;
		entites = getAllLogical();
		for (int i = 0; i < entites.size(); i++) {
			if (getKey(entites.get(i).entity).equals(key)) {
				entites.set(i, new LogicalEntity<Entity>(newEntity));
			}
		}
		save(entites);
	}

	public void delete(Key key) {
		List<LogicalEntity<Entity>> entites;
		entites = getAllLogical();
		for (int i = 0; i < entites.size(); i++) {
			if (getKey(entites.get(i).entity).equals(key)) {
				LogicalEntity<Entity> tmp = entites.get(i);
				tmp.deleted = true;
				entites.set(i, tmp);
			}
		}
		save(entites);
	}

	private void save(List<LogicalEntity<Entity>> entites) {
		try {
			FileWriter fw = new FileWriter(filePath);
			gson.toJson(entites, fw);
			fw.close();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
