package repositories;

import java.util.ArrayList;
import java.util.List;

import beans.Kupac;

public class KupacRepository {
	private List<Kupac> kupci = new ArrayList<Kupac>();
	
	public void Create(Kupac kupac) {
		kupci.add(kupac);
	}
	
	public List<Kupac> ReadAll() {
		return kupci;
	}
	
}
