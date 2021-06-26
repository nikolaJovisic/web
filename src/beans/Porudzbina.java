package beans;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Porudzbina {
	private String ID;
	private List<Artikal> artikli;
	private Restoran restoran;
	private LocalDateTime datumVreme;
	private BigDecimal cena;
	private String imePrezimeKupca;
	private StatusPorudzbine status;
}
