Vue.component('admin', {
	data: function() {
		return {
			ime: null
		}
	},
	methods: {
		novi: function(val) {
			localStorage.setItem('registracijaNovog', val);
		},

		odjava() {
			localStorage.setItem('jwt', -1);
			localStorage.setItem("role", "");
		}

	},
	template: `
	<div>
		<h1>Dobro došli admine!</h1>       
	    <a href="/#/" v-on:click="odjava()">Odjava</a>   <br/>
	    <a href="/#/registracija" v-on:click="novi(true)">Registracija novog korisnika</a>   <br/>
	    <a href="/#/sviKorisnici">Pregled svih korisnika</a><br/>
		<a href="/#/registracija" v-on:click="novi(false)">Pregled i izmena ličnih podataka</a> <br/>
		<a href="/#/noviRestoran">Novi restoran</a>    <br/>
	    </div>                   
`
})