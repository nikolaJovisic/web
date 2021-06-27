package rest;

import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;

import beans.Kupac;
import beans.Pol;
import services.KupacService;
import validations.KupacValidation;

public class SparkAppMain {

	private static Gson g = new Gson();
	private static KupacService kupacService = new KupacService();
	private static KupacValidation kupacValidation = new KupacValidation();

	public static void main(String[] args) throws Exception {
		port(8080);

		staticFiles.externalLocation(new File("./static").getCanonicalPath());

		post("logovanjeForma", (req, res) -> {
			return null;
		});

		post("registracijaForma", (req, res) -> {
			String korisnickoIme = req.queryParams("korisnickoIme");
			String lozinka = req.queryParams("lozinka");
			String ime = req.queryParams("ime");
			String prezime = req.queryParams("prezime");
			Pol pol = Pol.valueOf(req.queryParams("pol"));
			Date datumRodjenja = new SimpleDateFormat("yyyy-mm-dd").parse(req.queryParams("datumRodjenja"));

			Kupac noviKupac = new Kupac(korisnickoIme, lozinka, ime, prezime, pol, datumRodjenja);

			if (kupacValidation.isValid(noviKupac)) {
				kupacService.register(noviKupac);
				return "Kupac sa korisničkim imenom " + korisnickoIme + " registrovan";
			} else {
				return "Kupac nije registrovan.";
			}
		});

	}
}
