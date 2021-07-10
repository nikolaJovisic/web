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
		<a href="/#/registracija" v-on:click="novi(false)">Pregled i izmena ličnih podataka</a><br/>
		<a href="/#/restoraniPrikaz">Pregled restorana i narucivanje</a><br/>
		<h1>Prikaz svih porudzbina:</h1>
		<prikazPorudzbina/><br/>
	 </div>      
		      
`
})