Vue.component('admin', {
	data: function() {
		return {
			ime: null
		}
	},
	methods: {
		novi: function(val) {
			localStorage.setItem('registracijaNovog', val);
		}

	},
	template: `
	<div>
		<h1>Dobro došli admine!</h1>       
	    <a href="/#/registracija" v-on:click="novi(true)">Registracija novog korisnika</a>   
	    <a href="/#/sviKorisnici">Pregled svih korisnika</a>
		<a href="/#/registracija" v-on:click="novi(false)">Pregled i izmena ličnih podataka</a> </div>    
	    </div>                   
`
})