package rest;

import static spark.Spark.get;

import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import java.io.File;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import beans.Artikal;
import beans.Dostavljac;
import beans.Komentar;
import beans.Korisnik;
import beans.Korpa;
import beans.Porudzbina;
import beans.Restoran;
import beans.StatusPorudzbine;
import beans.Uloga;
import beans.Kupac;
import beans.Menadzer;
import beans.Ponuda;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import repositories.KomentarRepository;
import repositories.KupacRepository;
import repositories.MenadzerRepository;
import repositories.PonudaRepository;
import repositories.PorudzbineRepository;
import repositories.RestoranRepository;
import services.KorisnikService;
import services.LocalDateAdapter;
import services.PorudzbinaService;
import services.RestoranService;


public class SparkAppMain {
	

	private static Gson gson = new Gson();
	private static KorisnikService korisnikService = new KorisnikService();
	private static RestoranRepository restoranRepository = new RestoranRepository();
	private static KupacRepository kupacRepository = new KupacRepository();
	private static MenadzerRepository menadzerRepository = new MenadzerRepository();
	private static PorudzbineRepository porudzbineRepository = new PorudzbineRepository();
	private static KomentarRepository komentarRepository = new KomentarRepository();
	private static PonudaRepository ponudaRepository = new PonudaRepository();
	private static RestoranService restoranService = new RestoranService();
	private static PorudzbinaService porudzbinaService = new PorudzbinaService();
	private static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	
	public static void main(String[] args) throws Exception {
		port(8081);

		staticFiles.externalLocation(new File("./static").getCanonicalPath());
		post("/logovanje", (req, res) -> {
			String korisnickoIme = req.queryParams("korisnickoIme");
			String lozinka = req.queryParams("lozinka");
			String jws = null;
			List<String> response = new ArrayList<String>();

			Korisnik korisnik = korisnikService.FindByID(korisnickoIme);
			if (korisnik == null || !korisnik.getLozinka().equals(lozinka)) {
				jws = "-1";
				response.add(jws);
			} else if (korisnik.isBlokiran()) {
				jws = "-2";
				response.add(jws);
			} else {
				jws = Jwts.builder().setSubject(korisnickoIme)
						.setExpiration(new Date(new Date().getTime() + 100000 * 10L)).setIssuedAt(new Date())
						.signWith(key).compact();
				response.add(jws);
				response.add(korisnik.getUlogaString());
			}

			return gson.toJson(response);
		});

		post("/registracija", (req, res) -> {
			Gson gsonReg = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			Korisnik korisnik = gsonReg.fromJson(req.body(), Korisnik.class);
			korisnikService.register(korisnik);
			String nazivRestorana = req.queryParams("restoran");
			System.out.println(nazivRestorana);
			if (!nazivRestorana.equals("null")) {
				menadzerRepository.setRestoranForMenadzerUsername(nazivRestorana,
						menadzerRepository.getOne(korisnik.getKorisnickoIme()));

			}
			return true;
		});

		post("/registracijaArtikla", (req, res) -> {
			Gson gsonReg = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			System.out.println(req.body());
			Artikal artikal = gsonReg.fromJson(req.body(), Artikal.class);
			String jwt = req.queryParams("jwt");
			String username = getUsername(jwt);
			String nazivRestorana = menadzerRepository.getNazivRestorana(username);
			if (restoranRepository.getArtikal(nazivRestorana, artikal.getNaziv()) != null)
				return false;
			restoranRepository.addArtikal(nazivRestorana, artikal);
			return true;
		});

		post("/blokiraj", (req, res) -> {
			String korisnickoIme = req.queryParams("korisnickoIme");
			korisnikService.blokiraj(korisnickoIme);
			return true;
		});

		get("/sviKorisnici", (req, res) -> {
			List<Korisnik> unfiltered = korisnikService.getAll();
			String nameSearch = req.queryParams("nameSearch");
			String surnameSearch = req.queryParams("surnameSearch");
			String usernameSearch = req.queryParams("usernameSearch");
			System.out.println(nameSearch + surnameSearch + usernameSearch);
			if (nameSearch == null || surnameSearch == null || usernameSearch == null)
				return gson.toJson(unfiltered);
			return gson.toJson(korisnikService.filterUsers(unfiltered, nameSearch, surnameSearch, usernameSearch));
		});
		get("/sviRestorani", (req, res) -> {
			List<Restoran> unfiltered = restoranRepository.getAll();
			String nameSearch = req.queryParams("nameSearch");
			String locationSearch = req.queryParams("locationSearch");
			String tipSearch = req.queryParams("tipSearch");
			String ocenaSearch = req.queryParams("ocenaSearch");
			if (nameSearch == null || locationSearch == null || tipSearch == null || ocenaSearch == null)
				return gson.toJson(unfiltered);
			return gson.toJson(
					restoranService.FilterRestaurants(unfiltered, nameSearch, locationSearch, tipSearch, ocenaSearch));

		});
		get("/sviRestoraniZaKomentarisanje", (req, res) -> {
			List<String> naziviRestorana = new ArrayList<String>();
			String jwt = req.queryParams("jwt");
			String username = getUsername(jwt);
			Korisnik korisnik = korisnikService.FindByID(username);
			if (korisnik.getUloga() != Uloga.Kupac)
				return null;
			Kupac kupac = (Kupac) korisnik;
			for (Restoran r : restoranRepository.getAll())
			{
				boolean zaKomentarisanje = true;
				for (Komentar k : komentarRepository.getAll())
				{
					if (k.isOdobren() && k.getKupac().getKorisnickoIme().equals(kupac.getKorisnickoIme()) && k.getRestoran().getNaziv().equals(r.getNaziv()))
					{
						zaKomentarisanje = false;
						break;
					}					
				}
				if (zaKomentarisanje)
					naziviRestorana.add(r.getNaziv());
			}
			return gson.toJson(naziviRestorana);
		});

		get("/svePorudzbine", (req, res) -> {
			Gson gsonReg = new GsonBuilder()
	                .setPrettyPrinting()
	                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter()).create();
			String auth = req.headers("Authorization");
			String username = getUsername(auth);
			Korisnik korisnik = korisnikService.FindByID(username);
			Uloga uloga = korisnik.getUloga();
			List<Porudzbina> unfiltered = new ArrayList<Porudzbina>();
			if (uloga == Uloga.Kupac) {
				Kupac k = (Kupac) korisnik;
				unfiltered = k.getSvePorudzbine();
			} else if (uloga == Uloga.Dostavljac) {
				Dostavljac d = (Dostavljac) korisnik;
				unfiltered = d.getPorudzbineZaDostavu();
				for (Porudzbina p : porudzbineRepository.getAll()) {
					if (p.getStatus() == StatusPorudzbine.CekaDostavljaca || p.getStatus() == StatusPorudzbine.UTransportu)
						unfiltered.add(p);
				}
			} else if (uloga == Uloga.Menadzer) {
				Menadzer m = (Menadzer) korisnik;
				for (Porudzbina p : porudzbineRepository.getAll()) {
					if (p.getRestoran().getNaziv().equals(m.getRestoran().getNaziv()))
						unfiltered.add(p);
				}
			} else {
				unfiltered = porudzbineRepository.getAll();
			}
			
			String nameSearch = req.queryParams("nameSearch");
			String odSearch = req.queryParams("odSearch");
			String doSearch = req.queryParams("doSearch");
			String odDatumPorudzbine = req.queryParams("odDatumPorudzbine");
			String doDatumPorudzbine = req.queryParams("doDatumPorudzbine");
			
			if (nameSearch == null || odSearch == null || doSearch == null || odDatumPorudzbine == null || doDatumPorudzbine == null)
				return gsonReg.toJson(unfiltered);
			return gsonReg.toJson(porudzbinaService.filter(unfiltered, nameSearch, odSearch, doSearch, odDatumPorudzbine, doDatumPorudzbine));
		});

