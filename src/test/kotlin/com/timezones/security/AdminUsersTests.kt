package com.timezones.security

import com.timezones.IntegrationTests
import com.timezones.client.UserClient
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AdminUsersTests : IntegrationTests() {
    lateinit var adminToken: String
    lateinit var otherToken: String
    private val ADMIN = "admin"
    private val OTHER_USER = "othermanager"

    val client = UserClient()

    @Before
    fun init() {
        adminToken = loginUtils.successfulLogin(ADMIN, "password")
        otherToken = loginUtils.successfulLogin(OTHER_USER, "password")
    }


    @Test
    fun canCreateUser() {

        //Given
        val created = createUser(ADMIN, adminToken)


        //When
        val response = client.get(created.id!!, adminToken)

        //Then
        Assert.assertThat(response.status, Matchers.`is`(200))
        val found = client.toUser(response)

        Assert.assertThat(found, Matchers.`is`(created))
    }

    @Test
    fun canGetUser() {
        //Given
        val createdResponse = client.post(client.generate(), adminToken)
        val created = client.toUser(createdResponse)

        //When
        val response = client.get(created.id!!, adminToken)

        //Then
        Assert.assertThat(response.status, Matchers.`is`(200))
    }


    @Test
    fun canGetOtherUsersUser() {
        //Given
        val createdResponse = client.post(client.generate(), otherToken)
        val created = client.toUser(createdResponse)

        //When
        val response = client.get(created.id!!, adminToken)

        //Then
        Assert.assertThat(response.status, Matchers.`is`(200))
    }


    @Test
    fun canUpdateUser() {
        //Given
        val created = createUser(ADMIN, adminToken)
        val update = created.copy(username = utils.alphabetic(10))

        //When
        val response = client.put(update, adminToken)

        //Then
        Assert.assertThat(response.status, Matchers.`is`(200))
        val result = client.toUser(response)

        Assert.assertThat("updated as request", update, Matchers.`is`(result))
    }

    @Test
    fun canUpdateOtherUsersUser() {
        //Given
        val createdByOtherUser = createUser(OTHER_USER, otherToken)
        val update = createdByOtherUser.copy(username = utils.alphabetic(10))

        //When
        val response = client.put(update, adminToken)

        //Then
        Assert.assertThat(response.status, Matchers.`is`(200))

    }

    @Test
    fun canDeleteUser() {
        //Given
        val createdResponse = client.post(client.generate(), adminToken)
        val created = client.toUser(createdResponse)

        //When
        val response = client.delete(created.id!!, adminToken)

        //Then
        Assert.assertThat(response.status, Matchers.`is`(200))
    }


    @Test
    fun canDeleteOtherUsersUser() {
        //Given
        val createdResponse = client.post(client.generate(), otherToken)
        val createdByOtherUser = client.toUser(createdResponse)

        //When
        val response = client.delete(createdByOtherUser.id!!, adminToken)

        //Then
        Assert.assertThat(response.status, Matchers.`is`(200))
    }


}