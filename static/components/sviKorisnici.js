Vue.component('sviKorisnici', {
	data: function() {
		return {
			logged: false,
			role : window.localStorage.getItem("role"),
			korisnici: null,
			ascending: false,
			sortColumn: '',
			ulogaFilter: 'Kupac',
			columns: [{ name: "korisnickoIme" }, { name: "ime" }, { name: "prezime" }]
		}
	},
	computed: {
		filtriraniKorisnici: function () {
			if (this.korisnici == null) return null;
			searchTerm = this.ulogaFilter
			return this.korisnici.filter(function(row){
				uloga = row.uloga
				return uloga.includes(searchTerm)        
			});
	}
	},
	
	methods: {

		"sortTable": function sortTable(col) {
			if (this.sortColumn === col) {
				this.ascending = !this.ascending;
			  } else {
				this.ascending = true;
				this.sortColumn = col;
			  }
		
			  var ascending = this.ascending;
			this.korisnici.sort(function(a, b) {
				if (a[col] > b[col]) {
				  return ascending ? 1 : -1
				} else if (a[col] < b[col]) {
				  return ascending ? -1 : 1
				}
				return 0;
			  })
		  }
		
		
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
	<div>
		<div>
			Filter:
			<select name="uloga" v-model="ulogaFilter">
			<option></option>
			<option>Kupac</option>
			<option>Administrator</option>
			<option>Menadzer</option>
			<option>Dostavljac</option>
			</select>
		</div>
		 <table id="table">
		 <thead>
		   <tr>
			<th v-for="col in columns" v-on:click="sortTable(col.name)"> 
				{{col.name}} 
		   </th>
		   </tr>
		 </thead>
		 <tbody>
		   <tr v-for="korisnik in filtriraniKorisnici">
		   	<td v-for="col in columns">
			   {{ korisnik[col.name] }}
			</td>
		   </tr>
		 </tbody>
	   </table>
	</div>
	`
})