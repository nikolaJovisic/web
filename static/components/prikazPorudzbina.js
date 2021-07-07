Vue.component('prikazPorudzbina', {
	data: function() {
		return {
			porudzbine: null,
			ascending: false,
			uloga: null,
			sortColumn: ''

		}
	},
	computed: {

		filtriranePorudzbine: function () {
			if (this.porudzbine == null) return null;
			return this.porudzbine.filter(function(row){
				return true;    
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
			this.porudzbine.sort(function(a, b) {
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
		sjwt = window.localStorage.getItem('jwt');
		this.uloga = window.localStorage.getItem('role');
		axios.get("/svePorudzbine",{
			headers: {
				'Authorization': sjwt
			  },
			  contentType:"application/json",
			dataType:"json",
			  })
			.then(response => {
				if(response.data)
				{ 
					this.porudzbine = response.data;
				}
			})
	},
	
	template: `
	<div>
	<table id="table">
		 <thead>
		   <tr>
		   <th>
		   		Restoran
		   </th>
		   <th>
		   		Datum i vreme
	  		</th>
		   <th>
				Cena
		   </th>
		   <th>
				Kupac
		   </th>
		   </tr>
		 </thead>
		 <tbody>
		   <tr v-for="porudzbina in filtriranePorudzbine">
		   	<td>
			   {{porudzbina.restoran.naziv}}
			</td>
			<td>
			   	{{porudzbina.datumVreme.date.day}}/{{porudzbina.datumVreme.date.month}}/{{porudzbina.datumVreme.date.year}}<br/>
				   {{porudzbina.datumVreme.time.hour}}:{{porudzbina.datumVreme.time.minute}}
			</td>
			<td>
			   {{porudzbina.cena}}
			</td>
			<td>
			   {{porudzbina.imePrezimeKupca}}
			</td>
		   </tr>
		 </tbody>
	   </table>
	</div>
`
})