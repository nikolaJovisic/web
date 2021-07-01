Vue.component('sviKorisnici', {
	data: function() {
		return {
			logged: false,
			role : window.localStorage.getItem("role"),
			korisnici: null
		}
	},
	methods: {
		
		
	},
	
	mounted (){

			axios.get("/sviKorisnici",{
			headers: {
			  },
			  contentType:"application/json",
			dataType:"json",
			  })
			.then(response => {
				if(response.data)
				{ 
					this.korisnici = response.data;
				}
			})
		},
		
	template: `
	     <ul>
  			<li v-for="korisnik in korisnici">
    			Korisničko ime: {{ korisnik.korisnickoIme }}
    			Lozinka: {{ korisnik.lozinka }}
  			</li>
		 </ul>
	`
})