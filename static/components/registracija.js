Vue.component('registracija', {
	data: function() {
		return {
			korisnickoIme: null,
			lozinka: null,
			ime: null,
			prezime: null,
			pol: null,
			datumRodjenja: null,
			uloga: 'Kupac',
			role: localStorage.getItem('role')
		}
	},
	methods: {
	checkResponse : function(response, event){
			if(!response.data){
				alert("Neuspešna registracija.");
			}
			else{
				alert("Uspešno registrovan korisnik.");
				event.target.submit();
			}
		},
	
	checkForm: function(e) {
			e.preventDefault();
			if(!this.korisnickoIme || !this.lozinka || !this.ime || !this.prezime ||
					!this.pol || !this.datumRodjenja || !this.uloga)
			{
				alert("Morate popuniti sva polja")
				e.preventDefault();
			}
			else
			{
			axios
	          	.post('/registracija', {korisnickoIme: this.korisnickoIme, 
	        	  					  lozinka: this.lozinka,
	        	  					  ime : this.ime,
	        	  					  prezime: this.prezime,
	        	  					  pol : this.pol,
	        	  					  datumRodjenja : this.datumRodjenja,
	        	  					  uloga : this.uloga
	        	  					  })
				.then(response => (this.checkResponse(response, e)));
			}

		}
	},
	template: `
	<form action="#/" method="post" @submit="checkForm">
		<table>
			<tr>
				<td>Korisničko ime</td>
				<td><input v-model="korisnickoIme" type="text" name="korisnickoIme"></td>
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
			<div v-if="role==='Administrator'">
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