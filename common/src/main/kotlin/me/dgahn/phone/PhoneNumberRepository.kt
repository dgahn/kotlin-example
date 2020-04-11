package me.dgahn.phone

interface PhoneNumberRepository {
    fun findOne(id: Long): Phone?
}
