Vue.component('mainPage', {
	data: function() {
		return {
			logged: false
		}
	},
	methods: {
		mounted: function(){

			sjwt = window.localStorage.getItem('jwt');
			axios.get("/checkJWT",{
			headers: {
				'Authorization': sjwt,
			  },
			  contentType:"application/json",
			dataType:"json",
			  })
			.then(response => {
				if(response.data)
					this.logged=true;
			})
		}
		
	},
	template: `
	<div>
	<h1>Main!</h1>
	</div>          
`
})