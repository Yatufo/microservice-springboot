package com.timezones.utils

import com.jayway.jsonpath.Configuration
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.Option
import com.jayway.jsonpath.TypeRef
import com.timezones.config.TestConfig
import java.util.*
import java.util.stream.IntStream

object CommonsUtils {

    val parser = JsonPath.using(Configuration.defaultConfiguration()
            .addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL))!!

    val config = TestConfig

    val listTypeRef = object : TypeRef<Set<String>>() {}

    val alphabet = "abcdefghijklmnopqrstuvwxyz".split("".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

    fun alphabetic(maxLength: Int, random: Random = Random()): String {
        val wordLength = random.nextInt(maxLength) + 1 //Minimum one letter
        val builder = StringBuilder()

        IntStream.range(0, wordLength).forEach { i ->
            val character = alphabet[random.nextInt(alphabet.size)]
            builder.append(character)
        }
        return builder.toString()
    }
}