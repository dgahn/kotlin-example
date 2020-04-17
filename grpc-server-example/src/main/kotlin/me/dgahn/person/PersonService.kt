package me.dgahn.person

import io.grpc.stub.StreamObserver
import org.koin.java.KoinJavaComponent.inject

class PersonService : PersonServiceGrpc.PersonServiceImplBase() {

    private val personRepo: PersonRepository by inject(PersonHibernateRepository::class.java)

    override fun getPersonOne(request: PersonProto.Id, responseObserver: StreamObserver<PersonProto.Person>) {
        val person = personRepo.findOne(request.personId)
        responseObserver.onNext(person!!.toProto())
        responseObserver.onCompleted()
    }

}
