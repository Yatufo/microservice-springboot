<template>
<div class="row">
  <div class='col-sm-5 col-sm-offset-1'>
    <h2>Timezones</h2>
    <div class="input-group">
      <div class="input-group-btn">
        <button class="btn btn-primary" v-link="'timezone/new'">
          <span class="glyphicon glyphicon-file" aria-hidden="true"></span>
        </button>
      </div>
      <input type="text" class="form-control" aria-label="..." v-model="searchName" @keyup="debouncedSearch">
      <div class="input-group-btn">
        <button class="btn btn-default" @click="search()">
          <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
        </button>
      </div>
    </div>
  </div>
</div>
<div class="row">
  <div class='col-sm-5 col-sm-offset-1'>
    <div class="panel panel-default">
      <div class="panel-heading">Lists of timezones you have created.</div>
      <div class="panel-body">
        <table class="table">
          <thead>
            <tr>
              <th>Actions</th>
              <th>Name</th>
              <th>Current Time</th>
              <th>Creator</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="tz in timezones">
              <td>
                <button class="btn btn-default" type="button" v-link="'timezone/' + tz.id">
                   <span class="glyphicon glyphicon-edit" aria-hidden="true"></span>
                </button>
                <button class="btn btn-default" type="button" @click="deleteTimezone(tz)">
                   <span class="glyphicon glyphicon-remove-sign" aria-hidden="true"></span>
                </button>
              </td>
              <td>{{tz.name}}</td>
              <td>{{tz | currentTime}}</td>
              <td>{{tz.createdBy}}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
  <div class='col-sm-5'>
    <div class="panel panel-default">
      <div class="panel-heading">Lists of timezones being created now.</div>
      <div class="panel-body">
        <table class="table">
          <thead>
            <tr>
              <th>Actions</th>
              <th>Name</th>
              <th>Current Time</th>
              <th>Creator</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="tz in liveTimezones">
              <td>
                <button class="btn btn-default" type="button" v-link="'timezone/' + tz.id">
                   <span class="glyphicon glyphicon-edit" aria-hidden="true"></span>
                </button>
                <button class="btn btn-default" type="button" @click="deleteTimezone(tz)">
                   <span class="glyphicon glyphicon-remove-sign" aria-hidden="true"></span>
                </button>
              </td>
              <td>{{tz.name}}</td>
              <td>{{tz | currentTime}}</td>
              <td>{{tz.createdBy}}</td>
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
import EventSource from 'eventsource'
import moment from 'moment'
import _ from 'lodash'

const TIMEZONES_STREAM = "/api/v1/streams/timezones";
const TIMEZONES_ENDPOINT = "/api/v1/timezones/";
export default {
  data() {
    this.subscribe();
    this.getTimezones();
    return {
      searchName : "",
      timezones: [],
      liveTimezones: []
    }
  },
  filters: {
    currentTime(tz) {
      return tz ? moment().add(tz.difference, 'hours').format('h:mm:ss a') : "";
    }
  },
  methods: {
    getTimezones() {
      this.$http
        .get(TIMEZONES_ENDPOINT, {
          // Attach the JWT header
          headers: auth.getAuthHeader()
        }).then((data) => {
          this.timezones = data.body;
        })
        .catch((err) => console.log(err))
    },
    deleteTimezone(timezone) {
      this.$http
        .delete(TIMEZONES_ENDPOINT + timezone.id, {
          headers: auth.getAuthHeader()
        }).then((data) => {
          this.timezones = _.remove(this.timezones, (u) => u.id !== timezone.id);
          this.liveTimezones = _.remove(this.liveTimezones, (u) => u.id !== timezone.id);
        })
        .catch((err) => console.log(err))
    },
    subscribe() {
      var source = new EventSource(TIMEZONES_STREAM, {
        headers: auth.getAuthHeader()
      });
      source.onmessage = (event) => {
        const created = JSON.parse(event.data);
        this.liveTimezones.push(created);
      };
      source.onerror = (err) => {
        console.log("Timezone stream error:", err);
      };
    },
    search() {
      this.$http.get(TIMEZONES_ENDPOINT, {
        params: {
          name: this.searchName
        },
        headers: auth.getAuthHeader()
      })
      .then((data) => {
        console.log(data.body);
        this.timezones = data.body;
      }).catch((err) => {
        this.error = err;
      })
    },
    debouncedSearch(){
      _.debounce(() => this.search(), 500)();
    }
  },
  route: {
    // Check the timezones auth status before
    // allowing navigation to the route
    canActivate() {
      return auth.user.authenticated && auth.user.access.timezones;
    }
  }
}
</script>
