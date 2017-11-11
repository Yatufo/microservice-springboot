package com.Users.security

import com.timezones.IntegrationTests
import com.timezones.client.UserClient
import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

class MagagerUsersTests : IntegrationTests() {
    lateinit var managerToken: String
    lateinit var otherToken: String
    private val MANAGER = "manager"
    private val OTHER_USER = "othermanager"

    val client = UserClient()

    @Before
    fun init() {
        managerToken = loginUtils.successfulLogin(MANAGER, "password")
        otherToken = loginUtils.successfulLogin(OTHER_USER, "password")
    }


    @Test
    fun canCreateUser() {

        //Given
        val created = createUser(MANAGER, managerToken)


        //When
        val response = client.get(created.id!!, managerToken)

        //Then
        assertThat(response.status, `is`(200))
        val found = client.toUser(response)

        assertThat(found, `is`(created))
    }

    @Test
    fun canGetUser() {
        //Given
        val createdResponse = client.post(client.generate(), managerToken)
        val created = client.toUser(createdResponse)

        //When
        val response = client.get(created.id!!, managerToken)

        //Then
        assertThat(response.status, `is`(200))
    }


    @Test
    fun cannotGetOtherUsersUser() {
        //Given
        val createdResponse = client.post(client.generate(), otherToken)
        val created = client.toUser(createdResponse)

        //When
        val response = client.get(created.id!!, managerToken)

        //Then
        assertThat(response.status, `is`(404))
    }


    @Test
    fun canUpdateUser() {
        //Given
        val created = createUser(MANAGER, managerToken)
        val update = created.copy(username = utils.alphabetic(10))

        //When
        val response = client.put(update, managerToken)

        //Then
        assertThat(response.status, `is`(200))
        val result = client.toUser(response)

        assertThat("updated as request", update, `is`(result))
    }

    @Test
    fun cannotUpdateOtherUsersUser() {
        //Given
        val createdByOtherUser = createUser(OTHER_USER, otherToken)
        val update = createdByOtherUser.copy(username = utils.alphabetic(10))

        //When
        val response = client.put(update, managerToken)

        //Then

        assertThat(response.status, `is`(404))
    }

    @Test
    fun canDeleteUser() {
        //Given
        val createdResponse = client.post(client.generate(), managerToken)
        val created = client.toUser(createdResponse)

        //When
        val response = client.delete(created.id!!, managerToken)

        //Then
        assertThat(response.status, `is`(200))
    }


    @Test
    fun cannotDeleteOtherUsersUser() {
        //Given
        val createdResponse = client.post(client.generate(), otherToken)
        val createdByOtherUser = client.toUser(createdResponse)

        //When
        val response = client.delete(createdByOtherUser.id!!, managerToken)

        //Then
        assertThat(response.status, `is`(403))
    }


}