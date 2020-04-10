package me.dgahn.phone

import me.dgahn.person.Person
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class PhoneNumber(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long,
    val number: String,
    @Enumerated(EnumType.STRING)
    val type: PhoneType,
    @ManyToOne
    val person: Person
)