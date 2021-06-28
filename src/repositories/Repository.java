package repositories;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

public abstract class Repository<Entity, Key> {
	
	class LogicalEntity {
		public LogicalEntity(Entity entity) {
			this.entity = entity;
			this.deleted = false;
		}
		public Entity entity;
		public boolean deleted;
	}
	
	private Gson gson = new Gson();
	private String filePath = Paths.get("").toAbsolutePath() + File.separator + "data" + File.separator + this.getClass().getSimpleName() + ".json";
	protected abstract Key getKey(Entity entity);
	
	private List<LogicalEntity> getAllLogical() {
		Reader reader;
		List<LogicalEntity> entites;
		try {
			reader = Files.newBufferedReader(Paths.get(filePath));
			entites = gson.fromJson(reader, new TypeToken<List<LogicalEntity>>() {}.getType());
		    reader.close();
		    if (entites == null)
		    	entites = new ArrayList<LogicalEntity>();
			return entites;
		} catch (IOException e) {
			return new ArrayList<LogicalEntity>();
		}
	}
	
	public List<Entity> getAll()
	{
		List<Entity> filtriraniKupci = new ArrayList<Entity>();
		for (LogicalEntity entity : getAllLogical())
			if (!entity.deleted)
				filtriraniKupci.add(entity.entity);
		return filtriraniKupci;
	}
	
	public void addOne(Entity newEntity) {
		List<LogicalEntity> logicalEntites = getAllLogical();
		logicalEntites.add(new LogicalEntity(newEntity));
		save(logicalEntites);
	}
	
	
	public Entity getOne(Key key)
	{
		for (Entity entity : getAll())
		{
			if (key.equals(getKey(entity)))
				return entity;
		}
		return null;
	}
	
	public void update(Key key, Entity newEntity)
	{
		List<LogicalEntity> entites;
		entites = getAllLogical();
		for (int i = 0; i < entites.size(); i++)
		{
			if (getKey(entites.get(i).entity).equals(key))
			{
				entites.set(i, new LogicalEntity(newEntity));
			}
		}
		save(entites);
	}
	
	public void delete(Key key)
	{
		List<LogicalEntity> entites;
		entites = getAllLogical();
		for (int i = 0; i < entites.size(); i++)
		{
			if (getKey(entites.get(i).entity).equals(key))
			{
				LogicalEntity tmp = entites.get(i);
				tmp.deleted = true;
				entites.set(i, tmp);
			}
		}
		save(entites);
	}
	
	private void save(List<LogicalEntity> entites)
	{
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
