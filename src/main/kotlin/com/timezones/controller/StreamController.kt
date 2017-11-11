package com.timezones.controller

import com.timezones.model.Timezone
import com.timezones.service.ReactiveService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter


@RestController
@RequestMapping("api/v1/streams")
class StreamController {

    @Autowired
    private lateinit var service: ReactiveService<Timezone>

    @GetMapping("/timezones")
    fun timezones(): SseEmitter {
        val sseEmitter = SseEmitter(Long.MAX_VALUE)
        val observable = service.stream()
        observable.subscribe(
                sseEmitter::send,
                sseEmitter::completeWithError,
                sseEmitter::complete
        )
        return sseEmitter
    }

}