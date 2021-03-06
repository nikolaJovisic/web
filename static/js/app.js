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
const PrikazRestorana = {template : '<prikazRestorana/>'}
const PrikazKomentara = {template : '<prikazKomentara/>'}
const RestoraniPrikaz = {template : '<restoraniPrikaz/>'}

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
		{path: '/noviRestoran', component: NoviRestoran},
		{path: '/prikazRestorana/:naziv', component: PrikazRestorana},
		{path: '/prikazKomentara/:id', component: PrikazKomentara},
		{path: '/restoraniPrikaz', component: RestoraniPrikaz}
	  ]
});



var app =  new Vue({
    router,
    el:"#webstore"
});
