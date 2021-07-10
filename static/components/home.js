Vue.component('home', {
	data: function() {
		return {};
	},
	
	template: `
	<div>
	<login class="center"/> <br/>
	<h2 class="center">Restorani iz kojih nudimo dostavu:</h2>
	<restoraniPrikaz class="center"/>
	</div>

`
})