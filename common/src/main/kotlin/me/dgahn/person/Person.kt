package me.dgahn.person

import me.dgahn.phone.PhoneNumber
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
data class Person(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long,
    val name: String,
    val email: String,
    @OneToMany(mappedBy = "person")
    val phoneNumbers: List<PhoneNumber>
)
