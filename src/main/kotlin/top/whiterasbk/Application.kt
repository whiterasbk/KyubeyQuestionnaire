package top.whiterasbk

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import top.whiterasbk.plugins.*

const val DEFAULT_PORT = 8080
const val DEFAULT_HOST = "0.0.0.0"

fun main() {
    embeddedServer(Netty, environment = applicationEngineEnvironment {

        module {
            app()
        }

        val providePort = config.propertyOrNull("qb.deployment.port")?.getString()?.toIntOrNull()
        val provideHost = config.propertyOrNull("qb.deployment.host")?.getString()

        connector {
            port = providePort ?: DEFAULT_PORT
            host = provideHost ?: DEFAULT_HOST
        }

    }).start(wait = true)
//    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module, configure = {
//
//    }).start(wait = true)
}

fun Application.app() {
    DatabaseFactory.init(environment.config)
    configureHTTP()
    configureSerialization()
    configureTemplating()
    configureRouting()
}
