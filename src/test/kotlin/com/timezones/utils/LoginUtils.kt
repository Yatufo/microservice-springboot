package com.timezones.utils

import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.Unirest
import org.hamcrest.Matchers
import org.junit.Assert

class LoginUtils {
    val parser = CommonsUtils.parser
    val config = CommonsUtils.config

    fun unsuccessfulLogin(username: String, wrong: String) {
        val response = authenticate(username, wrong)
        val token = parser.parse(response.body).read<String>("$.access_token")
        Assert.assertThat(response.status, Matchers.`is`(400))
        Assert.assertNull(token)
    }

    fun successfulLogin(username: String, valid: String): String {
        val authResponse = authenticate(username, valid)
        val token = parser.parse(authResponse.body).read<String>("$.access_token")
        Assert.assertNotNull(token)
        return token
    }


    fun authenticate(username: String, password: String): HttpResponse<String> {
        return Unirest.post(config.tokenUrl)
                .field("grant_type", "password")
                .field("username", username)
                .field("password", password)
                .asString()
    }

}