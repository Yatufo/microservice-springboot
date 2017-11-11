package com.timezones.client

import com.jayway.jsonpath.DocumentContext
import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.Unirest
import com.timezones.controller.TimezoneCreate
import com.timezones.model.Timezone
import com.timezones.utils.CommonsUtils.alphabetic
import java.util.*

class TimezoneClient : AppRestClient() {

    private val baseUrl = "${config.url}/timezones"

    fun post(create: TimezoneCreate, token: String): HttpResponse<String> {
        return Unirest.post(baseUrl)
                .header("Authorization", "Bearer $token")
                .header("Content-Type", "application/json")
                .body(create)
                .asString()
    }

    fun get(id: String, token: String): HttpResponse<String> {
        return Unirest.get("$baseUrl/$id")
                .header("Authorization", "Bearer $token")
                .asString()
    }

    fun getByName(name: String, token: String): HttpResponse<String> {
        return Unirest.get("$baseUrl?name=$name")
                .header("Authorization", "Bearer $token")
                .asString()
    }

    fun put(update: Timezone, token: String): HttpResponse<String> {
        return Unirest.put(baseUrl)
                .header("Authorization", "Bearer $token")
                .header("Content-Type", "application/json")
                .body(update)
                .asString()
    }

    fun delete(id: String, token: String): HttpResponse<String> {
        return Unirest.delete("$baseUrl/$id")
                .header("Authorization", "Bearer $token")
                .asString()
    }

    fun toTimezone(response: HttpResponse<String>): Timezone {
        return toTimezone(parser.parse(response.body))
    }

    //TODO: fix mapping problem
    fun toTimezone(json: DocumentContext): Timezone {
        return Timezone(
                id = json.read<String>("$.id"),
                name = json.read("$.name")!!,
                city = json.read("$.city")!!,
                difference = json.read<Int>("$.difference")!!,
                createdBy = json.read("$.createdBy")!!
        )
    }

    fun generate() : TimezoneCreate {
        return  TimezoneCreate(name = alphabetic(10), city = alphabetic(10), difference = Random().nextInt() % 30)
    }

}
