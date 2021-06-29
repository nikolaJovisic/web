const Home = {template : '<home></home>'}
const Admin = {template : '<admin></admin>'}
const Registracija = {template : '<registracija/>>'}

const router = new VueRouter({
	mode: 'hash',
	  routes: [
		{path: '/', component: Home},
		{path: '/admin', component: Admin},
		{path: '/registracija', component: Registracija}
	  ]
});

var app =  new Vue({
    router,
    el:"#webstore"
});
