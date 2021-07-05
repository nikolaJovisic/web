Vue.component('home', {
	data: function() {
		return {
			korisnickoIme: null,
			restorani: null,
			lozinka: null,
			ascending: false,
			sortColumn: '',
			tipFilter: '',
			nameSearch: '',
			locationSearch: '',
			tipSearch: '',
			ocenaSearch: '',
			statusFilter: ''
		}
	},
	computed: {
		filtriraniRestorani: function () {
			if (this.restorani == null) return null;
			tip_filter = this.tipFilter
			status_filter = this.statusFilter

			return this.restorani.filter(function(row){
				tip = row.tip
				status = row.status ? 'Otvoren' : 'Zatvoren'
				if (status_filter == '')
					status = status_filter
				return tip.includes(tip_filter) &&  status == status_filter    
			});
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

				
		},
		"pretraga": function(e)
		{
			axios.get("/sviRestorani",
			{ params: {nameSearch: this.nameSearch, locationSearch: this.locationSearch, tipSearch: this.tipSearch, ocenaSearch: this.ocenaSearch}})
				.then(response => {
					if(response.data)
					{ 
						this.restorani = response.data;
					}
				})
		},
		"sortTable": function sortTable(col) {
			if (this.sortColumn === col) {
				this.ascending = !this.ascending;
			  } else {
				this.ascending = true;
				this.sortColumn = col;
			  } 
			var ascending = this.ascending;
			this.restorani.sort(function(a, b) {
				if (a[col] > b[col]) {
				  return ascending ? 1 : -1
				} else if (a[col] < b[col]) {
				  return ascending ? -1 : 1
				}
				return 0;
			  })
		  }
	},
	
	mounted() {
		localStorage.setItem("role", 'Kupac');
		localStorage.setItem("jwt", '-1');
		localStorage.setItem("registracijaNovog", true);
		localStorage.setItem("aktuelniRestoran", "null");


		axios.get("/sviRestorani",{
			headers: {
			  },
			  contentType:"application/json",
			dataType:"json",
			  })
			.then(response => {
				if(response.data)
				{ 
					this.restorani = response.data;
				}
			})
	},
	
	template: `
	<div>
		<div>
		<h1>Dobro došli!</h1>
			<form @submit="checkForm" action="#/mainPage" method="post">
				<input v-model="korisnickoIme" type="text" placeholder="Uneti korisničko ime" id="korisnickoIme" name="korisnickoIme">
				<input v-model="lozinka" type="password" placeholder="Uneti lozinku" id="lozinka" name="lozinka">
				<button type="submit">Prijava</button>
				<a href="/#/registracija">Registracija</a>    
			</form>
		</div>
		<div>
		<input type="text" v:model="nameSearch">
		<input type="text" v:model="locationSearch">
		<select name="ocenaSearch" v-model="tipSearch">
				<option></option>
				<option>Italijanski</option>
				<option>Kineski</option>
				<option>Rostilj</option>
		</select>
		<select name="ocenaSearch" v-model="ocenaSearch">
				<option></option>
				<option>4-5</option>
				<option>3-5</option>
				<option>2-5</option>
				<option>1-5</option>
		</select>
		<button v-on:click="pretraga">Pretraga</button>
		</div>
		<div>
			Tip restorana:
			<select name="tip" v-model="tipFilter">
				<option></option>
				<option>Italijanski</option>
				<option>Kineski</option>
				<option>Rostilj</option>
			</select>
			Status:
			<select name="status" v-model="statusFilter">
				<option></option>
				<option>Otvoren</option>
				<option>Zatvoren</option>
			</select>
		</div>
		 <table id="table">
		 <thead>
		   <tr>
		   <th v-on:click="sortTable('naziv')">
		   		Naziv
	  		</th>
		   <th>
				Tip
		   </th>
		   <th v-on:click="sortTable('lokacija.adresa')">
				Lokacija
		   </th>
		   </tr>
		 </thead>
		 <tbody>
		   <tr v-for="restoran in filtriraniRestorani">
		   	<td>
			   {{restoran.naziv}}
			</td>
			<td>
			   {{restoran.tip}}
			</td>
			<td>
			   {{restoran.lokacija.adresa}}<br>
			   {{restoran.lokacija.geografskaDuzina}},
			   {{restoran.lokacija.geografskaSirina}}
			</td>
		   </tr>
		 </tbody>
	   </table>
	</div>

`
})