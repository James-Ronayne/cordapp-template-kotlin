package com.template.webserver

import net.corda.core.node.AppServiceHub
import org.springframework.stereotype.Component

@Component
class AppServiceHubProxy {
    lateinit var serviceHub: AppServiceHub
}