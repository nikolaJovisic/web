package services;

import java.util.ArrayList;
import java.util.List;

import beans.Restoran;

public class RestoranService {

	
	public List<Restoran> FilterRestaurants(List<Restoran> unfiltered, String nameSearch, String locationSearch, String tipSearch, String ocenaSearch)
	{
		List<Restoran> filtered = new ArrayList<Restoran>();
		int ocenaLowerBound = Integer.parseInt(ocenaSearch.split("-")[0]);
		
		return filtered;
	}
}
