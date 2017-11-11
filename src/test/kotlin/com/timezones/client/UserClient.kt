package com.timezones.client

import com.jayway.jsonpath.DocumentContext
import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.Unirest
import com.timezones.controller.UserCreate
import com.timezones.model.AppUser
import com.timezones.utils.CommonsUtils

class UserClient : AppRestClient(){

    val utils = CommonsUtils
    private val baseUrl = "${config.url}/users"

    fun post(create: UserCreate, token: String): HttpResponse<String> {
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

    fun put(update: AppUser, token: String): HttpResponse<String> {
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

    fun toUser(response: HttpResponse<String>): AppUser {
        return toUser(parser.parse(response.body))
    }

    fun toUser(json: DocumentContext): AppUser {
        return AppUser(
                id = json.read<String>("$.id"),
                username = json.read<String>("$.username")!!,
                password = json.read<String?>("$.password").orEmpty(),
                roles = json.read<List<String>>("$.roles")!!.toSet(),
                createdBy = json.read("$.createdBy")!!
        )
    }

    fun generate(): UserCreate {
        val username = utils.alphabetic(10)
        return UserCreate(username = username, password = utils.alphabetic(10))
    }



}