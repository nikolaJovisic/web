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
	Hello
	</div>
`
})