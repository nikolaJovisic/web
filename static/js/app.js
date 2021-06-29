const Home = {template : '<home></home>'}

const router = new VueRouter({
	mode: 'hash',
	  routes: [
		{path: '/', component: Home}
	  ]
});

var app =  new Vue({
    router,
    el:"#webstore"
});
