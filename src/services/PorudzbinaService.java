package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import beans.Porudzbina;

public class PorudzbinaService {

	public List<Porudzbina> filter(List<Porudzbina> unfiltered, String nameSearch, String odSearch, String doSearch,
			String odDatumPorudzbine, String doDatumPorudzbine) throws ParseException {
		
		Date odDatum;
		Date doDatum;
		double minCena;
		double maxCena;
		try {
			odDatum = new SimpleDateFormat("yyyy-MM-dd").parse(odDatumPorudzbine);
			doDatum = new SimpleDateFormat("yyyy-MM-dd").parse(doDatumPorudzbine);
			
		} catch (ParseException e) {
		
			odDatum = new SimpleDateFormat("yyyy-MM-dd").parse("1980-01-01");
			doDatum = new SimpleDateFormat("yyyy-MM-dd").parse("2100-01-01");
		}
		minCena = (odSearch.equals("")) ? Double.MIN_VALUE : Double.parseDouble(odSearch);
		maxCena = (doSearch.equals("")) ? Double.MAX_VALUE : Double.parseDouble(doSearch);
		
		List<Porudzbina> filtered = new ArrayList<Porudzbina>();
		for (Porudzbina p : unfiltered)
		{
			Date pDate = Date.from(p.getDatumVreme().atZone(ZoneId.systemDefault()).toInstant());
			if (p.getRestoran().getNaziv().toLowerCase().startsWith(nameSearch.toLowerCase()) &&
					(pDate.getTime() <= doDatum.getTime() && pDate.getTime() >= odDatum.getTime()) &&
					p.getCena() <= maxCena && p.getCena() >= minCena)
			{
				filtered.add(p);
			}
		}
		return filtered;
	}

}
