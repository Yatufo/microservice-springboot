package com.timezones.config

import com.timezones.model.AppUser
import io.reactivex.subjects.PublishSubject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import java.util.*


@Configuration
@EnableAuthorizationServer
@EnableResourceServer
@ConfigurationProperties(prefix = "security.jwt")
class AuthorizationServerConfig : AuthorizationServerConfigurerAdapter() {

    lateinit var clientId: String
    lateinit var clientSecret: String
    var grantTypes: List<String> = Collections.emptyList()
    lateinit var scopeRead: String
    lateinit var scopeWrite: String
    var resourceIds: List<String> = Collections.emptyList()

    @Autowired
    private val tokenStore: TokenStore? = null

    @Autowired
    private val accessTokenConverter: JwtAccessTokenConverter? = null

    @Autowired
    private val authenticationManager: AuthenticationManager? = null

    @Throws(Exception::class)
    override fun configure(configurer: ClientDetailsServiceConfigurer?) {
        configurer!!.inMemory()
                .withClient(clientId)
                .secret(clientSecret)
                .authorizedGrantTypes(*grantTypes.toTypedArray())
                .scopes(scopeRead, scopeWrite)
                .resourceIds(*resourceIds.toTypedArray())

    }
    @Throws(Exception::class)
    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer?) {
        val enhancerChain = TokenEnhancerChain()
        enhancerChain.setTokenEnhancers(listOf(accessTokenConverter))
        endpoints!!.tokenStore(tokenStore)
                .accessTokenConverter(accessTokenConverter)
                .tokenEnhancer(enhancerChain)
                .authenticationManager(authenticationManager)
    }

}