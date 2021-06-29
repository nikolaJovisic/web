Vue.component('home',{
	data: function() {
		return {
			ime: null
		}
	},
	methods: {
		
	},
	template:`
		<h1>Dobro dosli</h1>
		<form action="login.html">
    	<input type="submit" value="Uloguj se" />
		</form>
		
		<form action="registracija.html">
	    	<input type="submit" value="Registruj se" />
		</form>
`
})