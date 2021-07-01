Vue.component('admin', {
	data: function() {
		return {
			ime: null
		}
	},
	methods: {

	},
	template: `
	<div>
		<h1>Dobro došli admine!</h1>       
	    <a href="/#/registracija">Registracija novog korisnika</a>   
	    <a href="/#/sviKorisnici">Pregled svih korisnika</a>
	    </div>                   
`
})