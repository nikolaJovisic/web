Vue.component('restoraniPrikaz', {
	data: function() {
		return {
			restorani: null,
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
			  startval = col.slice()
			var ascending = this.ascending;
			this.restorani.sort(function(a, b) {
				
				if (startval === 'lokacija.adresa')
				{
					col = 'adresa'
					a = a['lokacija']
					b = b['lokacija']
				}
				console.log(a[col])
				
				if (a[col] > b[col]) {
				  return ascending ? 1 : -1
				} else if (a[col] < b[col]) {
				  return ascending ? -1 : 1
				}
				return 0;
			  })
		  },
		  "prikazRestorana": function prikazRestorana(naziv) {

			this.$router.push('/prikazRestorana/' + naziv)
			
		  }
		  ,
		  "prikazOcene": function prikazOcene(ocena) {

			return (ocena >= 1) ? ocena + '/5' : 'Nije ocenjen'
			
		  }
	},
	
	mounted() {
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
			Naziv:
			<input type="text" v-model="nameSearch">
			Lokacija:
			<input type="text" v-model="locationSearch">
			Tip:
			<select name="tipSearch" v-model="tipSearch">
					<option></option>
					<option>Italijanski</option>
					<option>Kineski</option>
					<option>Rostilj</option>
			</select>
			Ocena
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
		   <th>
		   		Logo
		   </th>
		   <th v-on:click="sortTable('naziv')">
		   		Naziv
	  		</th>
		   <th>
				Tip
		   </th>
		   <th v-on:click="sortTable('lokacija.adresa')">
				Lokacija
		   </th>
		   <th v-on:click="sortTable('prosecnaOcena')">
				Prosečna ocena
		   </th>
		   </tr>
		 </thead>
		 <tbody>
		   <tr v-for="restoran in filtriraniRestorani" v-on:click="prikazRestorana(restoran.naziv)">
		   <td>
		   <img :src="restoran.slika" /> 
		   </td>
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
			<td v-if="restoran.prosecnaOcena >= 1">
			   {{restoran.prosecnaOcena}}/5
			</td>
			<td v-else>
			   Nije ocenjen
			</td>
		   </tr>
		 </tbody>
	   </table>
	</div>

`
})