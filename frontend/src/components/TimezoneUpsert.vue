<template>
<div class='col-sm-4 col-sm-offset-4'>
  <h2>Timezone</h2>
  <p>Create a Timezone.</p>
  <div class="alert alert-danger" v-if="error">
    <p>{{ error }}</p>
  </div>
  <div class="form-group">
    <input type="text" class="form-control" placeholder="Enter a name" v-model="timezone.name">
  </div>
  <div class="form-group">
    <input class="form-control" placeholder="Enter a city" v-model="timezone.city">
  </div>
  <div class="form-group">
    <input class="form-control" type="number" placeholder="Enter a Difference to GMT time" v-model="timezone.difference">
  </div>
  <!-- @click is the same as v-on:click -->
  <button class="btn btn-success" @click="saveTimezone(timezone)">Save</button>
</div>
</template>

<script>
import auth from '../auth'
import {router} from '../index'

const TIMEZONES_ENDPOINT = "/api/v1/timezones";
export default {
  data() {
    const id = this.$route.params.id;
    if (id && id !== "new") {
      this.loadTimezone(this.$route.params.id);
    }
    return {
      timezone: {
        name: '',
        city: '',
        difference: 1
      },
      error: ''
    }
  },
  methods: {
    saveTimezone(timezone) {
      const method = (timezone.id === "new") ? "post" : "put"
      this.$http[method](TIMEZONES_ENDPOINT, timezone, {
          headers: auth.getAuthHeader()
        })
        .then((data) => {
          router.go('/timezones')
        }).catch((err) => {
          this.error = err.message;
        })
    },
    loadTimezone(id) {
      this.$http.get(TIMEZONES_ENDPOINT + '/' + id, {
          headers: auth.getAuthHeader()
        })
        .then((data) => {
          this.timezone = data.body;
        }).catch((err) => {
          this.error = err;
        })
    }
  }
}
</script>
