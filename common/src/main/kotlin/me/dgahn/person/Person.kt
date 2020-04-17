package me.dgahn.person

import com.google.protobuf.util.JsonFormat
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import me.dgahn.phone.Phone
import me.dgahn.phone.toProto

@Entity
data class Person(
    @Id
    @GeneratedValue
    val id: Long,
    val name: String,
    val email: String,
    @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
    val phones: List<Phone>
)

fun Person.toProto(): PersonProto.Person = PersonProto.Person.newBuilder()
    .setId(this.id)
    .setEmail(this.email)
    .setName(this.name)
    .addAllPhones(this.phones.map { it.toProto() })
    .build()

fun PersonProto.Person.buildJson(): String = JsonFormat.printer().print(this)
