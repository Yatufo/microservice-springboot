package com.timezones.client

import com.jayway.jsonpath.Configuration
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.Option
import com.timezones.config.TestConfig


abstract class AppRestClient {
    val config = TestConfig
    val parser = JsonPath.using(Configuration.defaultConfiguration()
            .addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL))!!

}
