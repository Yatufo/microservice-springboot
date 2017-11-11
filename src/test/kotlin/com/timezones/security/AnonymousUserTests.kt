package com.timezones.security

import com.timezones.IntegrationTests
import org.junit.Before
import org.junit.Test

class AnonymousUserTests : IntegrationTests() {

    lateinit var succesfulToken: String

    private val USERNAME = "anonymous"

    @Before
    fun init() {
        succesfulToken = loginUtils.successfulLogin(USERNAME, "password")
    }


    @Test
    fun unsuccessfulLogin() {
        loginUtils.unsuccessfulLogin(USERNAME, "wrong")
    }

    @Test
    fun anonymousCanCreateUser() {
        createUser(USERNAME,succesfulToken)
    }


}
