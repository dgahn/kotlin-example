package me.dgahn

import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.http.ContentType
import io.ktor.request.accept
import io.ktor.response.respondBytes
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.pipeline.PipelineContext
import me.dgahn.convert.buildJson
import me.dgahn.convert.toProto
import me.dgahn.person.PersonHibernateRepository
import me.dgahn.person.PersonRepository
import me.dgahn.phone.PhoneNumberHibernateRepository
import me.dgahn.phone.PhoneNumberRepository
import mu.KotlinLogging
import org.koin.dsl.module
import org.koin.experimental.builder.singleBy
import org.koin.java.KoinJavaComponent.inject
import org.koin.ktor.ext.Koin

private val logger = KotlinLogging.logger { }

fun main() {
    embeddedServer(factory = Netty, port = 8080, module = Application::module).start(wait = true)
}

fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    install(Koin) {
        modules(hibernateModule)
    }
    routing {
        get("/persons/{id}") {
            getPerson()
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.getPerson() {
    val id = call.parameters["id"]!!.toLong()
    val personRepo by inject(PersonRepository::class.java)
    val person = personRepo.findOne(id)
    val personProto = person!!.toProto()
    when(call.request.accept()) {
        "application/protobuf" -> call.respondBytes(personProto.toByteArray())
        else -> call.respondText(personProto.buildJson(), ContentType.Application.Json)
    }

}

val hibernateModule = module(createdAtStart = true) {
    singleBy<PersonRepository, PersonHibernateRepository>()
    singleBy<PhoneNumberRepository, PhoneNumberHibernateRepository>()
}
