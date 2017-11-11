import Vue from 'vue'
import VueRouter from 'vue-router'
import VueResource from 'vue-resource'
import _ from 'lodash'

// components
import App from './components/App.vue'
import Users from './components/Users.vue'
import Timezones from './components/Timezones.vue'
import TimezoneUpsert from './components/TimezoneUpsert.vue'
import Login from './components/Login.vue'
import Signup from './components/Signup.vue'
import UserUpsert from './components/UserUpsert.vue'
import Home from './components/Home.vue'

// services
import auth from './auth'

Vue.use(VueResource)
Vue.use(VueRouter)

// Check the users auth status when the app starts
auth.checkAuth()

// export so we can use in components
export var router = new VueRouter()

// define routes
router.map({
	'/home': {
		component: Home
	},
	'/users': {
		component: Users
	},
	'/user/:id': {
		component: UserUpsert
	},
	'/timezone/:id': {
		component: TimezoneUpsert
	},
	'/timezones': {
		component: Timezones
	},
	'/login': {
		component: Login
	},
	'/signup': {
		component: Signup
	}
})

// fallback route
router.redirect({
	'*': '/home'
})

// expose the whole thing on element with 'app' as an id
router.start(App, '#app')
