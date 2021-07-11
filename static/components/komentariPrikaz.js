Vue.component('komentariPrikaz', {
	data: function() {
		return {
			jwt: window.localStorage.getItem('jwt'),
			uloga: window.localStorage.getItem('role'),
			komentari: null
		}

	},
	props: {
	restoran: Object   
   },
	computed: {

	},
	
	
	methods: {
		'odobriKomentar': function odobriKomentar(komentar)
		{
			komentar.odobren = true;
			axios.post('/odobriKomentar', {
			}, {
				params: {
					ID: komentar.ID,
					jwt: this.jwt
				}
			})
				.then(response => {
					if (response.data) {
						
					}
					else
					{
						alert('Greška prilikom odobravanja komentara')
					}
				})

		},
		'odbijKomentar': function odbijKomentar(komentar)
		{
			axios.post('/odbijKomentar', {
			}, {
				params: {
					ID: komentar.ID,
					jwt: this.jwt
				}
			})
				.then(response => {
					if (response.data) {
						
						this.$router.go();
					}
					else
					{
						alert('Greška prilikom odbijanja komentara')
					}
				})
		}

	},
	
	mounted() {
		axios.get("/sviKomentari", {
			params: {
				 jwt: this.jwt,
				 naziv: this.restoran.naziv
			},
			contentType: "application/json",
			dataType: "json",
		})
			.then(response => {
				if (response.data) {
					this.komentari = response.data;
				}
			})
	},
	
	template: `
	<div>
	<table>
	<thead>
	  <tr>
	  <th>
		   Kupac
	  </th>
	  <th>
		   Tekst
	  </th>
	  <th>
		   Ocena
	  </th>
	  <th v-if="uloga === 'Administrator' || uloga === 'Menadzer'">
	  		Status
	  </th>
	<th/>
	  </tr>
	</thead>
	<tbody>
	  <tr v-for="komentar in komentari">
	  <td>
	  	{{komentar.kupac.ime}} {{komentar.kupac.prezime}} 
	  </td>
	  <td>
	  	{{komentar.tekst}}
	  </td>
	  <td>
	  	{{komentar.ocena}}
	  </td>
	  <td v-if="komentar.odobren && (uloga === 'Administrator' || uloga === 'Menadzer')" >
	  	Odobren
	  </td>
	  <td v-else-if="uloga === 'Administrator' || uloga === 'Menadzer'">
	  	Čeka odobravanje
	  </td>
	  <td>
	  <span v-if="!komentar.odobren && uloga === 'Menadzer'">
			<button v-on:click=odobriKomentar(komentar)>Odobri</button>
			<button v-on:click=odbijKomentar(komentar)>Odbij</button>
	  </span>
</td>  
	</tr>
	</tbody>
  </table>
	</div>

`
})