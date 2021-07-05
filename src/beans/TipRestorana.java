package beans;

public enum TipRestorana {
	Italijanski("Italijanski"),
	Kineski("Kineski"),
	Rostilj("Rostilj");
	private String value;
	TipRestorana(String value){
		this.value = value;
	}
	public static TipRestorana fromString(String value) {
		for(TipRestorana p : TipRestorana.values()) {
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
