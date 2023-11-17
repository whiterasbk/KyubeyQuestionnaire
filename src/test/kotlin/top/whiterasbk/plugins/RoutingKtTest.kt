package top.whiterasbk.plugins

import io.ktor.client.request.*
import io.ktor.server.testing.*
import kotlin.test.Test

class RoutingKtTest {

    @Test
    fun testPostQ() = testApplication {
        application {
            configureRouting()
        }

        client.post("/q").apply {
            TODO("Please write your test here")
        }
    }
}