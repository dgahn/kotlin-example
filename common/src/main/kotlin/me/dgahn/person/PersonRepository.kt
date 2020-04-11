package me.dgahn.person

interface PersonRepository {
    fun findOne(id: Long): Person?
}
