package services;

import java.util.ArrayList;
import java.util.List;

import beans.Restoran;
import beans.TipRestorana;
import repositories.RestoranRepository;

public class RestoranService {

	
	public List<Restoran> FilterRestaurants(List<Restoran> unfiltered, String nameSearch, String locationSearch, String tipSearch, String ocenaSearch, String geografskaDuzina, String geografskaSirina)
	{
		double radius = 1.0;
		List<Restoran> filtered = new ArrayList<Restoran>();
		double ocenaLowerBound = ocenaSearch.equals("") ? -1 : Double.parseDouble(ocenaSearch.split("-")[0]);
		for (Restoran restoran :  unfiltered)
		{
			boolean withinLocation = (!geografskaDuzina.equals("") && !geografskaSirina.equals("")) ? isWithinLocation(restoran, Double.parseDouble(geografskaDuzina), Double.parseDouble(geografskaSirina), radius) : true;
			
			if (withinLocation && restoran.getNaziv().toLowerCase().startsWith(nameSearch.toLowerCase()) &&
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
	
	public boolean isWithinLocation(Restoran restoran, double geografskaDuzina, double geografskaSirina, double radius)
	{
		 final int R = 6371;
		 Double lat1 = restoran.getLokacija().getGeografskaSirina();
		 Double lon1 = restoran.getLokacija().getGeografskaDuzina();
		 Double lat2 = geografskaSirina;
		 Double lon2 = geografskaDuzina;
		 Double latDistance = toRad(lat2-lat1);
		 Double lonDistance = toRad(lon2-lon1);
		 Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + 
		 Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) * 
		 Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		 Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		 Double distance = R * c;
		 return distance <= radius;
		
	}
	 private static Double toRad(Double value) {
		 return value * Math.PI / 180;
		 }
}
