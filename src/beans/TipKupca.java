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
	
	




	public TipKupca() {
		super();
	}

		
		

	public TipKupca(double sakupljeniBodovi) {
		if (sakupljeniBodovi >= 2000)
		{
			this.tip = TipoviKupaca.Zlatni;
			this.popust = 10.0;
			this.trazeniBrojBodova = 2000;
		} else if (sakupljeniBodovi >= 1000)
		{
			this.tip = TipoviKupaca.Srebrni;
			this.popust = 5.0;
			this.trazeniBrojBodova = 1000;
		} else
		{
			this.tip = TipoviKupaca.Bronzani;
			this.popust = 0.0;
			this.trazeniBrojBodova = 0;
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