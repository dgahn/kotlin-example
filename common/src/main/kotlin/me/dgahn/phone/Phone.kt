package me.dgahn.phone

import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import me.dgahn.person.Person

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
