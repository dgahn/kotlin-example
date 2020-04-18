package me.dgahn

import io.grpc.ServerBuilder
import me.dgahn.person.PersonHibernateRepository
import me.dgahn.person.PersonService
import mu.KotlinLogging
import org.koin.core.context.startKoin
import org.koin.dsl.module

private val logger = KotlinLogging.logger {}

fun main() {

    startKoin {
        printLogger()
        modules(hibernateModule)
    }

    grpcServer()
}

fun grpcServer() {
    val port = 8081
    val server = ServerBuilder
        .forPort(port)
        .addService(PersonService())
        .build()

    Runtime.getRuntime().addShutdownHook(Thread {
        server.shutdown()
        server.awaitTermination()

        logger.info { "gRPC Server stopped" }
    })

    server.start()
    logger.info { "gRPC Server Start port: $port" }
    server.awaitTermination()
}

val hibernateModule = module(createdAtStart = true) {
    single { PersonHibernateRepository() }
}
