package com.timezones.service

import com.timezones.model.Timezone
import com.timezones.repository.ReactiveTimezoneRepository
import io.reactivex.*
import io.reactivex.subjects.PublishSubject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ReactiveTimezoneService : ReactiveService<Timezone> {

    val subject = PublishSubject.create<Timezone>()

    @Autowired
    override lateinit var repo: ReactiveTimezoneRepository

    override fun save(model: Timezone): Single<Timezone>? {
        val modelWithUser = if (model.id.isNullOrEmpty()) model.copy(createdBy = currentUsername()) else model
        authorize(modelWithUser)

        return repo.save(modelWithUser)
                .doOnSuccess {
                    subject.onNext(it)
                }
    }


    override fun findByName(name: String): Flowable<Timezone>? {
        val regex = ".*${name}.*"
        return if (userIsAdmin())
            repo.findByName(regex)
        else
            repo.findByNameAndCreatedBy(regex, currentUsername())
    }

    override fun findAll(): Flowable<Timezone>? {
        return if (userIsAdmin()) repo.findAll() else repo.findByCreatedBy(currentUsername())
    }

    override fun findById(id: String): Maybe<Timezone>? {
        return if(userIsAdmin()) repo.findById(id) else repo.findByIdAndCreatedBy(id, currentUsername())
    }

    override fun delete(id: String): Completable? {
        val model = repo.findById(id).blockingGet()
        authorize(model)
        return repo.deleteById(model.id)
    }

    override fun stream(): Observable<Timezone> {

        return Observable.create<Timezone>({ emitter ->
            subject.subscribe(
                    emitter::onNext,
                    emitter::onError,
                    emitter::onComplete
            )
        })
    }
}
