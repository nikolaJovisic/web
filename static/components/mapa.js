Vue.component('mapa', {
	data: function() {
		return {
			
		}
	},


	methods: {
		
	},

	mounted() {
		map = new ol.Map({
			target: 'map',
			layers: [
				new ol.layer.Tile({
					source: new ol.source.OSM()
				})
			],
			view: new ol.View({
				center: ol.proj.fromLonLat([19.83, 45.26]),
				zoom: 13
			})
		});
		
		v = this;

		map.on('singleclick', function(evt) {

			coordinate = ol.proj.transform(evt.coordinate, 'EPSG:3857', 'EPSG:4326');
			v.$root.$emit('longitude', coordinate[0]);
			v.$root.$emit('latitude', coordinate[1]);
		});
	},

	template: `
	<div id="map" class="map"> </div>   

`
})