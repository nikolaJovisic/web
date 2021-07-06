Vue.component('prikazRestorana', {
	data: function() {
		return {
			restoran: null,
			ascending: false,
			sortColumn: '',
			artikal: null,
			artikliZaPorucivanje: {},
			role: localStorage.getItem("role"),
			columns: [{ name: "naziv" }, { name: "tip" }, { name: "kolicina" }, { name: "cena" }]
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
				Kolicina: <input type="number" v-model="artikliZaPorucivanje[artikal.naziv]" min="0"/>
			</td>
		   </tr>
		 </tbody>
	   </table>
		<div v-for="artk in artikliZaPorucivanje""> 
				{{artk}}
		  </div>	   
    </div>
	`
})