		get("/checkJWT", (req, res) -> {
			String auth = req.headers("Authorization");
			String username = getUsername(auth);
			if (username.equals(""))
				return false;
			return true;
		});

		get("/slobodniMenadzeri", (req, res) -> {
			return gson.toJson(menadzerRepository.getSlobodniMenadzeriUsernames());
		});
		get("/sviKupciZaRestoran", (req, res) -> {
			List<Kupac> kupci = new ArrayList<Kupac>();
			String username = getUsername(req.queryParams("jwt"));
			Korisnik korisnik = korisnikService.FindByID(username);
			if (korisnik.getUloga() != Uloga.Menadzer)
				return null;
			Menadzer m = (Menadzer) korisnik;
			Restoran restoran = restoranRepository.getOne(m.getRestoran().getNaziv());
			for (Kupac kupac : kupacRepository.getAll())
			{
				for (Porudzbina porudzbina : kupac.getSvePorudzbine())
				{
					if (porudzbina.getRestoran().getNaziv().equals(restoran.getNaziv()))
					{
						kupci.add(kupac);
						break;
					}
				}
			}
			
			return gson.toJson(kupci);
		});
		get("/sviKomentari", (req, res) -> {
			
			String jwt = req.queryParams("jwt");
			String nazivRestorana = req.queryParams("naziv");
			if (jwt.equals("-1"))
				return gson.toJson(komentarRepository.getAllOdobreni(nazivRestorana));
			String username = getUsername(jwt);
			Korisnik korisnik = korisnikService.FindByID(username);
			if (korisnik.getUloga() == Uloga.Administrator)
				return gson.toJson(komentarRepository.getAll(nazivRestorana));
			if (korisnik.getUloga() == Uloga.Menadzer)
			{
				Menadzer m = (Menadzer) korisnik;
				if (m.getRestoran().getNaziv().equals(nazivRestorana))
					return gson.toJson(komentarRepository.getAll(nazivRestorana));
			}
			return gson.toJson(komentarRepository.getAllOdobreni(nazivRestorana));
		});

