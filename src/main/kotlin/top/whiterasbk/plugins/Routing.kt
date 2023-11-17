package top.whiterasbk.plugins

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

fun Application.configureRouting() {

    val dao = DAOFacadeImpl()
    val om = ObjectMapper().registerModule(
        KotlinModule.Builder()
            .withReflectionCacheSize(512)
            .configure(KotlinFeature.NullToEmptyCollection, false)
            .configure(KotlinFeature.NullToEmptyMap, false)
            .configure(KotlinFeature.NullIsSameAsDefault, false)
            .configure(KotlinFeature.SingletonSupport, false)
            .configure(KotlinFeature.StrictNullChecks, false)
            .build()
    )

    routing {
        get("/") {
            call.respondRedirect("static/index.html")
        }

        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }

        post("p") { call.respondRedirect("static/p") }

        post("/static/p") {
            try {
                val form = parsingParameters(call.receive<String>())
                val name = URLDecoder.decode(form.item("name"), StandardCharsets.UTF_8.toString())
                dao.addNewQuestionnaire(
                    name,
                    form.item("age").toInt(),
                    form.item("gender").toInt(),
                    form.item("education").toInt(),
                    form.item("q1").toInt(),
                    form.item("q2").toInt(),
                    form["q3"]!!,
                    form.item("q4").toInt(),
                    form.item("q5").toInt(),
                    form.item("q6").toInt(),
                    form.item("q7").toInt(),
                    form.item("q8").toInt(),
                    form.item("q9").toInt(),
                    form.item("q10").toInt(),
                )

                if (form["q3"]?.contains("genshin") == true) {
                    call.respondRedirect("/static/genshin.html")
                } else {
                    call.respondRedirect("/static/success.html")
                }

            } catch (e: Throwable) {
                call.respondRedirect("/static/fail.html")
            }
        }

        get("/api/q/{id?}") {
            call.parameters["id"]?.toIntOrNull()?.let { id ->
                dao.questionnaire(id)
                    ?.let { q -> call.respond(q) }
                    ?: call.respond(HttpStatusCode.NotFound, "id not found.")
            } ?: call.respond(HttpStatusCode.BadRequest, "id params is require.")
        }

        get("/api/q/all") {
            call.respond(dao.allQuestionnaires())
        }

        get("/api/q1") {
            call.respond(dao.playData())
        }

        get("/api/q2") { call.respond(dao.gamingFrequency()) }
        get("/api/q3") { call.respond(dao.gameTypeCount()) }
        get("/api/q4") { call.respond(dao.gamingPurpose()) }
        get("/api/q5") { call.respond(dao.opinionOnGameCharging()) }
        get("/api/q6") { call.respond(dao.opinionOnGamePiracy()) }
        get("/api/q7") { call.respond(dao.opinionOnGameLimit()) }
        get("/api/q8") { call.respond(dao.opinionOnGamingEffectLife()) }
        get("/api/q9") { call.respond(dao.opinionOnGradesDeclineReason()) }
        get("/api/q10") { call.respond(dao.opinionOnGamingTime()) }

        get("/api/qall") {

            val data = mapOf(
                "q1" to dao.playData(),
                "q2" to dao.gamingFrequency(),
                "q3" to dao.gameTypeCount(),
                "q4" to dao.gamingPurpose(),
                "q5" to dao.opinionOnGameCharging(),
                "q6" to dao.opinionOnGamePiracy(),
                "q7" to dao.opinionOnGameLimit(),
                "q8" to dao.opinionOnGamingEffectLife(),
                "q9" to dao.opinionOnGradesDeclineReason(),
                "q10" to dao.opinionOnGamingTime()
            )
            call.respondText(om.writeValueAsString(data), ContentType.Application.Json, HttpStatusCode.OK)
        }
    }

}

private fun Map<String, List<String>>.item(key: String): String {
    return this[key]!!.first()
}

private fun parsingParameters(p: String): MutableMap<String, MutableList<String>> {
    return mutableMapOf<String, MutableList<String>>().apply {
        p.split("&").forEach { pair ->
            val key = pair.split("=")[0]
            val value = pair.split("=")[1]
            this[key]?.add(value) ?: run { this[key] = mutableListOf(value) }
        }
    }
}
