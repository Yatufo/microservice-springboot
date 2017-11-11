package com.timezones.service

import com.timezones.model.AppUser
import com.timezones.repository.ReactiveUserRepository
import io.reactivex.*
import io.reactivex.subjects.PublishSubject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class ReactiveUserService : ReactiveService<AppUser> {

    val subject = PublishSubject.create<AppUser>()

    @Autowired
    override lateinit var repo: ReactiveUserRepository
    @Autowired
    lateinit var encoder: PasswordEncoder

    override fun save(model: AppUser): Single<AppUser>? {
        val maybeUpdate = if (!model.id.isNullOrEmpty()) {
            return findById(model.id!!)?.map({
                //only can change the username or password
                val password = getEncodedPassword(model.password, it)
                it.copy(username = model.username, password = password)
            })?.switchIfEmpty(Maybe.error(NotFoundException()))?.toSingle()
        } else {
            val creator = if (userIsAnonymous()) model.username else currentUsername()
            Maybe.just(model.copy(createdBy = creator, password = getEncodedPassword(model.password, model)))
        }

        return maybeUpdate?.flatMapSingle({
            repo.save(it).doOnSuccess {
                subject.onNext(it)
            }
        })
    }

    private fun getEncodedPassword(password: String?, user: AppUser) =
            if (password.isNullOrEmpty()) user.password else encoder.encode(password)

    override fun findAll(): Flowable<AppUser>? {
        return if (userIsAdmin()) repo.findAll() else repo.findByCreatedBy(currentUsername())
    }

    override fun findByName(name: String): Flowable<AppUser>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findById(id: String): Maybe<AppUser>? {
        return if (userIsAdmin()) repo.findById(id) else repo.find(id, currentUsername())
    }

    override fun delete(id: String): Completable? {
        val model = repo.findById(id).blockingGet()
        authorize(model)
        return repo.deleteById(model.id)
    }

    override fun stream(): Observable<AppUser> {

        return Observable.create<AppUser>({ emitter ->
            subject.subscribe(
                    emitter::onNext,
                    emitter::onError,
                    emitter::onComplete
            )
        })
    }

}