package com.timezones.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.mongodb.core.mapping.Document

interface Model {
    val id: String?
    val createdBy: String?

    fun copy(id: String?, createdBy: String?)
}

@Document
data class AppUser(override val id: String? = null,
                   val username: String,
                   @JsonIgnore
                   val password: String?,
                   val roles: Set<String> = setOf("USER"),
                   override val createdBy: String?) : Model{

    override fun copy(id: String?, createdBy: String?) {
        this.copy(id = id, createdBy = createdBy)
    }
}

@Document
data class Timezone(
        override val id: String? = null,
        val name: String,
        val city: String,
        val difference: Int,
        override val createdBy: String?) : Model {

    override fun copy(id: String?, createdBy: String?) {
        this.copy(id = id, createdBy = createdBy)
    }
}

