package services;

import java.util.List;

import beans.Kupac;
import repositories.KupacRepository;

public class KupacService {
	private KupacRepository kupacRepository = new KupacRepository();
	
	public void register(Kupac kupac) {
		kupacRepository.addOne(kupac);
	}
	
	public List<Kupac> getAll() {
		return kupacRepository.getAll();
	}
	
}