		get("/restoranPoNazivu", (req, res) -> {
			
			return gson.toJson(restoranRepository.getOne(req.queryParams("naziv")));
		});
		get("/restoranZaMenadzera", (req, res) -> {
			String username = getUsername(req.queryParams("jwt"));
			Korisnik korisnik = korisnikService.FindByID(username);
			if (korisnik.getUloga() != Uloga.Menadzer)
				return null;
			Menadzer m = (Menadzer) korisnik;
			Restoran restoran = restoranRepository.getOne(m.getRestoran().getNaziv());
			
			return gson.toJson(restoran);
		});
		
		get("/svePonude", (req, res) -> {
			return gson.toJson(ponudaRepository.getAll());
		});
		
		get("/popust", (req, res) -> {
			String jwt = req.queryParams("jwt");
			if (jwt.equals("-1"))
				return 0.0;
			
			String username = getUsername(req.queryParams("jwt"));
			Korisnik korisnik = korisnikService.FindByID(username);
			if (korisnik.getUloga() != Uloga.Kupac)
				return 0.0;
			Kupac kupac = kupacRepository.getOne(username);
			double popust = kupac.getPopust();
			return gson.toJson(popust);
		});
		
		get("/sumnjiviKupci", (req, res) -> {
			return gson.toJson(korisnikService.getSumnjiviKupci());
		});
		
		get("/porudzbinaZatrazena", (req, res) -> {
			String auth = req.headers("Authorization");
			String username = getUsername(auth);
			String porudzbinaID = req.queryParams("porudzbinaID");
			return ponudaRepository.contains(username+porudzbinaID);
		});
		
		post("/odobriPonudu", (req, res) -> {
			Gson gsonReg = new GsonBuilder().create();
			Ponuda ponuda = gsonReg.fromJson(req.body(), Ponuda.class);
			ponudaRepository.delete(ponuda.getKey());
			Porudzbina porudzbina = porudzbineRepository.getOne(ponuda.getPorudzbinaID());
			porudzbina.setStatus(StatusPorudzbine.UTransportu);
			porudzbineRepository.update(porudzbina.getID(), porudzbina);
			porudzbinaService.updateKupci(porudzbineRepository.getOne(ponuda.getPorudzbinaID()));
			return true;
		});
		
