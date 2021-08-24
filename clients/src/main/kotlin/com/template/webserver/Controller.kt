package com.template.webserver

import com.template.states.TemplateState
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Define your API endpoints here.
 */
@RestController
@RequestMapping("/") // The paths for HTTP requests are relative to this base path.
class Controller(private val serviceHubProxy: AppServiceHubProxy) {

    companion object {
        private val logger = LoggerFactory.getLogger(RestController::class.java)
    }

    @GetMapping(value = ["/"])
    private fun root(): Any {
        return serviceHubProxy.serviceHub.myInfo
    }

    @GetMapping(value = ["/query"])
    private fun query(): Any {
        return serviceHubProxy.serviceHub.vaultService.queryBy(TemplateState::class.java)
    }
}