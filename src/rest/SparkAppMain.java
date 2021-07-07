package rest;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import java.io.File;
import java.math.BigDecimal;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import beans.Artikal;
import beans.Dostavljac;
import beans.Korisnik;
import beans.Korpa;
import beans.Porudzbina;
import beans.Restoran;
import beans.StatusPorudzbine;
import beans.Uloga;
import beans.Kupac;
import beans.Menadzer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import repositories.KupacRepository;
import repositories.MenadzerRepository;
import repositories.PorudzbineRepository;
import repositories.RestoranRepository;
import services.KorisnikService;
import services.RestoranService;

public class SparkAppMain {

	private static Gson gson = new Gson();
	private static KorisnikService korisnikService = new KorisnikService();
	private static RestoranRepository restoranRepository = new RestoranRepository();
	private static KupacRepository kupacRepository = new KupacRepository();
	private static MenadzerRepository menadzerRepository = new MenadzerRepository();
	private static PorudzbineRepository porudzbineRepository = new PorudzbineRepository();
	private static RestoranService restoranService = new RestoranService();
	private static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	private static int porudzbineID = 0;

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
			menadzerRepository.setRestoranForMenadzerUsername(req.queryParams("restoran"),
					menadzerRepository.getOne(korisnik.getKorisnickoIme()));
			return true;
		});

		post("/registracijaArtikla", (req, res) -> {
			Gson gsonReg = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			System.out.println(req.body());
			Artikal artikal = gsonReg.fromJson(req.body(), Artikal.class);
			String jwt = req.queryParams("jwt");
			String username = getUsername(jwt);
			String nazivRestorana = menadzerRepository.getNazivRestorana(username);
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
		
		get("/svePorudzbine", (req, res) -> {
			Gson gsonReg = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			String auth = req.headers("Authorization");
			String username = getUsername(auth);
			Korisnik korisnik = korisnikService.FindByID(username);
			Uloga uloga = korisnik.getUloga();
			List<Porudzbina> unfiltered = new ArrayList<Porudzbina>();
			if (uloga == Uloga.Kupac)
			{
				Kupac k = (Kupac) korisnik;
				unfiltered = k.getSvePorudzbine();
			}
			else if (uloga == Uloga.Dostavljac)
			{
				Dostavljac d = (Dostavljac) korisnik;
				unfiltered = d.getPorudzbineZaDostavu();
				for (Porudzbina p : porudzbineRepository.getAll())
				{
					if (p.getStatus() == StatusPorudzbine.CekaDostavljaca)
						unfiltered.add(p);
				}
			} else if (uloga == Uloga.Menadzer)
			{
				Menadzer m = (Menadzer) korisnik;
				for (Porudzbina p : porudzbineRepository.getAll())
				{
					if (p.getRestoran().getNaziv().equals(m.getRestoran().getNaziv()))
						unfiltered.add(p);
				}
			}
			else
			{
				unfiltered = porudzbineRepository.getAll();
			}
			return gsonReg.toJson(unfiltered);
			
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

		get("/restoranPoNazivu", (req, res) -> {
			return gson.toJson(restoranRepository.getOne(req.queryParams("naziv")));
		});

		post("/izmenaPodataka", (req, res) -> {
			Gson gsonReg = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			String username = getUsername(req.queryParams("jwt"));
			Korisnik korisnik = korisnikService.FindByID(username);
			return gsonReg.toJson(korisnik);
		});

		post("/novaPorudzbina", (req, res) -> {
			String username = getUsername(req.queryParams("jwt"));
			String nazivRestorana = req.queryParams("restoran");
			double cena = Double.parseDouble(req.queryParams("cena"));
			Korpa korpa = new Korpa(null, kupacRepository.getOne(username), cena); // umesto null staviti mapu
			//Porudzbina porudzbina = new Porudzbina(porudzbineID++, restoranRepository.getOne(nazivRestorana), cena, korpa);
			//porudzbineRepository.addOne(porudzbina);
			//kupacRepository.dodajPorudzbinu(username, porudzbina);
			return true;
		});
		
		post("/mojaNovaPorudzbina", (req, res) -> {
			String username = getUsername(req.queryParams("jwt"));
			String nazivRestorana = req.queryParams("nazivRestorana");
			System.out.println(nazivRestorana);
			Restoran restoran = restoranRepository.getOne(nazivRestorana);
			System.out.println(nazivRestorana);
			Kupac kupac = (Kupac) korisnikService.FindByID(username);
			System.out.println(req.body());
			Korpa korpa = gson.fromJson(req.body(), Korpa.class);
			
			korpa.setKupac(kupac);

			//TODO: korpa ima mapu imena artikala i ukupnu cenu, konstruisati porudzbinu na osnovu ovog.
			//tvoj staticki counter za porudzbine nije vrednost od 10 karaktera
			//staticki counter je uvek 0 na pocetku izvrsavanja programa, ako se restartuje nije vise unikatna vrednost
			
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
