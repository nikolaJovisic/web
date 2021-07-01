Vue.component('home', {
	data: function() {
		return {
			korisnickoIme: null,
			lozinka: null
		}
	},
	
	
	methods: {
		checkResponse: function(response, event) {
			if (JSON.parse(JSON.stringify(response.data))[0] === "-1") {
				alert("Pogrešni kredencijali.")
			}
			else {
				localStorage.setItem('jwt', JSON.parse(JSON.stringify(response.data))[0]);
				localStorage.setItem("role", JSON.parse(JSON.stringify(response.data))[1]);
				event.target.submit();
			}
		},
		checkForm: function(e) {
			e.preventDefault();
			axios
				.post('/logovanje', {}, { params: { korisnickoIme: this.korisnickoIme, lozinka: this.lozinka } }
				)
				.then(response => (this.checkResponse(response, e)));

		}
	},
	
	mounted() {
		localStorage.setItem("role", 'Kupac');
	},
	
	template: `
	<div>
		<h1>Dobro došli!</h1>
		<form @submit="checkForm" action="#/mainPage" method="post">
			<input v-model="korisnickoIme" type="text" placeholder="Uneti korisničko ime" id="korisnickoIme" name="korisnickoIme">
			<input v-model="lozinka" type="password" placeholder="Uneti lozinku" id="lozinka" name="lozinka">
			<button type="submit">Prijava</button>
			<a href="/#/registracija">Registracija</a>    
		</form>
	</div>

`
})