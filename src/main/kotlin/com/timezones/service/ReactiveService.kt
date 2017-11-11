package com.timezones.service

import com.timezones.model.Model
import io.reactivex.*
import org.springframework.data.repository.reactive.RxJava2CrudRepository
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder


/**
 * Abtraction layer with all the business logic aiming to separate the
 * protocol Rest or otherwise from the data access layer.
 *
 * Additionally it offers some authentication/authorization utils that can be further
 * extracted to another class
 */
interface ReactiveService<M : Model> {
    val repo: RxJava2CrudRepository<M, String>

    /**
     * Finds all the elements.
     */
    fun findAll(): Flowable<M>?

    /**
     * Upserts an element in to repository.
     */
    fun save(model: M): Single<M>?

    /**
     * finds an element by id.
     */
    fun findById(id: String): Maybe<M>?


    /**
     * finds an element by name.
     */
    fun findByName(name: String): Flowable<M>?

    /**
     * deletes an element by id.
     */
    fun delete(id: String): Completable?

    /**
     * observable that streams all the changes as they occur to the elements in the repo.
     */
    fun stream(): Observable<M>

    fun authorize(model: M) {
        if (!model.id.isNullOrBlank()) {
            //TODO: Convert to reactive
            val found = repo.findById(model.id).blockingGet()
            if (!allowedUpdate(found)) {
                throw AccessDeniedException("Can't acccess other users records")
            }
        }
    }

    fun currentUsername(): String {
        return auth().principal.toString()
    }

    fun userIsAdmin(): Boolean {
        return auth().authorities.map { it.authority }.contains("ADMIN")
    }

    fun userIsAnonymous() :Boolean {
        return auth().authorities.map { it.authority }.contains("ANONYMOUS")
    }


    fun allowedUpdate(record: M): Boolean {
        return record.createdBy == currentUsername() || userIsAdmin()
    }

    private fun auth() = SecurityContextHolder.getContext().authentication

}
