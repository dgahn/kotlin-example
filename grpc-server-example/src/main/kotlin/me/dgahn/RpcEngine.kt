package me.dgahn

import io.grpc.ServerBuilder
import me.dgahn.person.PersonProto
import me.dgahn.person.PersonService
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

fun main() {
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