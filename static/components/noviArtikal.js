Vue.component('noviArtikal', {
	data: function() {
		return {
			naziv: null,
			cena: null,
			tip: null,
			slika: null,
			opis: null,
			kolicina: null,
			editMode: false,
			file: null,
			jwt: localStorage.getItem('jwt'),
			registracijaNovogArtikla: localStorage.getItem('registracijaNovogArtikla')
		}
	},
	methods: {
		checkRegistrationResponse: function(response, event) {
			if (!response.data) {
				alert("Neuspešna registracija.");
			}
			else {
				alert("Uspešno registrovan artikal.");
				event.target.submit();
			}
		},
		onChangeFileUpload ($event) {
			this.file = this.$refs.file.files[0];
			this.encodeImage(this.file)
		  },
		  encodeImage (input) {
			if (input) {
			  const reader = new FileReader()
			  reader.onload = (e) => {
				this.slika = e.target.result
			  }
			  reader.readAsDataURL(input)
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
			if (!this.naziv || !this.cena || !this.tip || !this.slika) {
				alert("Morate popuniti naziv, cenu, tip i sliku!")
				e.preventDefault();
			}
			else if (this.registracijaNovogArtikla === "true") {
				axios
					.post('/registracijaArtikla', {
						naziv: this.naziv,
						cena: this.cena,
						tip: this.tip,
						slika: this.slika,
						opis: this.opis,
						kolicina: this.kolicina,
					}, {params: {
								jwt: this.jwt
							}})
					.then(response => (this.checkRegistrationResponse(response, e)));


			}
			else {
				axios
					.post('/izmenaArtikla', {
						naziv: this.naziv,
						cena: this.cena,
						tip: this.tip,
						slika: this.slika,
						opis: this.opis,
						kolicina: this.kolicina,
					},
						{
							params: {
								jwt: this.jwt
							}
						})
					.then(response => (this.checkEditResponse(response, e)));
			}

		},

		izmenaResponse: function(response) {
			this.naziv = response.data.naziv;
			this.cena = response.data.cena;
			this.tip = response.data.tip;
			this.slika = response.data.slika;
			this.opis = response.data.opis;
			this.kolicina = response.data.kolicina;
		}
	},

	mounted() {

		if (this.registracijaNovogArtikla === "false") {
			this.editMode = true
			axios
				.post('/izmenaPodatakaArtikla', {}, { params: { naziv: localStorage.getItem('nazivArtikla'), jwt: this.jwt } })
				.then(response => (this.izmenaResponse(response)));
		}
	},

	template: `
	<div>
	<form action="#/" method="post" @submit="checkForm">
		<table>
			<tr>
				<td>Naziv</td>
				<td><input v-model="naziv" :disabled="editMode" type="text"></td>
			</tr>
			<tr>
				<td>Cena</td>
				<td><input v-model="cena" type="text"></td>
			</tr>
			<tr>
				<td>Tip</td>
				<td><select v-model="tip">
						<option value="Jelo">Jelo</option>
						<option value="Pice">Pice</option>
						</select>
				</td>
			</tr>
			<tr>
				<td>Slika</td>
				<input type="file" id="file" ref="file" v-on:change="onChangeFileUpload()"/>
			</tr>
			<tr>
				<td>Opis</td>
				<td><input v-model="opis" type="text"></td>
			</tr>
			
			<tr>
				<td>Kolicina</td>
				<td><input v-model="kolicina" type="text"></td>
			</tr>
			<tr>
				<td>Slika:</td>
				<td><img :src="slika"></td>
			</tr>
			<tr>
				<td><input type="submit" value="Registruj"></td>
			</tr>
		</table>
	</form>
	</div>
	            
`
})