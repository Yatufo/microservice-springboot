<template>
	<div class='col-sm-4 col-sm-offset-4'>
		<h2>Signup</h2>
		<p>Create an account to get some great quotes.</p>
		<div class="alert alert-danger" v-if="error">
	        <p>{{ error }}</p>
	    </div>
		<div class="form-group">
			<input
			  type="text"
			  class="form-control"
			  placeholder="Enter a username"
			  v-model="user.username">
		</div>
		<div class="form-group">
			<input
			  type="password"
			  class="form-control"
			  placeholder="Enter a password"
				@keyup.enter="saveUser"
			  v-model="user.password">
		</div>
		<!-- @click is the same as v-on:click -->
		<button class="btn btn-success" @click="saveUser">Save</button>
	</div>
</template>

<script>
import auth from '../auth'
import {router} from '../index'

const USERS_ENDPOINT = "/api/v1/users";
export default {
	data(){
		const id = this.$route.params.id;
		if (id && id !== "new") {
			this.loadUser(this.$route.params.id);
		}
		return {
			user: {
				username: '',
				password: ''
			},
			error: ''
		}
	},
	methods: {
    saveUser() {
      const method = (this.user.id === "new") ? "post" : "put"
      this.$http[method](USERS_ENDPOINT, this.user, {
          headers: auth.getAuthHeader()
        })
        .then((data) => {
          router.go('/users')
        }).catch((err) => {
          this.error = err.message;
        })
    },
    loadUser(id) {
      this.$http.get(USERS_ENDPOINT + '/' + id, {
          headers: auth.getAuthHeader()
        })
        .then((data) => {
          this.user = data.body;
        }).catch((err) => {
          this.error = err;
        })
    }
	}
}
</script>
