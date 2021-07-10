Vue.component('prikazPorudzbina', {
	data: function() {
		return {
			porudzbine: null,
			ponude: null,
			ascending: false,
			uloga: null,
			nameSearch: '',
			odSearch: '',
			doSearch: '',
			odDatumPorudzbine: '',
			doDatumPorudzbine: '',
			sortColumn: '',
			tipFilter: '',
			res: null,
			restoraniZaKomentarisanje: null,
			statusFilter: ''

		}
	},
	computed: {


		filtriranePorudzbine: function() {
			if (this.porudzbine == null) return null;
			tip_filter = this.tipFilter
			status_filter = this.statusFilter
			return this.porudzbine.filter(function(row) {
				tip = row.restoran.tip
				status = row.status
				return tip.includes(tip_filter) && (status_filter === 'NijeDostavljena' ? !status.includes('Dostavljena') : status.includes(status_filter))
			});
		}

	},


	methods: {
		"zaKomentarisanje": function zaKomentarisanje(porudzbina) {
			for (restoran of this.restoraniZaKomentarisanje)
			{
				if (porudzbina.restoran.naziv == restoran)
					return true;
			}
			return false;
		},
		"otkazi": function otkazi(porudzbina) {
			sjwt = window.localStorage.getItem('jwt');
			axios.post('/otkazi', {
			}, {
				params: {
					IDPorudzbine: porudzbina.ID,
					jwt: sjwt
				}
			})
				.then(response => {
					if (response.data) {
						porudzbina.status = "Otkazana"
					}
				})
		},
		"pripremi": function pripremi(porudzbina) {
			sjwt = window.localStorage.getItem('jwt');
			axios.post('/pripremi', {
			}, {
				params: {
					IDPorudzbine: porudzbina.ID,
					jwt: sjwt
				}
			})
				.then(response => {
					if (response.data) {
						porudzbina.status = "CekaDostavljaca"
					}
				})
		},
		"obradi": function obrada(porudzbina) {
			sjwt = window.localStorage.getItem('jwt');
			axios.post('/obrada', {
			}, {
				params: {
					IDPorudzbine: porudzbina.ID,
					jwt: sjwt
				}
			})
				.then(response => {
					if (response.data) {
						porudzbina.status = "UPripremi"
					}
				})
		},
		"oceni": function oceni(porudzbina) {
			sjwt = window.localStorage.getItem('jwt');
			axios.post('/oceni', {
			}, {
				params: {
					IDPorudzbine: porudzbina.ID,
					jwt: sjwt
				}
			})
				.then(response => {
					if (response.data) {
						this.$router.push('/prikazKomentara/' + response.data)
					}
					else
					{
						alert('Greška prilikom ocenjivanja porudžbine')
					}
				})
		},

		"nijeZatrazena": function nijeZatrazena(porudzbina) {
			for (let i = 0; i < this.ponude.length; ++i) {
				if(this.ponude[i].porudzbinaID === porudzbina.ID) {
					return false;
				}
			}
			return true;
		},

		zatrazi(porudzbina) {
			sjwt = window.localStorage.getItem('jwt');
			axios.post('/zatraziPorudzbinu', {
			}, {
				params: {
					IDPorudzbine: porudzbina.ID,
					jwt: sjwt
				}
			})
			this.$router.go();
		},
		
		moguceZatraziti(porudzbina) {

			
			return porudzbina.status === 'CekaDostavljaca' && this.uloga === 'Dostavljac' && this.nijeZatrazena(porudzbina);
		},
		
		
		dostavi(porudzbina) {
			sjwt = window.localStorage.getItem('jwt');
			axios.post('/dostaviPorudzbinu', {
			}, {
				params: {
					IDPorudzbine: porudzbina.ID,
					jwt: sjwt
				}
			})
				.then(response => {
					if (response.data) {
						porudzbina.status = "Dostavljena"
					}
				})
		},
		"pretraga": function(e) {
			sjwt = window.localStorage.getItem('jwt');
			axios.get("/svePorudzbine",
				{
					headers: {
						'Authorization': sjwt
					},
					contentType: "application/json",
					dataType: "json",
					params: {
						nameSearch: this.nameSearch,
						odSearch: this.odSearch,
						doSearch: this.doSearch,
						odDatumPorudzbine: this.odDatumPorudzbine,
						doDatumPorudzbine: this.doDatumPorudzbine

					}
				})
				.then(response => {
					if (response.data) {
						this.porudzbine = response.data;
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
			this.porudzbine.sort(function(a, b) {
				if (a[col] > b[col]) {
					return ascending ? 1 : -1
				} else if (a[col] < b[col]) {
					return ascending ? -1 : 1
				}
				return 0;
			})
		},
		"datum": function datum(porudzbina) {
			return porudzbina.datumVreme.substring(0, 4) + '.' + porudzbina.datumVreme.substring(4, 6) + '.' + porudzbina.datumVreme.substring(6, 8) + '.'

		},
		"vreme": function datum(porudzbina) {
			return porudzbina.datumVreme.substring(8, 10) + ':' + porudzbina.datumVreme.substring(10, 12)

		}

	},

	mounted() {
		sjwt = window.localStorage.getItem('jwt');
		this.uloga = window.localStorage.getItem('role');
		axios.get("/svePorudzbine", {
			headers: {
				'Authorization': sjwt
			},
			contentType: "application/json",
			dataType: "json",
		})
			.then(response => {
				if (response.data) {
					this.porudzbine = response.data;
				}
			})
		if (window.localStorage.getItem('role') == 'Kupac')
		axios.get("/sviRestoraniZaKomentarisanje", {
			params: {
				jwt: sjwt
			},
			contentType: "application/json",
			dataType: "json",
		})
			.then(response => {
				if (response.data) {
					console.log(response.data)
					this.restoraniZaKomentarisanje = response.data;
				}
			})
			axios.get("/svePonude", {
				params: {
					jwt: sjwt
				},
				contentType: "application/json",
				dataType: "json",
			})
				.then(response => {
					if (response.data) {
						this.ponude = response.data;
					}
				})
	},

	template: `
	<div>
		<div>
			<input type="text" v-model="nameSearch" v-if="uloga !== 'Menadzer'" >
			<input type="number" v-model="odSearch">
			<input type="number" v-model="doSearch">
			<input v-model="odDatumPorudzbine" type="date">
			<input v-model="doDatumPorudzbine" type="date">
			<button v-on:click="pretraga">Pretraga</button>
		</div>
		<div>
			<label v-if="uloga !== 'Menadzer'">Tip restorana:</label>
			<select name="tip" v-model="tipFilter" v-if="uloga !== 'Menadzer'">
				<option></option>
				<option>Italijanski</option>
				<option>Kineski</option>
				<option>Rostilj</option>
			</select>
			Status:
			<select name="status" v-model="statusFilter">
				<option></option>
				<option v-if="uloga != 'Dostavljac'">Obrada</option>
				<option v-if="uloga != 'Dostavljac'">UPripremi</option>
				<option v-if="uloga == 'Kupac'">NijeDostavljena</option>
				<option>CekaDostavljaca</option>
				<option>UTransportu</option>
				<option v-if="uloga != 'Dostavljac'">Dostavljena</option>
				<option v-if="uloga != 'Dostavljac'">Otkazana</option>
			</select>
		</div>
		<table id="table">
		 <thead>
		   <tr>
		   <th v-if="uloga === 'Menadzer'">
				ID
		   </th>
		   <th v-if="uloga !== 'Menadzer'" v-on:click="sortTable('restoran.naziv')">
		   		Restoran
		   </th>
		   <th v-on:click="sortTable('datumVreme')">
		   		Datum i vreme
	  		</th>
		   <th v-on:click="sortTable('cena')">
				Cena
		   </th>
		   <th v-if="uloga !== 'Kupac'">
				Kupac
		   </th>
		   <th>
				Status
		   </th>
		   <th>
		   </th>
		   </tr>
		 </thead>
		 <tbody>
		   <tr v-for="porudzbina in filtriranePorudzbine">
		   <td v-if="uloga === 'Menadzer'">
			   {{porudzbina.ID}}
			</td>
		   	<td v-if="uloga !== 'Menadzer'">
			   {{porudzbina.restoran.naziv}}
			</td>
			<td>
				<div>
			   	{{datum(porudzbina)}}<br/>
				{{vreme(porudzbina)}}
				</div>
			</td>
			<td>
			   {{porudzbina.cena}}
			</td>
			<td v-if="uloga !== 'Kupac'">
			   {{porudzbina.imePrezimeKupca}}
			</td>
			<td>
			   {{porudzbina.status}}
			</td>


			<td >
			<a v-if="porudzbina.status === 'Obrada' && uloga === 'Menadzer'" v-on:click="obradi(porudzbina)">
				Obradi
</a>
			<a v-else-if="porudzbina.status === 'Obrada' && uloga === 'Kupac'" v-on:click="otkazi(porudzbina)">
				Otkaži
</a>
			<a v-else-if="porudzbina.status === 'Dostavljena' && uloga === 'Kupac' && zaKomentarisanje(porudzbina)" v-on:click="oceni(porudzbina)">
				Oceni
</a>
			<a v-else-if="porudzbina.status === 'UPripremi' && uloga === 'Menadzer'" v-on:click="pripremi(porudzbina)">
				Pripremi
</a>
			<a v-else-if="moguceZatraziti(porudzbina)" v-on:click="zatrazi(porudzbina)">
				Zatraži
</a>
			<a v-else-if="porudzbina.status === 'UTransportu' && uloga === 'Dostavljac'" v-on:click="dostavi(porudzbina)">
				Obeleži kao dostavljenu
</a>
			</td>	
			
		   </tr>
		 </tbody>
	   </table>
	</div>
`
})