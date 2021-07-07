Vue.component('sviKorisnici', {
	data: function() {
		return {
			logged: false,
			role: window.localStorage.getItem("role"),
			korisnici: null,
			ascending: false,
			sortColumn: '',
			ulogaFilter: '',
			nameSearch: '',
			surnameSearch: '',
			usernameSearch: '',
			tipFilter: '',
			columns: [{ name: "korisnickoIme" }, { name: "ime" }, { name: "prezime" }, {name: "sakupljeniBodovi"}, {name: "blokiran"}]
		}
	},
	computed: {
		filtriraniKorisnici: function() {
			if (this.korisnici == null) return null;
			uloga_filter = this.ulogaFilter
			tip_filter = this.tipFilter
			return this.korisnici.filter(function(row) {
				uloga = row.uloga
				tip = uloga === 'Kupac' ? row.tip.tip : null
				return uloga.includes(uloga_filter) && (tip == tip_filter || tip_filter == '')
			});
		}
	},

	methods: {
		"pretraga": function(e)
		{
			axios.get("/sviKorisnici",
			{ params: {nameSearch: this.nameSearch, surnameSearch: this.surnameSearch, usernameSearch: this.usernameSearch,}})
				.then(response => {
					if(response.data)
					{ 
						this.korisnici = response.data;
					}
				})
		},

		"sortTable": function sortTable(col) {
			if (this.sortColumn === col) {
				this.ascending = !this.ascending;
			} else {
				this.ascending = true;
				this.sortColumn = col;
			}

			var ascending = this.ascending;
			this.korisnici.sort(function(a, b) {
				if (a[col] > b[col]) {
					return ascending ? 1 : -1
				} else if (a[col] < b[col]) {
					return ascending ? -1 : 1
				}
				return 0;
			})
		},

		blokiraj(korisnik) {
			alert("Blokiran " + korisnik.korisnickoIme);
			korisnik.blokiran = true;
			axios
					.post('/blokiraj', {
					}, {params: {
						korisnickoIme: korisnik.korisnickoIme
							}});
		},

		moguceBlokirati(korisnik) {
			return korisnik.uloga !== 'Administrator' && !korisnik.blokiran;
		},
		
		renderSakupljeniBodovi: function(colName) {
			return this.ulogaFilter === 'Kupac' || colName !== 'sakupljeniBodovi';
		}


	},

	mounted() {

		axios.get("/sviKorisnici", {
			headers: {
			},
			contentType: "application/json",
			dataType: "json",
		})
			.then(response => {
				if (response.data) {
					this.korisnici = response.data;
				}
			})
	},

	template: `
	<div>
		<div>
		<div>
			<input type="text" v-model="nameSearch">
			<input type="text" v-model="surnameSearch">
			<input type="text" v-model="usernameSearch">
			<button v-on:click="pretraga">Pretraga</button>
		</div>
			Filter:
			<select name="uloga" v-model="ulogaFilter">
			<option></option>
			<option>Kupac</option>
			<option>Administrator</option>
			<option>Menadzer</option>
			<option>Dostavljac</option>
			</select>
			<select name="tip" v-model="tipFilter">
			<option></option>
			<option>Bronzani</option>
			<option>Srebrni</option>
			<option>Zlatni</option>
			</select>
		</div>
		 <table id="table">
		 <thead>
		   <tr>
			<th v-for="col in columns" v-on:click="sortTable(col.name)"> 
				<div v-if="renderSakupljeniBodovi(col.name)">
					{{col.name}}
				</div> 
		   </th>
		   </tr>
		 </thead>
		 <tbody>
		   <tr v-for="korisnik in filtriraniKorisnici">
		   	<td v-for="col in columns">
				<div v-if="renderSakupljeniBodovi(col.name)">
			   		{{ korisnik[col.name] }}
				</div> 
			</td>
			<td v-if="moguceBlokirati(korisnik)">
				<button v-on:click="blokiraj(korisnik)"> Blokiraj </button>
			</td>
		   </tr>
		 </tbody>
	   </table>
	</div>
	`
})