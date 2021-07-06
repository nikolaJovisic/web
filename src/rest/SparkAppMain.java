package rest;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import java.io.File;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import beans.Artikal;
import beans.Korisnik;
import beans.Menadzer;
import beans.Restoran;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import repositories.MenadzerRepository;
import repositories.RestoranRepository;
import services.KorisnikService;

public class SparkAppMain {

	private static Gson gson = new Gson();
	private static KorisnikService korisnikService = new KorisnikService();
	private static RestoranRepository restoranRepository = new RestoranRepository();
	private static MenadzerRepository menadzerRepository = new MenadzerRepository();
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

			if (korisnik != null && korisnik.getLozinka().equals(lozinka)) {
				jws = Jwts.builder().setSubject(korisnickoIme)
						.setExpiration(new Date(new Date().getTime() + 100000 * 10L)).setIssuedAt(new Date())
						.signWith(key).compact();
				response.add(jws);
				response.add(korisnik.getUlogaString());
			} else {
				jws = "-1";
				response.add(jws);
			}

			return gson.toJson(response);
		});

		post("/registracija", (req, res) -> {
			Gson gsonReg = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			Korisnik korisnik = gsonReg.fromJson(req.body(), Korisnik.class);
			korisnikService.register(korisnik);
			menadzerRepository.setRestoranForMenadzerUsername(req.queryParams("restoran"), menadzerRepository.getOne(korisnik.getKorisnickoIme()));
			return true;
		});
		
		post("/registracijaArtikla", (req, res) -> {
			Gson gsonReg = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			Artikal artikal = gsonReg.fromJson(req.body(), Artikal.class);
			String jwt = req.queryParams("jwt");
			String username = getUsername(jwt);
			String nazivRestorana = menadzerRepository.getNazivRestorana(username);
			restoranRepository.addArtikal(nazivRestorana, artikal);
			return true;
		});
		
		get("/sviKorisnici", (req, res) -> {
			return gson.toJson(korisnikService.getAll());
		});
		get("/sviRestorani", (req, res) -> {
			String nameSearch = req.queryParams("nameSearch");
			String locationSearch = req.queryParams("locationSearch");
			String tipSearch = req.queryParams("tipSearch");
			String ocenaSearch = req.queryParams("ocenaSearch");
			
			//System.out.println(nameSearch + locationSearch + tipSearch + ocenaSearch);
			return gson.toJson(restoranRepository.getAll());
		});
		
		get("/checkJWT", (req, res)->{
			String auth = req.headers("Authorization");
			String username = getUsername(auth);
			if(username.equals(""))
				return false;
			return true;
		});
		
		get("/slobodniMenadzeri", (req, res)->{
			return gson.toJson(menadzerRepository.getSlobodniMenadzeriUsernames());
		});
		
		post("/izmenaPodataka", (req, res)->{
			Gson gsonReg = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			String username = getUsername(req.queryParams("jwt"));
			Korisnik korisnik = korisnikService.FindByID(username);
			return gsonReg.toJson(korisnik);
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
