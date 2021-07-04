Vue.component('menadzer', {
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
		<h1>Dobro došli menadzeru!</h1>
		<a href="/#/registracija" v-on:click="novi(false)">Pregled i izmena ličnih podataka</a> </div>                   
`
})