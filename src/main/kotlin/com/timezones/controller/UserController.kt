package com.timezones.controller

import com.timezones.model.AppUser
import com.timezones.service.NotFoundException
import com.timezones.service.ReactiveService
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

data class UserCreate(val username: String, val password: String)
@RestController
@RequestMapping("api/v1/users")
class UserController {
    @Autowired
    private lateinit var service: ReactiveService<AppUser>

    @GetMapping
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    fun get(): Flowable<AppUser>? {
        return service.findAll()
    }

    @PutMapping
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    fun put(@RequestBody update: AppUser): Single<AppUser>? {
        return service.save(update) ?: throw NotFoundException()
    }

    @GetMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    fun getById(@PathVariable id: String): Maybe<AppUser>? {
        return service.findById(id)?.switchIfEmpty(Maybe.error<AppUser>(NotFoundException()))
    }

    @DeleteMapping("/{id}", produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    fun deleteById(@PathVariable id: String): Completable? {
        return service.delete(id)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    fun post(@RequestBody create: UserCreate): Single<AppUser>? {
        val user = AppUser(username = create.username, password = create.password, createdBy = "")
        return service.save(user)
    }


}