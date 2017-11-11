package com.timezones

import com.fasterxml.jackson.core.JsonProcessingException
import com.jayway.jsonpath.Configuration
import com.jayway.jsonpath.Option
import com.jayway.jsonpath.spi.json.JacksonJsonProvider
import com.jayway.jsonpath.spi.json.JsonProvider
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider
import com.jayway.jsonpath.spi.mapper.MappingProvider
import com.mashape.unirest.http.Unirest
import com.timezones.client.TimezoneClient
import com.timezones.client.UserClient
import com.timezones.model.AppUser
import com.timezones.model.Timezone
import com.timezones.utils.CommonsUtils
import com.timezones.utils.LoginUtils
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner
import java.io.IOException
import java.util.*


@RunWith(BlockJUnit4ClassRunner::class)
abstract class IntegrationTests {
    val loginUtils = LoginUtils()

    val timezoneClient = TimezoneClient()
    val userClient = UserClient()
    val utils = CommonsUtils
    val parser = CommonsUtils.parser


    fun createTimeZone(creator: String, token: String) : Timezone {
        //Given
        val sent = timezoneClient.generate()

        //When
        val response = timezoneClient.post(sent, token)

        //Then
        Assert.assertThat(response.status, Matchers.`is`(201))

        val created = timezoneClient.toTimezone(response)
        val expected = Timezone(created.id, sent.name, sent.city, sent.difference, creator)

        Assert.assertNotNull("unique id is generated", created.id)
        Assert.assertThat(created, Matchers.`is`(expected))

        return created
    }

    fun createUser(creator: String, token: String) : AppUser {
        //Given
        val sent = userClient.generate()
        val response = userClient.post(sent, token)


        //When
        Assert.assertThat(response.status, Matchers.`is`(201))

        //Then
        val created = userClient.toUser(response)
        val realCreator = if (creator == "anonymous") sent.username else creator
        val expected = AppUser(created.id, sent.username, "", setOf("USER"), realCreator)

        Assert.assertNotNull("unique id is generated", created.id)
        Assert.assertThat(created, Matchers.`is`(expected))

        return created
    }


    companion object {

        init {
            // Only one time
            Unirest.setObjectMapper(
                    object : com.mashape.unirest.http.ObjectMapper {
                        val jacksonObjectMapper = com.fasterxml.jackson.databind.ObjectMapper();

                        override fun <T> readValue(value: String, valueType: Class<T>): T {
                            try {
                                return jacksonObjectMapper.readValue(value, valueType)
                            } catch (e: IOException) {
                                throw RuntimeException(e);
                            }
                        }

                        override fun writeValue(value: Any): String {
                            try {
                                return jacksonObjectMapper.writeValueAsString(value)
                            } catch (e: JsonProcessingException) {
                                throw RuntimeException(e)
                            }
                        }
                    })

            Configuration.setDefaults(object : Configuration.Defaults {

                val jsonProvider = JacksonJsonProvider()
                val mappingProvider = JacksonMappingProvider()

                override fun jsonProvider(): JsonProvider {
                    return jsonProvider
                }

                override fun mappingProvider(): MappingProvider {
                    return mappingProvider
                }

                override fun options(): Set<Option> {
                    return EnumSet.noneOf(Option::class.java)
                }
            })
        }
    }
}


