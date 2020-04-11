package me.dgahn.convert

import com.google.protobuf.util.JsonFormat
import me.dgahn.person.Person
import me.dgahn.person.PersonProto as PersonProto

fun Person.toProto(): PersonProto.Person = PersonProto.Person.newBuilder()
    .setId(this.id)
    .setEmail(this.email)
    .setName(this.name)
    .addAllPhones(this.phones.map { it.toProto() })
    .build()

fun PersonProto.Person.buildJson(): String = JsonFormat.printer().print(this)
