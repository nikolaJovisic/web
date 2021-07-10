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
			var ascending = this.ascending;
			this.restorani.sort(function(a, b) {
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
			<input type="text" v-model="nameSearch" placeholder="Naziv">
			<input type="text" v-model="locationSearch" placeholder="Lokacija">
			<select name="ocenaSearch" v-model="tipSearch" placeholder="Tip">
					<option value="" selected>Svi tipovi</option>
					<option>Italijanski</option>
					<option>Kineski</option>
					<option>Rostilj</option>
			</select>
			<select name="ocenaSearch" v-model="ocenaSearch" placeholder="Ocena">
					<option value="" selected>Ocena</option>
					<option>4-5</option>
					<option>3-5</option>
					<option>2-5</option>
					<option>1-5</option>
			</select>
			<select name="status" v-model="statusFilter">
				<option value="" selected>Status</option>
				<option>Otvoren</option>
				<option>Zatvoren</option>
			</select>
			<button v-on:click="pretraga" style="margin: 0px 30px;">Pretraga</button>
		</div> <br/>
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
			   {{restoran.lokacija.adresa}}
			</td>
		   </tr>
		 </tbody>
	   </table>
	</div>

`
})