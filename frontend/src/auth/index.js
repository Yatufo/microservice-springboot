import {router} from '../index'
import jwt_decode from 'jwt-decode'

// endpoints
const LOGIN_URL = '/oauth/token'
const SIGNUP_URL = "/api/v1/users"
const AUTH_HEADERS = {
  headers: {
    "Content-Type": "application/x-www-form-urlencoded",
    "Authorization": "Basic V2ViQXBwQ2xpZW50SWQ6U2VjcmV0V2ViQXBwQ2xpZW50SWQ="
  }
}

export default {
  // user object is how we check auth status
  user : {
    authenticated: false
  },

  login(context, creds, redirect) {
    var formData = new FormData();
    formData.append("grant_type", "password")
    formData.append("username", creds.username)
    formData.append("password", creds.password)

    context.$http.post(LOGIN_URL, formData, AUTH_HEADERS).then((data) => {
      localStorage.setItem('id_token', data.body.access_token);
      this.checkAuth();

      // redirect
      if (redirect) {
        router.go('home')
      }
    }).catch((err) => {
      console.log(err);
    })
  },

  signup(context, creds, redirect) {
    context.$http.post(SIGNUP_URL, creds).then((data) => {
      this.login(context, creds, redirect);
    }).catch((err) => {
      console.log(err);
    })
  },

  logout() {
    localStorage.removeItem('id_token')
    this.user.authenticated = false
  },

  checkAuth() {
    var jwt = localStorage.getItem('id_token')
    if (jwt) {
      const authorities = jwt_decode(jwt).authorities
      this.user.authenticated = true
      this.user.authorities = authorities;
      this.user.access = {
        users : authorities.includes("ADMIN") || authorities.includes("MANAGER"),
        timezones : authorities.includes("USER")
      }
    } else {
      this.user.authenticated = false
    }
  },

  getAuthHeader() {
    return {
      'Authorization': 'Bearer ' + localStorage.getItem('id_token')
    }
  }
}
