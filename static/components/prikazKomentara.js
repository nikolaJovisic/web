Vue.component('prikazKomentara', {
	data: function() {
		return {
            jwt: localStorage.getItem("jwt"),
            komentar: null,
            IDKomentara: this.$route.params.id

		}
	},
	methods: {
        'posalji': function posalji()
        {
            axios.post("/komentar",
            {
                komentar: this.komentar
            },
			{ params: { ID: this.komentar.ID, jwt: this.jwt } })
			.then(response => {
				if (response.data) {
					
				}
			})
        }
	},
	computed: {
	
	

	},

	mounted() {
        axios.get("/komentar",
			{ params: { ID: this.$route.params.id, jwt: this.jwt } })
			.then(response => {
				if (response.data) {
					this.komentar = response.data;
				}
			})

	
	},
	

	template: `
    <div>
        <h1>
            Ocenjujete {{komentar.restoran.naziv}}<br/>
            <img :src="komentar.restoran.slika"> <br/>
        </h1>
        <div>
            <label>Komentar:</label>
            <textarea v-model="komentar.opis">
            </textarea>
            <label>Ocena:</label>
            <select name="ocena" v-model="komentar.ocena">
					<option></option>
					<option>1</option>
					<option>2</option>
					<option>3</option>
					<option>4</option>
                    <option>5</option>
			</select>
        </div>
        <div>
            <button v-on:click="posalji">
                Pošalji
            </button>
        </div>
    </div>
	`
	})