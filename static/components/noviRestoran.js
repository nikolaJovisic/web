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

		onChangeFileUpload($event) {
			this.file = this.$refs.file.files[0];
			this.encodeImage(this.file)
		},
		encodeImage(input) {
	if(input) {
		const reader = new FileReader()
		reader.onload = (e) => {
			this.slika = e.target.result
		}
		reader.readAsDataURL(input)
	}
},

	checkForm: function(e) {
		e.preventDefault();
		if (!this.naziv || !this.tip || !this.lokacija || !this.lokacija.geografskaDuzina || !this.lokacija.geografskaSirina ||
			!this.lokacija.adresa || !this.lokacija.grad || !this.lokacija.drzava || !this.slika) {
			alert("Morate popuniti sva polja")
			e.preventDefault();
		}
		else {
			axios
				.post('/noviRestoran', {
					naziv: this.naziv,
					tip: this.tip,
					lokacija: this.lokacija,
					slika: this.slika
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
	
	beforeMount() {
					localStorage.setItem('longit', 19.83); 
					localStorage.setItem('latit', 45.25);
	
	},


	mounted() {
	

	this.lokacija = {};
	this.lokacija.geografskaDuzina = null;
	this.lokacija.geografskaSirina = null;
	this.lokacija.adresa = null;
	this.lokacija.grad = null;
	this.lokacija.drzava = null;


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

		this.$root.$on('longitude', (text) => {
			this.lokacija.geografskaDuzina = text; 
			this.$forceUpdate();
			});
		this.$root.$on('latitude', (text) => { 
			this.lokacija.geografskaSirina = text;
			this.$forceUpdate();});


},

	template: `
	
	
	<div>   
	<mapa/>
	<div>
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
				<td><input v-model="lokacija.geografskaDuzina" id = "gd"type="text"></td>
			</tr>
			<tr>
				<td>Geografska širina</td>
				<td><input v-model="lokacija.geografskaSirina" id = "gs" type="text"></td>
			</tr>
			<tr>
				<td>Adresa</td>
				<td><input v-model="lokacija.adresa" type="text" name="adresa"></td>
			</tr>
			<tr>
				<td>Grad</td>
				<td><input v-model="lokacija.grad" type="text" name="grad"></td>
			</tr>
			<tr>
				<td>Drzava</td>
				<td><input v-model="lokacija.drzava" type="text" name="drzava"></td>
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
				<input type="file" id="file" ref="file" v-on:change="onChangeFileUpload()"/>
			</tr>
			<tr>
				<td><input type="submit" value="Registruj"></td>
			</tr>
		</table>
	</form>  
	</div>          
	</div>              
`
})