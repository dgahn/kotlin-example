package me.dgahn.phone

import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import me.dgahn.person.Person
import me.dgahn.person.PersonProto

@Entity
data class Phone(
    @Id
    @GeneratedValue
    val id: Long,
    val number: String,
    @Enumerated(EnumType.STRING)
    val type: PhoneType,
    @ManyToOne(fetch = FetchType.EAGER)
    val person: Person
)

fun Phone.toProto(): PersonProto.Person.PhoneNumber = PersonProto.Person.PhoneNumber.newBuilder()
    .setNumber(this.number)
    .setType(this.type.toProto())
    .build()

fun PhoneType.toProto() = when (this) {
    PhoneType.HOME -> PersonProto.Person.PhoneType.HOME
    PhoneType.MOBILE -> PersonProto.Person.PhoneType.MOBILE
    PhoneType.WORK -> PersonProto.Person.PhoneType.WORK
}
