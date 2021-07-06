Vue.component('kupac', {
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
		<h1>Dobro došli kupče!</h1>                
		
		<a href="/#/registracija" v-on:click="novi(false)">Pregled i izmena ličnih podataka</a>
		<a href="/#/restoraniPrikaz">Pregled restorana i narucivanje</a>

	 </div>      
		      
`
})