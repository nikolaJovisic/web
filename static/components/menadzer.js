Vue.component('menadzer', {
	data: function() {
		return {
			ime: null
		}
	},
	methods: {
		novi: function(val) {
			localStorage.setItem('registracijaNovog', val);
		},
		noviArtikal: function(val) {
			localStorage.setItem('registracijaNovogArtikla', val);
		},

		odjava() {
			localStorage.setItem('jwt', -1);
			localStorage.setItem("role", "");
		}
	},
	template: `
	<div>
		<h1>Dobro došli menadzeru!</h1>
		
	    <a href="/#/" v-on:click="odjava()">Odjava</a>   <br/>
		<a href="/#/registracija" v-on:click="novi(false)">Pregled i izmena ličnih podataka</a> <br/>
		<a href="/#/noviArtikal" v-on:click="noviArtikal(true)">Dodavanje artikla</a>
		<prikazPorudzbina/><br/>
		Prikaz ponuda:
		<ponudePrikaz/>
		 </div>                   
`
})