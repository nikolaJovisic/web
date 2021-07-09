Vue.component('kupac', {
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
		<h1>Dobro došli kupče!</h1>                
		
	    <a href="/#/" v-on:click="odjava()">Odjava</a>   <br/>
		<a href="/#/registracija" v-on:click="novi(false)">Pregled i izmena ličnih podataka</a>
		<a href="/#/restoraniPrikaz">Pregled restorana i narucivanje</a>
		<prikazPorudzbina/><br/>
	 </div>      
		      
`
})