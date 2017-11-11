package com.timezones.service

import com.timezones.repository.ReactiveUserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class DefaultUsersDetailsService(val repo: ReactiveUserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails? {
        //TODO: Not return null when not found
        return repo.findByUsername(username.orEmpty()).map({
            User(it.username, it.password, it.roles.map { GrantedAuthority { it } })
        })?.blockingGet()
    }
}