		post("/odbijPonudu", (req, res) -> {
			Gson gsonReg = new GsonBuilder().create();
			Ponuda ponuda = gsonReg.fromJson(req.body(), Ponuda.class);
			ponudaRepository.delete(ponuda.getKey());
			return true;
		});
		post("/odobriKomentar", (req, res) -> {
			String ID = req.queryParams("ID");
			String username = getUsername(req.queryParams("jwt"));
			Korisnik korisnik = korisnikService.FindByID(username);
			if (korisnik.getUloga() != Uloga.Menadzer)
				return false;
			Menadzer menadzer = (Menadzer) korisnik;
			Komentar komentar = komentarRepository.getOne(ID);
			if (komentar == null)
				return false;
			if (!komentar.getRestoran().getNaziv().equals(menadzer.getRestoran().getNaziv()))
				return false;
			komentar.setOdobren(true);
			komentarRepository.update(komentar.getID(), komentar);
			Restoran restoran = komentar.getRestoran();
			restoran.updateOcene();
			restoranRepository.update(restoran.getNaziv(), restoran);
			return true;
		});
		post("/odbijKomentar", (req, res) -> {
			String ID = req.queryParams("ID");
			String username = getUsername(req.queryParams("jwt"));
			Korisnik korisnik = korisnikService.FindByID(username);
			if (korisnik.getUloga() != Uloga.Menadzer)
				return false;
			Menadzer menadzer = (Menadzer) korisnik;
			Komentar komentar = komentarRepository.getOne(ID);
			if (komentar == null)
				return false;
			if (!komentar.getRestoran().getNaziv().equals(menadzer.getRestoran().getNaziv()))
				return false;
			komentarRepository.delete(komentar.getID());
			return true;
		});
		post("/promeniStatus", (req, res) -> {
			String naziv = req.queryParams("naziv");
			String username = getUsername(req.queryParams("jwt"));
			Korisnik korisnik = korisnikService.FindByID(username);
			if (korisnik.getUloga() != Uloga.Menadzer)
				return false;
			Menadzer menadzer = (Menadzer) korisnik;
			Restoran restoran = restoranRepository.getOne(naziv);
			if (restoran == null || !menadzer.getRestoran().getNaziv().equals(naziv))
				return false;
			restoran.setStatus(!restoran.isStatus());
			restoranRepository.update(naziv, restoran);
			return true;
		});
		

		post("/izmenaPodataka", (req, res) -> {
			Gson gsonReg = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			String username = getUsername(req.queryParams("jwt"));
			Korisnik korisnik = korisnikService.FindByID(username);
			return gsonReg.toJson(korisnik);
		});
		
