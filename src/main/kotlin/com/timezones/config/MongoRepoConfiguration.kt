package com.timezones.config

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import com.timezones.model.Timezone
import io.reactivex.subjects.PublishSubject
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration
import org.springframework.data.mongodb.core.mapping.event.LoggingEventListener

@Configuration
@ConfigurationProperties(prefix = "repository.mongo")
class MongoRepoConfiguration : AbstractReactiveMongoConfiguration() {

    lateinit var url: String
    lateinit var dbName: String

    @Bean
    fun mongoEventListener(): LoggingEventListener {
        return LoggingEventListener()
    }

    @Bean
    override fun reactiveMongoClient(): MongoClient {
        return MongoClients.create(url)
    }

    override fun getDatabaseName() = dbName


    @Bean
    fun publishSubject(): PublishSubject<Timezone> {
        return PublishSubject.create<Timezone>()
    }

}