const Home = {template : '<home></home>'}
const Admin = {template : '<admin></admin>'}
const Registracija = {template : '<registracija/>'}
const MainPage = {template : '<mainPage></mainPage>'}

const router = new VueRouter({
	mode: 'hash',
	  routes: [
		
		{path: '/admin', component: Admin},
		{path: '/mainPage', component: MainPage},
		{path: '/registracija', component: Registracija},
		{path: '/', component: Home}
	  ]
});

var app =  new Vue({
    router,
    el:"#webstore"
});