		post("/obrada", (req, res) -> {
			String username = getUsername(req.queryParams("jwt"));
			String ID = req.queryParams("IDPorudzbine");
			Korisnik korisnik = korisnikService.FindByID(username);
			if (korisnik.getUloga() != Uloga.Menadzer)
				return false;
			Porudzbina p = porudzbineRepository.getOne(ID);
			if (p.getStatus() != StatusPorudzbine.Obrada)
				return false;
			p.setStatus(StatusPorudzbine.UPripremi);
			porudzbinaService.updateKupci(p);
			porudzbineRepository.update(ID, p);
			return true;
		});
		post("/oceni", (req, res) -> {
			String username = getUsername(req.queryParams("jwt"));
			String ID = req.queryParams("IDPorudzbine");
			Korisnik korisnik = korisnikService.FindByID(username);
			if (korisnik.getUloga() != Uloga.Kupac)
				return null;
			Porudzbina p = porudzbineRepository.getOne(ID);
			if (p.getStatus() != StatusPorudzbine.Dostavljena)
				return null;
			Komentar komentar = null;
			for (Komentar k : komentarRepository.getAll())
			{
				if (k.getKupac().getKorisnickoIme().equals(korisnik.getKorisnickoIme()) && k.getRestoran().getNaziv().equals(p.getRestoran().getNaziv()))
					komentar = k;
			}
			if (komentar == null)
			{
				String IDKomentar = komentarRepository.GetNewID();
				komentarRepository.addOne(new Komentar(IDKomentar, (Kupac) korisnik,  p.getRestoran(), "", 0));
				return IDKomentar;
			}
			return gson.toJson(komentar.getID());
		});
		get("/komentar", (req, res) -> {
			String username = getUsername(req.queryParams("jwt"));
			String ID = req.queryParams("ID");
			Korisnik korisnik = korisnikService.FindByID(username);
			if (korisnik.getUloga() != Uloga.Kupac)
				return null;
			Kupac kupac = (Kupac) korisnik;
			Komentar komentar = komentarRepository.getOne(ID);
			if (komentar == null) 
				return false;
			boolean belongsToKupac = false;
			for (Porudzbina p : kupac.getSvePorudzbine())
			{
				if (p.getRestoran().getNaziv().equals(komentar.getRestoran().getNaziv()))
					belongsToKupac = true;
			}
			if (!belongsToKupac)
				return false;
			return gson.toJson(komentar);
		});
		post("/komentar", (req, res) -> {
			String username = getUsername(req.queryParams("jwt"));
			String ID = req.queryParams("ID");
			Korisnik korisnik = korisnikService.FindByID(username);
			if (korisnik.getUloga() != Uloga.Kupac)
				return null;
			Kupac kupac = (Kupac) korisnik;
			Komentar komentar = komentarRepository.getOne(ID);
			if (komentar == null) 
				return false;
			Restoran restoran = komentar.getRestoran();
			boolean belongsToKupac = false;
			for (Porudzbina p : kupac.getSvePorudzbine())
			{
				if (p.getRestoran().getNaziv().equals(komentar.getRestoran().getNaziv()))
					belongsToKupac = true;
			}
			if (!belongsToKupac)
				return false;
			Komentar izmenjenKomentar = gson.fromJson(req.body(), Komentar.class);
			System.out.println(gson.toJson(izmenjenKomentar));
			if (izmenjenKomentar.getOcena() < 1 || izmenjenKomentar.getOcena() > 5)
				return false;
			komentar.setOcena(izmenjenKomentar.getOcena());
			komentar.setTekst(izmenjenKomentar.getTekst());
			komentarRepository.update(komentar.getID(), komentar);
			restoran.updateOcene();
			restoranRepository.update(restoran.getNaziv(), restoran);
			
			return true;
		});
		post("/pripremi", (req, res) -> {

			String username = getUsername(req.queryParams("jwt"));
			String ID = req.queryParams("IDPorudzbine");
			Korisnik korisnik = korisnikService.FindByID(username);
			if (korisnik.getUloga() != Uloga.Menadzer)
				return false;
			Porudzbina p = porudzbineRepository.getOne(ID);
			if (p.getStatus() != StatusPorudzbine.UPripremi)
				return false;
			p.setStatus(StatusPorudzbine.CekaDostavljaca);
			porudzbinaService.updateKupci(p);
			porudzbineRepository.update(ID, p);
			return true;
		});
		
		post("/zatraziPorudzbinu", (req, res) -> {

			String username = getUsername(req.queryParams("jwt"));
			String ID = req.queryParams("IDPorudzbine");
			Korisnik korisnik = korisnikService.FindByID(username);
			if (korisnik.getUloga() != Uloga.Dostavljac)
				return false;
			Porudzbina porudzbina = porudzbineRepository.getOne(ID);
			if (porudzbina.getStatus() != StatusPorudzbine.CekaDostavljaca)
				return false;
			
			Ponuda ponuda = new Ponuda(username, porudzbina.getID());
			ponudaRepository.addOne(ponuda);
			return true;
		});
		
