
<template>
<nav class='navbar navbar-default'>
  <div class='container'>
    <ul class='nav navbar-nav'>
      <li><a v-link="'/home'">Home</a></li>
      <li><a v-link="'/login'" v-if="!user.authenticated">Login</a></li>
      <li><a v-link="'/users'" v-if="user.authenticated && user.access.users">Users</a></li>
      <li><a v-link="'/timezones'" v-if="user.authenticated && user.access.timezones">Timezones</a></li>
      <li><a v-link="'/signup'" v-if="!user.authenticated">Sign Up</a></li>
      <li><a v-link="'/login'" v-if="user.authenticated" @click="logout()">Logout</a></li>
    </ul>
  </div>
</nav>
<div class='container'>
  <!-- Routes rendered here -->
  <router-view></router-view>
</div>
</template>


<script>
  import auth from '../auth'
  export default {
    data() {
      return {
        user: auth.user
      }
    },
    methods: {
      logout() {
        auth.logout()
      }
    }
  }
  </script>
