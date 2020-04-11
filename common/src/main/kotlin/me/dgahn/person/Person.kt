package me.dgahn.person

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import me.dgahn.phone.Phone

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
