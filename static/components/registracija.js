Vue.component('registracija', {
	data: function() {
		return {
			korisnickoIme: null,
			lozinka: null,
			ime: null,
			prezime: null,
			pol: null,
			datumRodjenja: null,
			editMode: false,
			uloga: 'Kupac',
			role: localStorage.getItem('role'),
			jwt: localStorage.getItem('jwt')
		}
	},
	methods: {
		checkRegistrationResponse: function(response, event) {
			if (!response.data) {
				alert("Neuspešna registracija.");
			}
			else {
				alert("Uspešno registrovan korisnik.");
				event.target.submit();
			}
		},

		checkEditResponse: function(response, event) {
			if (!response.data) {
				alert("Neuspešna izmena.");
			}
			else {
				alert("Uspešna izmena.");
				this.$router.push('/mainPage');
			}
		},

		checkForm: function(e) {
			e.preventDefault();
			if (!this.korisnickoIme || !this.lozinka || !this.ime || !this.prezime ||
				!this.pol || !this.datumRodjenja || !this.uloga) {
				alert("Morate popuniti sva polja")
				e.preventDefault();
			}
			else if (localStorage.getItem('registracijaNovog') === "true"){
				axios
					.post('/registracija', {
						korisnickoIme: this.korisnickoIme,
						lozinka: this.lozinka,
						ime: this.ime,
						prezime: this.prezime,
						pol: this.pol,
						datumRodjenja: this.datumRodjenja,
						uloga: this.uloga
					})
					.then(response => (this.checkRegistrationResponse(response, e)));
			}
			else
			{
				axios
					.post('/izmenaProfila', {
						korisnickoIme: this.korisnickoIme,
						lozinka: this.lozinka,
						ime: this.ime,
						prezime: this.prezime,
						pol: this.pol,
						datumRodjenja: this.datumRodjenja,
						uloga: this.uloga
					})
					.then(response => (this.checkEditResponse(response, e)));
			}

		},

		izmenaResponse: function(response) {
			this.korisnickoIme = response.data.korisnickoIme;
			this.lozinka = response.data.lozinka;
			this.ime = response.data.ime;
			this.prezime = response.data.prezime;
			this.pol = response.data.pol;
			this.datumRodjenja = response.data.datumRodjenja;
			this.uloga = response.data.uloga;
		},
		
		renderAll: function() {
			return localStorage.getItem('role')==='Administrator' && localStorage.getItem('registracijaNovog')==="true";
		}
	},

	mounted() {
		if (localStorage.getItem('registracijaNovog') === "false") {
			this.editMode = true
			axios
				.post('/izmenaPodataka', {}, { params: { jwt: this.jwt } })
				.then(response => (this.izmenaResponse(response)));
		}
	},

	template: `
	<form action="#/" method="post" @submit="checkForm">
		<table>
			<tr>
				<td>Korisničko ime</td>
				<td><input v-model="korisnickoIme" :disabled="editMode" type="text" name="korisnickoIme"></td>
			</tr>
			<tr>
				<td>Lozinka</td>
				<td><input v-model="lozinka" type="text" name="lozinka"></td>
			</tr>
			<tr>
				<td>Ime</td>
				<td><input v-model="ime" type="text" name="ime"></td>
			</tr>
			<tr>
				<td>Prezime</td>
				<td><input v-model="prezime" type="text" name="prezime"></td>
			</tr>
			<tr>
				<td>Pol</td>
				<td><select v-model="pol" name="pol">
						<option value="Muski">Muški</option>
						<option value="Zenski">Ženski</option>
						</select>
				</td>
			</tr>
			<tr>
				<td>Datum rođenja</td>
				<td><input v-model="datumRodjenja" type="date" name="datumRodjenja"></td>
			</tr>
			<div v-if="renderAll()">
			<tr>
				<td>Uloga</td>
				<td><select v-model="uloga" name="uloga">
						<option value="Kupac">Kupac</option>
						<option value="Menadzer">Menadžer</option>
						<option value="Dostavljac">Dostavljač</option>
						</select>
				</td>
			</tr>
			</div>
			
			<tr>
				<td><input type="submit" value="Registruj se"></td>
			</tr>
		</table>
	</form>             
`
})