Vue.component('sviKorisnici', {
	data: function() {
		return {
			logged: false,
			role: window.localStorage.getItem("role"),
			korisnici: null,
			sumnjiviKupci: null,
			korisniciPrikaz: null,
			ascending: false,
			sortColumn: '',
			ulogaFilter: '',
			nameSearch: '',
			surnameSearch: '',
			usernameSearch: '',
			prikazSumnjivih: false,
			tipFilter: '',
			columns: [{ name: "korisnickoIme" }, { name: "ime" }, { name: "prezime" }, {name: "sakupljeniBodovi"}, {name: "blokiran"}],
			names: ["Korisnicko ime", "Ime", "Prezime", "Bodovi", "Blokiran"]
		}
	},
	computed: {
		filtriraniKorisnici: function() {
			if (this.korisniciPrikaz == null) return null;
			uloga_filter = this.ulogaFilter
			tip_filter = this.tipFilter
			return this.korisniciPrikaz.filter(function(row) {
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
						this.korisniciPrikaz = response.data;
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
		},

		prikaziSumnjive() {
			this.korisniciPrikaz = this.sumnjiviKupci;
			this.prikazSumnjivih = true;
		},

		prikaziSve() {
			this.korisniciPrikaz = this.korisnici;
			this.prikazSumnjivih = false;
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
					this.korisniciPrikaz = response.data;
					this.korisnici = response.data;
				}
			})
			
		axios.get("/sumnjiviKupci", {
			headers: {
			},
			contentType: "application/json",
			dataType: "json",
		})
			.then(response => {
				if (response.data) {
					this.sumnjiviKupci = response.data;
				}
			})

	},

	template: `
	<div>
		<div>
		<div>
			<input type="text" placeholder="Korisničko ime" v-model="usernameSearch">
			<input type="text" placeholder="Ime" v-model="nameSearch">
			<input type="text" placeholder="Prezime" v-model="surnameSearch">
			<button v-on:click="pretraga">Pretraga</button>
		</div>
			Uloga:
			<select name="uloga" v-model="ulogaFilter">
			<option></option>
			<option>Kupac</option>
			<option>Administrator</option>
			<option>Menadzer</option>
			<option>Dostavljac</option>
		</select>
		Tip:
			<select name="tip" v-model="tipFilter">
			<option></option>
			<option>Bronzani</option>
			<option>Srebrni</option>
			<option>Zlatni</option>
			</select>
		</div>
		<div>
			<button style="margin: 10px;" v-if="!prikazSumnjivih" v-on:click="prikaziSumnjive()">Prikaži samo sumnjive kupce </button>
			<button style="margin: 10px;" v-else-if="prikazSumnjivih" v-on:click="prikaziSve()">Prikaži sve korisnike </button>
		</div>
		 <table id="table">
		 <thead>
		   <tr>
			<th v-for="(col, index) in columns" v-on:click="sortTable(col.name)"> 
					{{names[index]}}
		   </th>
		   <th/>
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
			<td v-else=""/>
		   </tr>
		 </tbody>
	   </table>
	</div>
	`
})