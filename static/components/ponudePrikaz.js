Vue.component('ponudePrikaz', {
	data: function() {
		return {
			ponude: null,
			uloga: null,
			jwt: window.localStorage.getItem('jwt')
		}
	},


	methods: {
		odobri(ponuda) {
			axios.post('/odobriPonudu', ponuda)
			this.$router.go();
		},
		odbij(ponuda) {
			axios.post('/odbijPonudu',  ponuda)
			this.$router.go();
		}
	},

	mounted() {
		jwt = window.localStorage.getItem('jwt');
		this.uloga = window.localStorage.getItem('role');
		axios.get("/svePonude", {
			params: { jwt: jwt },
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
		<table id="table">
		 <thead>
		   <tr>
		   <th >
		   		Dostavljac
		   </th>
		   <th>
		   		Porudzbina
	  		</th>
		   </tr>
		 </thead>
		 <tbody>
		   <tr v-for="ponuda in ponude">
		   		<td>
		   		{{ponuda.dostavljacUsername}}
		   		</td>
		   		<td>
		   		{{ponuda.porudzbinaID}}
		   		</td>
		   		
		   		<td >
					<button v-on:click="odobri(ponuda)">
						Odobri
					</button>
					<button v-on:click="odbij(ponuda)">
						Odbij
					</button>
			</td>
		   </tr>
		 </tbody>
	   </table>
	</div>
`
})