Vue.component('mainPage', {
	data: function() {
		return {
			logged: false,
			role : window.localStorage.getItem('role')
		}
	},
	methods: {
		
		
	},
	
	mounted (){

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
		},
		
	template: `
	<div>
	<h1>Main!</h1>
	<div v-if="role=='Administrator'">
		<h1> Admin </h2>
	</div> 
	<div v-if="role=='Menadzer'">
		<h1> Menadzer </h2>
	</div> 
	<div v-if="role=='Dostavljac'">
		<h1> Dostavljac </h2>
	</div> 
	<div v-if="role=='Kupac'">
		<h1> Kupac </h2>
	</div> 
	</div>          
`
})