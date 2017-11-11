package com.timezones.config

object TestConfig {
    var baseUrl: String = "localhost:8080"
    var url: String = "http://${baseUrl}/api/v1"
    var tokenUrl: String = "http://WebAppClientId:SecretWebAppClientId@${baseUrl}/oauth/token"
}