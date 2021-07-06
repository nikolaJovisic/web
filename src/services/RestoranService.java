package services;

import java.util.ArrayList;
import java.util.List;

import beans.Restoran;
import beans.TipRestorana;
import repositories.RestoranRepository;

public class RestoranService {

	
	public List<Restoran> FilterRestaurants(List<Restoran> unfiltered, String nameSearch, String locationSearch, String tipSearch, String ocenaSearch)
	{
		List<Restoran> filtered = new ArrayList<Restoran>();
		double ocenaLowerBound = ocenaSearch.equals("") ? -1 : Double.parseDouble(ocenaSearch.split("-")[0]);
		for (Restoran restoran :  unfiltered)
		{
			if (restoran.getNaziv().toLowerCase().startsWith(nameSearch.toLowerCase()) &&
				(restoran.getLokacija().getGrad().toLowerCase().startsWith(locationSearch.toLowerCase()) ||
				 (restoran.getLokacija().getDrzava().toLowerCase().startsWith(locationSearch.toLowerCase()))) &&
				  (restoran.getTip().equals(TipRestorana.fromString(tipSearch)) || TipRestorana.fromString(tipSearch) == null)  &&
				   restoran.getProsecnaOcena() >= ocenaLowerBound)
			{
				filtered.add(restoran);
			}
			
		}
		
		return filtered;
	}
}
