package com.timezones.security

import com.timezones.IntegrationTests
import com.timezones.client.TimezoneClient
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AdminTimezonesTests : IntegrationTests() {
    lateinit var adminToken: String
    lateinit var otherToken: String
    private val AMDIN = "admin"
    private val OTHER_USER = "otheruser"

    val client = TimezoneClient()

    @Before
    fun init() {
        adminToken = loginUtils.successfulLogin(AMDIN, "password")
        otherToken = loginUtils.successfulLogin(OTHER_USER, "password")
    }


    @Test
    fun canCreateUser() {
        //Given

        //When
        val userCreate = userClient.generate()
        val createResponse = userClient.post(userCreate, adminToken)

        //Then
        Assert.assertThat(createResponse.status, Matchers.`is`(201))
    }

    @Test
    fun canCreateTimezone() {

        //Given
        val created = createTimeZone(AMDIN, adminToken)


        //When
        val response = client.get(created.id!!, adminToken)

        //Then
        Assert.assertThat(response.status, Matchers.`is`(200))
        val found = client.toTimezone(response)

        Assert.assertThat(found, Matchers.`is`(created))
    }

    @Test
    fun canGetTimezone() {
        //Given
        val createdResponse = client.post(client.generate(), adminToken)
        val created = client.toTimezone(createdResponse)

        //When
        val response = client.get(created.id!!, adminToken)

        //Then
        Assert.assertThat(response.status, Matchers.`is`(200))
    }


    @Test
    fun canGetTimezoneByName() {
        //Given
        val createdResponse = client.post(client.generate(), adminToken)
        val created = client.toTimezone(createdResponse)

        //When
        val response = client.getByName(created.name, adminToken)
        val ids = parser.parse(response.body).read<List<String>>("$.[*].id")

        //Then
        Assert.assertThat(response.status, Matchers.`is`(200))
        Assert.assertThat(ids, Matchers.contains(created.id))
    }

    @Test
    fun canGetOtherUsersTimezone() {
        //Given
        val createdResponse = client.post(client.generate(), otherToken)
        val created = client.toTimezone(createdResponse)

        //When
        val response = client.get(created.id!!, adminToken)

        //Then
        Assert.assertThat(client.toTimezone(response), Matchers.`is`(created))
        Assert.assertThat(response.status,  Matchers.`is`(200))
    }


    @Test
    fun canUpdateTimezone() {
        //Given
        val created = createTimeZone(AMDIN, adminToken)
        val update = created.copy(name = "otherName", city = "otherCity", difference = -66)

        //When
        val response = client.put(update, adminToken)

        //Then
        Assert.assertThat(response.status, Matchers.`is`(200))
        val result = client.toTimezone(response)

        Assert.assertThat("updated as request", update, Matchers.`is`(result))
    }

    @Test
    fun canUpdateOtherUsersTimezone() {
        //Given
        val createdByOtherUser = createTimeZone(OTHER_USER, otherToken)
        val update = createdByOtherUser.copy(name = "otherName", city = "otherCity", difference = -66)

        //When
        val response = client.put(update, adminToken)

        //Then
        Assert.assertThat(response.status, Matchers.`is`(200))

    }

    @Test
    fun canDeleteTimezone() {
        //Given
        val createdResponse = client.post(client.generate(), adminToken)
        val created = client.toTimezone(createdResponse)

        //When
        val response = client.delete(created.id!!, adminToken)

        //Then
        Assert.assertThat(response.status, Matchers.`is`(200))
    }


    @Test
    fun canDeleteOtherUsersTimezone() {
        //Given
        val createdResponse = client.post(client.generate(), otherToken)
        val createdByOtherUser = client.toTimezone(createdResponse)

        //When
        val response = client.delete(createdByOtherUser.id!!, adminToken)

        //Then
        Assert.assertThat(response.status, Matchers.`is`(200))
    }


}