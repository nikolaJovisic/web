Vue.component('prikazRestorana', {
	data: function() {
		return {
			restoran: null,
			mapa: null,
			ascending: false,
			sortColumn: '',
			role: localStorage.getItem("role"),
			jwt: localStorage.getItem("jwt"),
			columns: [{ name: "naziv" }, { name: "tip" },  { name: "cena" }]
		}
	},
	methods: {
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
		  
		  getUkupnaCena() {
		  let sum = 0;
		  	for (let i = 0; i <  this.restoran.dostupniArtikli.length; ++i) {
		  		sum += this.restoran.dostupniArtikli[i].count * this.restoran.dostupniArtikli[i].cena;
		  	}
		  	return sum;
		  },
		  
		  posaljiPorudzbinu() {
		  let mapa = 0;
		  for (let i = 0; i <  this.restoran.dostupniArtikli.length; ++i) {
			mapa = Object()
			mapa[this.restoran.dostupniArtikli[i].naziv] = this.restoran.dostupniArtikli[i].count !== null ? this.restoran.dostupniArtikli[i].count : 0
			console.log(mapa)
		}
		  axios
					.post('/mojaNovaPorudzbina', 
						{
							artikli: mapa,
							cena: this.UkupnaCena
						}
					, {params: {nazivRestorana: this.restoran.naziv, jwt: this.jwt}});
					
		alert("Porudžbina uspešno registrovana!");
	}},
	computed: {
		UkupnaCena: function () {
			let sum = 0;
		  	for (let i = 0; i <  this.restoran.dostupniArtikli.length; ++i) {
		  		sum += this.restoran.dostupniArtikli[i].count * this.restoran.dostupniArtikli[i].cena;
		  	}
		  	return sum;
	}
	},

	mounted() {
		
		axios.get("/restoranPoNazivu",
			{ params: { naziv: this.$route.params.naziv } })
			.then(response => {
				if (response.data) {
					this.restoran = response.data;
				}
			})
			console.log(this)
			for (let i = 0; i <  this.restoran.dostupniArtikli.length; ++i) {
				restoran.dostupniArtikli[i].count = 0;
			}

			
	},
	

	template: `
    <div>
	<img :src="restoran.slika"> <br/>
    {{restoran.naziv}} <br/>
    {{restoran.tip}} <br/>
    {{restoran.status}} <br/>
    <table class="center">
		 <thead>
		   <tr>
			<th v-for="col in columns" v-on:click="sortTable(col.name)"> 
				{{col.name}}
		   </th>
		   <th>Slika</th>
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
				Kolicina: <input type="number" v-model="artikal.count" min="0"/>
			</td>
		   </tr>
		 </tbody>
	   </table>
	   <div v-if="role === 'Kupac'">
	   Ukupna cena: {{UkupnaCena}}
	   <button v-on:click="posaljiPorudzbinu()">Poruči</button>
    	</div>
    </div>
	`
	})