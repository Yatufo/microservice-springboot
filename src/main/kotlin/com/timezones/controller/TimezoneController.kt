package com.timezones.controller


import com.timezones.model.Timezone
import com.timezones.service.NotFoundException
import com.timezones.service.ReactiveService
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.Max

class TimezoneCreate(val name: String, val city: String, @Max(30)val difference: Int)


@RestController
@RequestMapping("api/v1/timezones")
class TimezoneController {

    @Autowired
    private lateinit var service: ReactiveService<Timezone>

    @GetMapping
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    fun get(): Flowable<Timezone>? {
        return service.findAll()
    }

    @PutMapping
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    fun put(@Valid @RequestBody update: Timezone): Single<Timezone>? {
        return service.save(update)
    }

    @GetMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    fun getById(@PathVariable id: String): Maybe<Timezone>? {
        return service.findById(id)?.switchIfEmpty(Maybe.error<Timezone>(NotFoundException()))
    }

    @GetMapping(params = arrayOf("name"))
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    fun getByParams(@RequestParam name: String): Flowable<Timezone>? {
        return service.findByName(name)
    }


    @DeleteMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    fun deleteById(@PathVariable id: String): Completable? {
        return service.delete(id)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    fun post(@Valid @RequestBody create: TimezoneCreate): Single<Timezone>? {
        val timezone = Timezone(name = create.name, city = create.city, difference = create.difference, createdBy = "")
        return service.save(timezone)
    }


}