Vue.component('login', {
	data: function() {
		return {
			korisnickoIme: null,
			lozinka: null,
		}
	},


	methods: {
		checkResponse: function(response, event) {
			if (JSON.parse(JSON.stringify(response.data))[0] === "-1") {
				alert("Pogrešni kredencijali.")
			}
			else if (JSON.parse(JSON.stringify(response.data))[0] === "-2") {
				alert("Korisnik blokiran.")
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
		localStorage.setItem("role", '');
		localStorage.setItem("jwt", '-1');
		localStorage.setItem("registracijaNovog", true);
		localStorage.setItem("aktuelniRestoran", "null");

	},

	template: `
	<div>
		<h1>Dobro došli!</h1>
		<div>
			<form @submit="checkForm" action="#/mainPage" method="post" class="center">
				<input class="textinput" v-model="korisnickoIme" type="text" placeholder="Uneti korisničko ime" id="korisnickoIme" name="korisnickoIme"><br/>
				<input class="textinput" v-model="lozinka" type="password" placeholder="Uneti lozinku" id="lozinka" name="lozinka"><br/>
				<button class="textinput" type="submit">Prijava</button>
				<a class="textinput" href="/#/registracija">Registracija</a>    </form>
		</div>
	</div>

`
})