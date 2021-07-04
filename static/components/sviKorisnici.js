Vue.component('sviKorisnici', {
	data: function() {
		return {
			logged: false,
			role : window.localStorage.getItem("role"),
			korisnici: null,
			ascending: false,
			sortColumn: '',
			columns: [{ name: "korisnickoIme" }, { name: "ime" }, { name: "prezime" }]
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
			console.log(col)
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
		 <table id="table">
		 <thead>
		   <tr>
			<th v-for="col in columns" v-on:click="sortTable(col.name)"> 
				{{col.name}} 
		   </th>
		   </tr>
		 </thead>
		 <tbody>
		   <tr v-for="korisnik in korisnici">
		   	<td>{{ korisnik.korisnickoIme }}</td>
			<td>{{ korisnik.ime }}</td>
			<td>{{ korisnik.prezime }}</td>
		   </tr>
		 </tbody>
	   </table>
	</div>
	`
})