package beans;

public enum Pol {
	Muski("Muški"),
	Zenski("Ženski");
	private String value;
	Pol(String value){
		this.value = value;
	}
	public static Pol fromString(String value) {
		for(Pol p : Pol.values()) {
			if(p.value.equals(value)) {
				return p;
			}
		}
		return null;
	}
	public String getValue() {
		return value;
	}
}
