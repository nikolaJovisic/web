package beans;



public class TipKupca {
	
	private TipoviKupaca tip;
	private double popust;
	private int trazeniBrojBodova;
	
	
	public TipKupca(TipoviKupaca tip)
	{
		this.tip = tip;
		if (tip == TipoviKupaca.Bronzani)
		{
			popust = 0.0;
			trazeniBrojBodova = 0;
		}
		else if (tip == TipoviKupaca.Srebrni)
		{
			popust = 5.0;
			trazeniBrojBodova = 1000;
		}
		else if (tip == TipoviKupaca.Zlatni)
		{
			popust = 10.0;
			trazeniBrojBodova = 2000;
		}
	}
	
	




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
	public TipoviKupaca getTip() {
		return tip;
	}




	public void setTip(TipoviKupaca tip) {
		this.tip = tip;
	}




	public double getPopust() {
		return popust;
	}




	public void setPopust(double popust) {
		this.popust = popust;
	}




	public int getTrazeniBrojBodova() {
		return trazeniBrojBodova;
	}




	public void setTrazeniBrojBodova(int trazeniBrojBodova) {
		this.trazeniBrojBodova = trazeniBrojBodova;
	}
}