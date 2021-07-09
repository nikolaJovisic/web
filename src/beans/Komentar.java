package beans;

public class Komentar {
	private String ID;
	private Kupac kupac;
	private Restoran restoran;
	private String tekst;
	private int ocena;
	private boolean odobren;
	
	
	
	
	
	public Komentar(String iD, Kupac kupac, Restoran restoran, String tekst, int ocena) {
		super();
		this.ID = iD;
		this.kupac = kupac;
		this.restoran = restoran;
		this.tekst = tekst;
		this.ocena = ocena;
		this.odobren = false;
	}
	
	public boolean isOdobren() {
		return odobren;
	}

	public void setOdobren(boolean odobren) {
		this.odobren = odobren;
	}

	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public Kupac getKupac() {
		return kupac;
	}
	public void setKupac(Kupac kupac) {
		this.kupac = kupac;
	}
	public Restoran getRestoran() {
		return restoran;
	}
	public void setRestoran(Restoran restoran) {
		this.restoran = restoran;
	}
	public String getTekst() {
		return tekst;
	}
	public void setTekst(String tekst) {
		this.tekst = tekst;
	}
	public int getOcena() {
		return ocena;
	}
	public void setOcena(int ocena) {
		this.ocena = ocena;
	}
	
	
}
