package com.timezones

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@EnableReactiveMongoRepositories
@SpringBootApplication(exclude = arrayOf(MongoAutoConfiguration::class, MongoDataAutoConfiguration::class))
class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