		post("/dostaviPorudzbinu", (req, res) -> {

			String username = getUsername(req.queryParams("jwt"));
			String ID = req.queryParams("IDPorudzbine");
			Korisnik korisnik = korisnikService.FindByID(username);
			if (korisnik.getUloga() != Uloga.Dostavljac)
				return false;
			Porudzbina porudzbina = porudzbineRepository.getOne(ID);
			if (porudzbina.getStatus() != StatusPorudzbine.UTransportu)
				return false;
			porudzbina.setStatus(StatusPorudzbine.Dostavljena);
			porudzbinaService.updateKupci(porudzbina);
			porudzbineRepository.update(ID, porudzbina);
			return true;
		});
		
		
		
		
		post("/otkazi", (req, res) -> {

			String username = getUsername(req.queryParams("jwt"));
			String ID = req.queryParams("IDPorudzbine");
			Korisnik korisnik = korisnikService.FindByID(username);
			if (korisnik.getUloga() != Uloga.Kupac)
				return false;
			Porudzbina p = porudzbineRepository.getOne(ID);
			if (p.getStatus() != StatusPorudzbine.Obrada)
				return false;
			p.setStatus(StatusPorudzbine.Otkazana);
			Kupac k = (Kupac) korisnik;
			k.reducePoints(p.getCena());
			kupacRepository.update(k.getKorisnickoIme(), k);
			porudzbinaService.updateKupci(p);
			porudzbineRepository.update(ID, p);
			return true;
		});

		post("/mojaNovaPorudzbina", (req, res) -> {
			String username = getUsername(req.queryParams("jwt"));
			String nazivRestorana = req.queryParams("nazivRestorana");
			Restoran restoran = restoranRepository.getOne(nazivRestorana);
			if (!restoran.isStatus())
				return false;
			Kupac kupac = (Kupac) korisnikService.FindByID(username);
			Korpa korpa = gson.fromJson(req.body(), Korpa.class);
			korpa.setKupac(kupac);
			if(!korpa.checkCena(restoran)) {
				return false;
			}
			if (korpa.getCena() == 0)
				return false;

			Porudzbina porudzbina = new Porudzbina(porudzbineRepository.GetNewID(), restoran, korpa.getCena(), korpa);
			porudzbineRepository.addOne(porudzbina);
			kupacRepository.dodajPorudzbinu(username, porudzbina);
			return true;
		});

		post("/izmenaProfila", (req, res) -> {
			String jwt = req.headers("jwt");
			String username = getUsername(jwt);

			Gson gsonReg = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			Korisnik izmenjeniKorisnik = gsonReg.fromJson(req.body(), Korisnik.class);
			Korisnik korisnik = korisnikService.FindByID(username);

			korisnik.setDatumRodjenja(izmenjeniKorisnik.getDatumRodjenja());
			korisnik.setIme(izmenjeniKorisnik.getIme());
			korisnik.setLozinka(izmenjeniKorisnik.getLozinka());
			korisnik.setPol(izmenjeniKorisnik.getPol());
			korisnik.setPrezime(izmenjeniKorisnik.getPrezime());

			korisnikService.update(korisnik);
			return true;
		});

		post("/izmenaArtikla", (req, res) -> {
			String jwt = req.queryParams("jwt");
			String username = getUsername(jwt);

			Gson gsonReg = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			Artikal artikal = gsonReg.fromJson(req.body(), Artikal.class);
			String nazivRestorana = menadzerRepository.getNazivRestorana(username);
			restoranRepository.editArtikal(nazivRestorana, artikal);
			return true;
		});
		post("/izmenaPodatakaArtikla", (req, res) -> {
			Gson gsonReg = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			String username = getUsername(req.queryParams("jwt"));
			String nazivArtikla = req.queryParams("naziv");
			String nazivRestorana = menadzerRepository.getNazivRestorana(username);
			Artikal artikal = restoranRepository.getArtikal(nazivRestorana, nazivArtikla);
			return gsonReg.toJson(artikal);
		});

		post("/noviRestoran", (req, res) -> {
			Restoran restoran = new GsonBuilder().create().fromJson(req.body(), Restoran.class);
			restoranRepository.addOne(restoran);
			menadzerRepository.setRestoranForMenadzerUsername(restoran, req.queryParams("menadzer"));
			return true;
		});

	}

	private static String getUsername(String auth) {
		if (auth == null)
			return "";
		String jwt = auth;
		Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
		return claims.getBody().getSubject();
	}
	
}
