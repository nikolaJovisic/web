Vue.component('prikazPorudzbina', {
	data: function() {
		return {
			porudzbine: null,
			ascending: false,
			uloga: null,
			nameSearch: '',
			odSearch: '',
			doSearch: '',
			odDatumPorudzbine: '',
			doDatumPorudzbine: '',
			sortColumn: '',
			tipFilter : '',
			statusFilter: ''

		}
	},
	computed: {

		filtriranePorudzbine: function () {
			if (this.porudzbine == null) return null;
			tip_filter = this.tipFilter
			status_filter = this.statusFilter
			return this.porudzbine.filter(function(row){
				tip = row.restoran.tip
				status = row.status
				return tip.includes(tip_filter) && status.includes(status_filter)
			});
		}

	},
	
	
	methods: {
		"pretraga": function(e)
		{
			axios.get("/svePorudzbine",
			{
				headers: {
					'Authorization': sjwt
				  },
				  contentType:"application/json",
				dataType:"json",
				params: {
				nameSearch: this.nameSearch,
				odSearch: this.odSearch,
				doSearch: this.doSearch,
				odDatumPorudzbine: this.odDatumPorudzbine,
				doDatumPorudzbine: this.doDatumPorudzbine

			}})
				.then(response => {
					if(response.data)
					{ 
						this.porudzbine = response.data;
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
			this.porudzbine.sort(function(a, b) {
				if (a[col] > b[col]) {
				  return ascending ? 1 : -1
				} else if (a[col] < b[col]) {
				  return ascending ? -1 : 1
				}
				return 0;
			  })
		  },
		"datum": function datum(porudzbina) {
			return porudzbina.datumVreme.substring(0,4) + '.' + porudzbina.datumVreme.substring(4,6) + '.' + porudzbina.datumVreme.substring(6,8) +'.'
			
		},
		"vreme": function datum(porudzbina) {
			return porudzbina.datumVreme.substring(8,10) + ':' + porudzbina.datumVreme.substring(10,12)
			
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
		<div>
			<input type="text" v-model="nameSearch" v-if="uloga !== 'Menadzer'" >
			<input type="number" v-model="odSearch">
			<input type="number" v-model="doSearch">
			<input v-model="odDatumPorudzbine" type="date">
			<input v-model="doDatumPorudzbine" type="date">
			<button v-on:click="pretraga">Pretraga</button>
		</div>
		<div>
			Tip restorana:
			<select name="tip" v-model="tipFilter" v-if="uloga !== 'Menadzer'">
				<option></option>
				<option>Italijanski</option>
				<option>Kineski</option>
				<option>Rostilj</option>
			</select>
			Status:
			<select name="status" v-model="statusFilter">
				<option></option>
				<option>Obrada</option>
				<option>UPripremi</option>
				<option>CekaDostavljaca</option>
				<option>UTransportu</option>
				<option>Dostavljena</option>
				<option>Okazana</option>
			</select>
		</div>
		<table id="table">
		 <thead>
		   <tr>
		   <th v-if="uloga !== 'Menadzer'" v-on:click="sortTable('restoran.naziv')">
		   		Restoran
		   </th>
		   <th v-on:click="sortTable('datumVreme')">
		   		Datum i vreme
	  		</th>
		   <th v-on:click="sortTable('cena')">
				Cena
		   </th>
		   <th v-if="uloga !== 'Kupac'">
				Kupac
		   </th>
		   </tr>
		 </thead>
		 <tbody>
		   <tr v-for="porudzbina in filtriranePorudzbine">
		   	<td v-if="uloga !== 'Menadzer'">
			   {{porudzbina.restoran.naziv}}
			</td>
			<td>
				<div>
			   	{{datum(porudzbina)}}<br/>
				{{vreme(porudzbina)}}
				</div>
			</td>
			<td>
			   {{porudzbina.cena}}
			</td>
			<td v-if="uloga !== 'Kupac'">
			   {{porudzbina.imePrezimeKupca}}
			</td>
		   </tr>
		 </tbody>
	   </table>
	</div>
`
})