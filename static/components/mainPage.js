Vue.component('mainPage', {
	data: function() {
		return {
			logged: false,
			role : window.localStorage.getItem("role")
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
				{ 
					this.logged=true;
					if(window.localStorage.getItem("role")==='Administrator')
					{
					 	this.$router.push('/admin');						
					}
					else if (window.localStorage.getItem("role")==='Menadzer'){
					 	this.$router.push('/menadzer');					
					}
					else if (window.localStorage.getItem("role")==='Dostavljac'){
					 	this.$router.push('/dostavljac');					
					}
					else if (window.localStorage.getItem("role")==='Kupac'){
					 	this.$router.push('/kupac');					
					}
				}
			})
		},
		
	template: `
	     
	`
})