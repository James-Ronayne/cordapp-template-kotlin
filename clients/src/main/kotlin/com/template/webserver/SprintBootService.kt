package com.template.webserver

import net.corda.core.node.AppServiceHub
import net.corda.core.node.services.CordaService
import net.corda.core.node.services.ServiceLifecycleEvent
import net.corda.core.node.services.ServiceLifecycleObserver
import net.corda.core.serialization.SingletonSerializeAsToken
import net.corda.core.utilities.loggerFor
import org.springframework.boot.Banner
import org.springframework.boot.SpringApplication
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Our Spring Boot application.
 */
@SpringBootApplication
private open class StarterWebServer

@CordaService
class SprintBootService (private val serviceHub: AppServiceHub) : SingletonSerializeAsToken() {

    companion object {
        val logger = loggerFor<SprintBootService>()
    }

    init {
        logger.info("SpringBootService init starting ...")

        // Optional: Express interest in receiving lifecycle events
        serviceHub.register(observer = object : ServiceLifecycleObserver {
            override fun onServiceLifecycleEvent(event: ServiceLifecycleEvent) {
                //Lifecycle event handling code
                logger.info("ServiceLifecycleObserver received event: ${event.name} ...")
                if (event == ServiceLifecycleEvent.STATE_MACHINE_STARTED) {
                    startup()
                }
            }
        })
    }

    fun startup() {
        Thread().run() {
            logger.info("SpringBootService startup ...")
            // Set current thread initialising spring to this cordapps classloader
            // So spring can find the correct classes rather the corda nodes classpath
            Thread.currentThread().contextClassLoader = StarterWebServer::class.java.classLoader
            val app = SpringApplication(StarterWebServer::class.java)
            app.setBannerMode(Banner.Mode.OFF)
            app.webApplicationType = WebApplicationType.SERVLET
            app.setDefaultProperties(mapOf("server.port" to serviceHub.getAppContext().config.get("serverPort")))
            val context = app.run()
            val proxy = context.getBean(AppServiceHubProxy::class.java)
            proxy.serviceHub = serviceHub
            logger.info("SpringBootService PostConstruct completed ...")
        }
    }
}