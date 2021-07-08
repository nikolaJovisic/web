package beans;


public class Ponuda {
	private String dostavljacUsername;
	private String porudzbinaID;
	public Ponuda(String dostavljacUsername, String porudzbinaID) {
		super();
		this.dostavljacUsername = dostavljacUsername;
		this.porudzbinaID = porudzbinaID;
	}
	public String getDostavljacUsername() {
		return dostavljacUsername;
	}
	public String getPorudzbinaID() {
		return porudzbinaID;
	}
	public String getKey() {
		return dostavljacUsername + porudzbinaID;
	}
}
