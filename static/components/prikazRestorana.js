Vue.component('prikazRestorana', {
	data: function() {
		return {
			restoran: null,
			ascending: false,
			sortColumn: '',
			artikal: null,
			role: localStorage.getItem("role"),
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
		  		sum += this.restoran.dostupniArtikli[i].kolicina * this.restoran.dostupniArtikli[i].cena;
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
	},

	template: `
    <div>
    {{restoran.naziv}} <br/>
    {{restoran.tip}} <br/>
    {{restoran.status}} <br/>
    <table>
		 <thead>
		   <tr>
			<th v-for="col in columns" v-on:click="sortTable(col.name)"> 
				{{col.name}}
		   </th>
		   </tr>
		 </thead>
		 <tbody>
		   <tr v-for="artikal in this.restoran.dostupniArtikli" v-bind:artikal="artikal">
		   	<td v-for="col in columns">
			   		{{ artikal[col.name] }}
			</td>
			<td v-if="role === 'Kupac'">
				Kolicina: <input type="number" v-model="artikal.kolicina" min="0"/>
			</td>
		   </tr>
		 </tbody>
	   </table>
	   <div v-if="role === 'Kupac'">
	   Ukupna cena: {{getUkupnaCena()}}
    </div>
    </div>
	`
})