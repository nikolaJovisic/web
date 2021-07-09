Vue.component('menadzer', {
	data: function() {
		return {
			kupci: null,
			jwt: localStorage.getItem("jwt"),
			ime: null
		}
	},
	methods: {
		novi: function(val) {
			localStorage.setItem('registracijaNovog', val);
		},
		noviArtikal: function(val) {
			localStorage.setItem('registracijaNovogArtikla', val);
		}
	},
	mounted() {
		axios.get("/sviKupciZaRestoran", {
			headers: {
			},
			 params: {jwt: this.jwt},
			contentType: "application/json",
			dataType: "json",
		})
			.then(response => {
				if (response.data) {
					this.kupci = response.data;
				}
			})

	},
	template: `
	<div>
		<h1>Dobro došli menadzeru!</h1>
		<a href="/#/registracija" v-on:click="novi(false)">Pregled i izmena ličnih podataka</a> <br/>
		<a href="/#/noviArtikal" v-on:click="noviArtikal(true)">Dodavanje artikla</a>
		<prikazPorudzbina/><br/>
		<h1>Prikaz ponuda:</h1>
		<ponudePrikaz/>
		<h1>Prikaz kupaca koji su ikada naručili nešto</h1>
		<div>
			<table id="table">
			<thead>
			<tr>
				<th>
					Ime
				</th>
				<th>
					Prezime
				</th>
				<th>
					Sakupljeni poeni
				</th>
			</tr>
			</thead>
			<tbody>
			<tr v-for="kupac in kupci">
				<td>
					{{kupac.ime}}
				</td>
				<td>
					{{kupac.prezime}}
				</td>
				<td>
					{{kupac.sakupljeniBodovi}}
				</td>
			</tr>
			</tbody>
		</table>
	   </div>

	</div>                   
`
})