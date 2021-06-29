package rest;

import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import beans.Administrator;
import beans.Korisnik;
import beans.Kupac;
import beans.Pol;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import repositories.AdministratorRepository;
import services.KorisnikService;
import services.KupacService;
import java.security.Key;
import validations.KupacValidation;

public class SparkAppMain {

	private static Gson gson = new Gson();
	private static KupacService kupacService = new KupacService();
	private static KorisnikService korisnikService = new KorisnikService();
	private static KupacValidation kupacValidation = new KupacValidation();
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
			Kupac kupac = gsonReg.fromJson(req.body(), Kupac.class);

			/*JsonParser parser = new JsonParser();
			JsonObject jobj = parser.parse(req.body()).getAsJsonObject();
			

			String korisnickoIme = jobj.get("korisnickoIme").toString().replaceAll("\"", "");
			String lozinka = jobj.get("lozinka").toString().replaceAll("\"", "");
			String ime = jobj.get("ime").toString().replaceAll("\"", "");
			String prezime = jobj.get("prezime").toString().replaceAll("\"", "");
			String polVal = jobj.get("pol").toString().replaceAll("\"", "");
			Pol pol;
			if (polVal.equals("Muški"))
				pol = Pol.Muski;
			else
				pol = Pol.Zenski;
			Date datumRodjenja = new SimpleDateFormat("yyyy-mm-dd").parse(jobj.get("datumRodjenja").toString().replaceAll("\"", ""));*/
			
			//Kupac noviKupac = new Kupac(korisnickoIme, lozinka, ime, prezime, pol, datumRodjenja);

			if (kupacValidation.isValid(kupac)) {
				kupacService.register(kupac);
				return true;
			} else {
				return false;
			}
		});

	}
}
