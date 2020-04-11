package me.dgahn.convert

import me.dgahn.person.PersonProto
import me.dgahn.phone.Phone
import me.dgahn.phone.PhoneType

fun Phone.toProto() = PersonProto.Person.PhoneNumber.newBuilder()
    .setNumber(this.number)
    .setType(this.type.toProto())
    .build()

fun PhoneType.toProto() = when (this) {
    PhoneType.HOME -> PersonProto.Person.PhoneType.HOME
    PhoneType.MOBILE -> PersonProto.Person.PhoneType.MOBILE
    PhoneType.WORK -> PersonProto.Person.PhoneType.WORK
}
