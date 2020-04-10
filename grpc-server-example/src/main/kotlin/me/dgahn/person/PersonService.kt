package me.dgahn.person

import io.grpc.stub.StreamObserver

class PersonService: PersonServiceGrpc.PersonServiceImplBase() {

    override fun getPerson1(
        request: PersonProto.Person,
        responseObserver: StreamObserver<PersonProto.Person>
    ) {
        super.getPerson1(request, responseObserver)
    }

    override fun getPerson2(responseObserver: StreamObserver<PersonProto.Person>?): StreamObserver<PersonProto.Person> {
        return super.getPerson2(responseObserver)
    }

    override fun getPerson3(
        request: PersonProto.Person?,
        responseObserver: StreamObserver<PersonProto.Person>?
    ) {
        super.getPerson3(request, responseObserver)
    }

    override fun getPerson4(responseObserver: StreamObserver<PersonProto.Person>?): StreamObserver<PersonProto.Person> {
        return super.getPerson4(responseObserver)
    }
}