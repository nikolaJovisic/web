package beans;


public enum TipoviKupaca {
	Bronzani("Bronzazni"),
	Srebrni("Srebrni"),
	Zlatni("Zlatni");
	private String value;
	TipoviKupaca(String value){
		this.value = value;
	}
	public static TipoviKupaca fromString(String value) {
		for(TipoviKupaca p : TipoviKupaca.values()) {
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