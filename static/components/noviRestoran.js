Vue.component('noviRestoran', {
	data: function() {
		return {
			naziv: null,
			tip: null,
			lokacija: null,
			slika: null,
			menadzer: null,
			menadzeri: null
		}
	},
	methods: {
		checkResponse: function(response, event) {
			if (!response.data) {
				alert("Neuspešna registracija.");
			}
			else {
				alert("Uspešno registrovan restoran.");
				event.target.submit();
			}
		},

		checkForm: function(e) {
			e.preventDefault();
			if (!this.naziv || !this.tip || !this.lokacija || !this.lokacija.geografskaDuzina || !this.lokacija.geografskaSirina ||
				!this.lokacija.adresa || !this.slika) {
				alert("Morate popuniti sva polja")
				e.preventDefault();
			}
			else {
				axios
					.post('/noviRestoran', {
						naziv: this.naziv,
						tip: this.tip,
						lokacija: this.lokacija,
						//slika: this.slika
					}, { params: { menadzer: this.menadzer } })
					.then(response => (this.checkResponse(response, e)));
				if (this.menadzeri.length === 0) {
					alert("Trenutno nema slobodnih menadzera, kreirati novog koji će biti povezan sa ovim restoranom.")
					this.$router.push('/registracija');
					localStorage.setItem("aktuelniRestoran", this.naziv);			
				}
			}
		}
	},

	mounted() {
		this.lokacija = {};
		this.lokacija.geografskaDuzina = null;
		this.lokacija.geografskaSirina = null;
		this.lokacija.adresa = null;


		axios.get("/slobodniMenadzeri", {
			headers: {
			},
			contentType: "application/json",
			dataType: "json",
		})
			.then(response => {
				if (response.data) {
					this.menadzeri = response.data;
				}
			})
			
	},

	template: `
	<form action="#/" method="post" @submit="checkForm">
		<table>
			<tr>
				<td>Naziv</td>
				<td><input v-model="naziv" type="text" name="naziv"></td>
			</tr>
			<tr>
				<td>Tip</td>
				<td><select v-model="tip" name="tip">
						<option value="Italijanski">Italijanski</option>
						<option value="Kineski">Kineski</option>
						<option value="Rostilj">Rostilj</option>
						</select>
				</td>
			</tr>
			<tr>
				<td>Geografska dužina</td>
				<td><input v-model="lokacija.geografskaDuzina" type="text" name="geografskaDuzina"></td>
			</tr>
			<tr>
				<td>Geografska širina</td>
				<td><input v-model="lokacija.geografskaSirina" type="text" name="geografskaSirina"></td>
			</tr>
			<tr>
				<td>Adresa</td>
				<td><input v-model="lokacija.adresa" type="text" name="adresa"></td>
			</tr>
			<tr v-if="this.menadzeri.length !== 0">
					<td>Menadzer</td>
					<td><select v-model="menadzer">
							<option v-for="item in menadzeri" selected="selected">{{item}}</option>
							</select>
					</td>
			</tr>
			<tr>
				<td>Slika</td>
				<td><input v-model="slika" type="file" ></td>
			</tr>
			<tr>
				<td><input type="submit" value="Registruj"></td>
			</tr>
		</table>
	</form>             
`
})