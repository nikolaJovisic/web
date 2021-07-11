Vue.component('prikazRestorana', {
	data: function() {
		return {
			restoran: null,
			mapa: null,
			ascending: false,
			popust: null,
			sortColumn: '',
			role: localStorage.getItem("role"),
			jwt: localStorage.getItem("jwt"),
			columns: [{ name: "naziv" }, { name: "tip" }, { name: "opis" },  { name: "cena" }, {name: "kolicina"}]
		}
	},
	methods: {
		izmeni: function(val) {
			localStorage.setItem('registracijaNovogArtikla', false);
			localStorage.setItem('nazivArtikla', val);
			this.$router.push('/noviArtikal/')
		},
		"sortTable": function sortTable(col) {
			if (this.sortColumn === col) {
				this.ascending = !this.ascending;
			  } else {
				this.ascending = true;
				this.sortColumn = col;
			  } 
			var ascending = this.ascending;
			this.restoran.dostupniArtikli.sort(function(a, b) {
				if (a[col] > b[col]) {
				  return ascending ? 1 : -1
				} else if (a[col] < b[col]) {
				  return ascending ? -1 : 1
				}
				return 0;
			  })
		  },
		  "promeniStatus": function promeniStatus()
		  {
			axios
				.post('/promeniStatus', {}, { params: { naziv: this.restoran.naziv, jwt: this.jwt } })
				.then(response => {
					if (response.data) {
						this.$router.go();
					}
				});
		  },
		  
		  getUkupnaCena() {
		  let sum = 0;
		  	for (let i = 0; i <  this.restoran.dostupniArtikli.length; ++i) {
		  		sum += this.restoran.dostupniArtikli[i].count * this.restoran.dostupniArtikli[i].cena;
		  	}
		  	return sum * (1 - this.popust/100);
		  },
		  
		  posaljiPorudzbinu() {

		  mapa = Object()
		  for (let i = 0; i <  this.restoran.dostupniArtikli.length; ++i) {
			mapa[this.restoran.dostupniArtikli[i].naziv] = this.restoran.dostupniArtikli[i].count !== null ? this.restoran.dostupniArtikli[i].count : 0
			if (this.UkupnaCena == 0) return
		}
		  axios
					.post('/mojaNovaPorudzbina', 
						{
							artikli: mapa,
							cena: this.UkupnaCena
						}
					, {params: {nazivRestorana: this.restoran.naziv, jwt: this.jwt}})
					.then(response => {
						if (response.data)
						{
							alert("Porudžbina uspešno registrovana!");
							this.$router.push("/mainPage");
						}
					});
					
		
	}},
	computed: {
		UkupnaCena: function () {
			let sum = 0;
		  	for (let i = 0; i <  this.restoran.dostupniArtikli.length; ++i) {
		  		sum += this.restoran.dostupniArtikli[i].count * this.restoran.dostupniArtikli[i].cena;
		  	}
		  	return sum * (1 - this.popust/100);
	}

	},
	
	beforeMount() {
		axios.get("/restoranPoNazivu",
			{ params: { naziv: this.$route.params.naziv } })
			.then(response => {
				if (response.data) {
					for (let i = 0; i <  response.data.dostupniArtikli.length; ++i) {
						response.data.dostupniArtikli[i].count = 0;
					}
					this.restoran = response.data;
					localStorage.setItem('longit', this.restoran.lokacija.geografskaDuzina); 
					localStorage.setItem('latit', this.restoran.lokacija.geografskaSirina);
				}
			})
	
	},
	

	mounted() {
		self = this;
		
			
		axios.get("/popust",
			{ params: { jwt: this.jwt } })
			.then(response => {
				if (response.data) {
					this.popust = response.data;
				}
			})

		for (let i = 0; i <  this.restoran.dostupniArtikli.length; ++i) {
			restoran.dostupniArtikli[i].count = 0;
		}
		
		
			
	},
	

	template: `
    <div class="center">
	<h1 >{{restoran.naziv}}</h1>
	<img :src="restoran.slika" class="center"> <br/>
    <h3>Tip: {{restoran.tip}}</h3>
	<h3>
    Status:
	<span v-if="restoran.status"><label class="Otvoreno">Otvoren</label>
	<button v-if="role === 'Menadzer'" v-on:click="promeniStatus()">Zatvori</button>
	</span>
	<span v-else><label class="Zatvoreno">Zatvoren</label>
	<button v-if="role === 'Menadzer'" v-on:click="promeniStatus()">Otvori</button>
	</span></h3>
	<h3 >
	Lokacija:
	{{restoran.lokacija.drzava}}, 
			{{restoran.lokacija.grad}}, 
			   {{restoran.lokacija.adresa}},
			   Koordinate: 
			   {{restoran.lokacija.geografskaDuzina}}, 
			   {{restoran.lokacija.geografskaSirina}}</h3>
			   <div>
			  <mapa> </mapa>
</div>
			  <h3 >
			  
			  
			Ocena:
			{{restoran.prosecnaOcena === 0 ?'Nije ocenjen.' : restoran.prosecnaOcena}}
			
			</h3>
	<h1>Artikli:</h1>
    <table >
		 <thead>
		   <tr>
			<th v-for="col in columns" v-on:click="sortTable(col.name)"> 
				{{col.name}}
		   </th>
		   <th>Slika</th>
		<th/>
		   </tr>
		 </thead>
		 <tbody>
		   <tr v-for="artikal in this.restoran.dostupniArtikli">
		   	<td v-for="col in columns">
			   		{{ artikal[col.name] }} 
			</td>
			<td>
			<img :src="artikal.slika" /> 
			</td>
			<td v-if="role === 'Kupac'">
				Broj: <input type="number" v-model="artikal.count" min="0"/>
			</td>
			<td v-else-if="role === 'Menadzer'">
				<button v-on:click="izmeni(artikal.naziv)">
				Izmeni
				</button>
			</td>
		   </tr>
		 </tbody>
	   </table>
	   <div v-if="role === 'Kupac'">
	   Ukupna cena: {{UkupnaCena}} din
	   <button v-on:click="posaljiPorudzbinu()" v-if="restoran.status">Poruči</button>
    	</div>
		<h1>Komentari kupaca:</h1>
		<komentariPrikaz v-bind:restoran="restoran"></komentariPrikaz>
    </div>
	`
	})