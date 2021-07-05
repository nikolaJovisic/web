const Home = {template : '<home></home>'}
const Admin = {template : '<admin></admin>'}
const Menadzer = {template : '<menadzer/>'}
const Dostavljac = {template : '<dostavljac/>'}
const Kupac = {template : '<kupac/>'}
const Registracija = {template : '<registracija/>'}
const MainPage = {template : '<mainPage></mainPage>'}
const SviKorisnici = {template : '<sviKorisnici/>'}
const NoviRestoran = {template : '<noviRestoran/>'}
const NoviArtikal = {template : '<noviArtikal/>'}

const router = new VueRouter({
	mode: 'hash',
	  routes: [
		
		{path: '/admin', component: Admin},
		{path: '/menadzer', component: Menadzer},
		{path: '/dostavljac', component: Dostavljac},
		{path: '/kupac', component: Kupac},
		{path: '/mainPage', component: MainPage},
		{path: '/registracija', component: Registracija},
		{path: '/', component: Home},
		{path: '/sviKorisnici', component: SviKorisnici},
		{path: '/izmenaPodataka', component: Registracija},
		{path: '/noviArtikal', component: NoviArtikal},
		{path: '/noviRestoran', component: NoviRestoran}
	  ]
});



var app =  new Vue({
    router,
    el:"#webstore"
});
