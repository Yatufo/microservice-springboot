<template>
  <div class="row">
    <div class='col-sm-5 col-sm-offset-1'>
      <h2>Users</h2>
      <div class="input-group">
        <div class="input-group-btn">
          <button class="btn btn-primary" v-link="'user/new'">
            <span class="glyphicon glyphicon-file" aria-hidden="true"></span>
          </button>
        </div>
      </div>
    </div>
  </div>
  <div class="row">
    <div class='col-sm-5 col-sm-offset-1'>
      <div class="panel panel-default">
        <div class="panel-heading">Lists of users you have created.</div>
        <div class="panel-body">
          <table class="table">
            <thead>
              <tr>
                <th>Actions</th>
                <th>Name</th>
                <th>Creator</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="user in users">
                <td>
                  <button class="btn btn-default" type="button" v-link="'user/' + user.id">
                     <span class="glyphicon glyphicon-edit" aria-hidden="true"></span>
                  </button>
                  <button class="btn btn-default" type="button" @click="deleteUser(user)">
                     <span class="glyphicon glyphicon-remove-sign" aria-hidden="true"></span>
                  </button>
                </td>
                <td>{{user.username}}</td>
                <td>{{user.createdBy}}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <div class='col-sm-5'>
      <div class="panel panel-default">
        <div class="panel-heading">Lists of users being created now.</div>
        <div class="panel-body">
          <table class="table">
            <thead>
              <tr>
                <th>Actions</th>
                <th>Name</th>
                <th>Creator</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="user in liveUsers">
                <td>
                  <button class="btn btn-default" type="button" v-link="'user/' + user.id">
                     <span class="glyphicon glyphicon-edit" aria-hidden="true"></span>
                  </button>
                  <button class="btn btn-default" type="button" @click="deleteUser(user)">
                     <span class="glyphicon glyphicon-remove-sign" aria-hidden="true"></span>
                  </button>
                </td>
                <td>{{user.username}}</td>
                <td>{{user.createdBy}}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import auth from '../auth'
export default {
  data() {
    this.getUsers();
    return {
      users: [],
      liveUsers:[]
    }
  },
  methods: {
    getUsers() {
      this.$http
        .get('/api/v1/users', {
          // Attach the JWT header
          headers: auth.getAuthHeader()
        }).then((data) => {
          this.users = data.body;
        })
        .catch((err) => console.log(err))
    },
    deleteUser(user) {
      this.$http
        .delete('/api/v1/users/' + user.id, {
          // Attach the JWT header
          headers: auth.getAuthHeader()
        }).then((data) => {
        	this.users = _.remove(this.users, (u) => u.id !== user.id);
        })
        .catch((err) => console.log(err))
    }

  },
  route: {
    // Check the users auth status before
    // allowing navigation to the route
    canActivate() {
			return auth.user.authenticated && auth.user.access.users;
    }
  }
}
</script>
