package com.timezones.security

import com.timezones.IntegrationTests
import com.timezones.client.TimezoneClient
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.contains
import org.junit.Assert
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

class RegularUsersTimezonesTests : IntegrationTests() {
    lateinit var userToken: String
    lateinit var otherToken: String
    private val USERNAME = "user"
    private val OTHER_USER = "otheruser"

    val client = TimezoneClient()

    @Before
    fun init() {
        userToken = loginUtils.successfulLogin(USERNAME, "password")
        otherToken = loginUtils.successfulLogin(OTHER_USER, "password")
    }

    @Test
    fun canCreateTimezone() {

        //Given
        val created = createTimeZone(USERNAME, userToken)


        //When
        val response = client.get(created.id!!, userToken)

        //Then
        assertThat(response.status, `is`(200))
        val found = client.toTimezone(response)

        assertThat(found, `is`(created))
    }

    @Test
    fun canGetTimezone() {
        //Given
        val createdResponse = client.post(client.generate(), userToken)
        val created = client.toTimezone(createdResponse)

        //When
        val response = client.get(created.id!!, userToken)

        //Then
        assertThat(response.status, `is`(200))
    }


    @Test
    fun canGetTimezoneByName() {
        //Given
        val createdResponse = client.post(client.generate(), userToken)
        val created = client.toTimezone(createdResponse)

        //When
        val response = client.getByName(created.name, userToken)
        val ids = parser.parse(response.body).read<List<String>>("$.[*].id")

        //Then
        assertThat(response.status, `is`(200))
        assertThat(ids, contains(created.id))
    }

    @Test
    fun cannotGetOtherUsersTimezone() {
        //Given
        val createdResponse = client.post(client.generate(), otherToken)
        val created = client.toTimezone(createdResponse)

        //When
        val response = client.get(created.id!!, userToken)

        //Then
        assertThat(response.status, `is`(404))
    }


    @Test
    fun canUpdateTimezone() {
        //Given
        val created = createTimeZone(USERNAME, userToken)
        val update = created.copy(name = "otherName", city = "otherCity", difference = -66)

        //When
        val response = client.put(update, userToken)

        //Then
        assertThat(response.status, `is`(200))
        val result = client.toTimezone(response)

        assertThat("updated as request", update, `is`(result))
    }

    @Test
    fun cannotUpdateOtherUsersTimezone() {
        //Given
        val createdByOtherUser = createTimeZone(OTHER_USER, otherToken)
        val update = createdByOtherUser.copy(name = "otherName", city = "otherCity", difference = -66)

        //When
        val response = client.put(update, userToken)

        //Then
        assertThat(response.status, `is`(403))

    }

    @Test
    fun canDeleteTimezone() {
        //Given
        val createdResponse = client.post(client.generate(), userToken)
        val created = client.toTimezone(createdResponse)

        //When
        val response = client.delete(created.id!!, userToken)

        //Then
        assertThat(response.status, `is`(200))
    }


    @Test
    fun cannotDeleteOtherUsersTimezone() {
        //Given
        val createdResponse = client.post(client.generate(), otherToken)
        val createdByOtherUser = client.toTimezone(createdResponse)

        //When
        val response = client.delete(createdByOtherUser.id!!, userToken)

        //Then
        assertThat(response.status, `is`(403))
    }


}