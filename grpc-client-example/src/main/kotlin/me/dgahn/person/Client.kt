package me.dgahn.person

import io.grpc.ManagedChannelBuilder
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

fun main() {
    callGrpc()
}


fun callGrpc() {
    val channel = ManagedChannelBuilder.forAddress("localhost", 8081)
        .usePlaintext()
        .build()

    val personService = PersonServiceGrpc.newBlockingStub(channel)
    val req = PersonProto.Id.newBuilder().setPersonId(1).build()
    val person = personService.getPersonOne(req)
    logger.info { "person.id : ${person.id}" }
    logger.info { "person.name : ${person.name}" }
}
