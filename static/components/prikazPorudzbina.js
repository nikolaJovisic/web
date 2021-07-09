Vue.component('prikazPorudzbina', {
	data: function() {
		return {
			porudzbine: null,
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
		"oceni": function obrada(porudzbina) {
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
			sjwt = window.localStorage.getItem('jwt');
			axios.get("/porudzbinaZatrazena",
				{
					headers: {
						'Authorization': sjwt
					},
					contentType: "application/json",
					dataType: "json",
					params: {
						porudzbinaID: porudzbina.ID

					}
				})
				.then(response => {
					this.res = !response.data
				})
				
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

			this.nijeZatrazena(porudzbina)
			return porudzbina.status === 'CekaDostavljaca' && this.uloga === 'Dostavljac' && this.res;
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
		   </tr>
		 </thead>
		 <tbody>
		   <tr v-for="porudzbina in filtriranePorudzbine">
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
			<td v-if="porudzbina.status === 'Obrada' && uloga === 'Menadzer'">
			<button v-on:click="obradi(porudzbina)">
				Obradi
			</button>
			</td>
			<td v-else-if="porudzbina.status === 'Obrada' && uloga === 'Kupac'">
			<button v-on:click="otkazi(porudzbina)">
				Otkaži
			</button>
			</td>
			<td v-else-if="porudzbina.status === 'Dostavljena' && uloga === 'Kupac'">
			<button v-on:click="oceni(porudzbina)">
				Oceni
			</button>
			</td>
			<td v-else-if="porudzbina.status === 'UPripremi' && uloga === 'Menadzer'">
			<button v-on:click="pripremi(porudzbina)">
				Pripremi
			</button>
			</td>
			<td v-else-if="moguceZatraziti(porudzbina)">
			<button v-on:click="zatrazi(porudzbina)">
				Zatraži
			</button>
			</td>
			<td v-else-if="porudzbina.status === 'UTransportu' && uloga === 'Dostavljac'">
			<button v-on:click="dostavi(porudzbina)">
				Obeleži kao dostavljenu
			</button>
			</td>
			
		   </tr>
		 </tbody>
	   </table>
	</div>
`
})