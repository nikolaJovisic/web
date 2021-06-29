Vue.component('registracija', {
	data: function() {
		return {
			ime: null
		}
	},
	methods: {

	},
	template: `
	<form action="/registracija" method="post">
		<table>
			<tr>
				<td>Korisničko ime</td>
				<td><input type="text" name="korisnickoIme"></td>
			</tr>
			<tr>
				<td>Lozinka</td>
				<td><input type="text" name="lozinka"></td>
			</tr>
			<tr>
				<td>Ime</td>
				<td><input type="text" name="ime"></td>
			</tr>
			<tr>
				<td>Prezime</td>
				<td><input type="text" name="prezime"></td>
			</tr>
			<tr>
				<td>Pol</td>
				<td><select name="pol">
						<option value="Muski">Muški</option>
						<option value="Zenski">Ženski</option></td>
			</tr>
			<tr>
				<td>Datum rođenja</td>
				<td><input type="date" name="datumRodjenja"></td>
			</tr>
			<tr>
				<td><input type="submit" value="Registruj se"></td>
			</tr>
		</table>
	</form>             
`
})