package com.timezones.repository

import com.timezones.model.AppUser
import com.timezones.model.Timezone
import io.reactivex.Flowable
import io.reactivex.Maybe
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.reactive.RxJava2CrudRepository
import org.springframework.stereotype.Component

@Component
interface ReactiveTimezoneRepository : RxJava2CrudRepository<Timezone, String> {
    fun findByIdAndCreatedBy(id: String, createdBy: String): Maybe<Timezone>

    @Query("{'name' : { \$regex : ?0 }}")
    fun findByName(name: String): Flowable<Timezone>
    @Query("{'name' : { \$regex : ?0 }, 'createdBy' : ?1 }")
    fun findByNameAndCreatedBy(name: String, createdBy: String): Flowable<Timezone>

    fun findByCreatedBy(createdBy: String): Flowable<Timezone>

}

@Component
interface ReactiveUserRepository : RxJava2CrudRepository<AppUser, String> {
    fun findByUsername(username: String): Maybe<AppUser>
    @Query("{ \$and : [{ \$or : [ { 'id' : ?0} , { 'username' : ?0} ] } , { 'createdBy' : ?1 } ] }")
    fun find(id: String, createdBy: String): Maybe<AppUser>

    fun findByCreatedBy(createdBy: String): Flowable<AppUser>
}