package me.dgahn.phone

import me.dgahn.config.HibernateManager
import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

class PhoneNumberHibernateRepository(val hm: HibernateManager) : PhoneNumberRepository {

    override fun findOne(id: Long): Phone? {
        val session = hm.sessions.openSession()
        val tx = session.transaction
        tx.begin()
        var phone: Phone? = null
        try {
            phone = session.createQuery(
                "SELECT ph FROM ${Phone::class.java.simpleName} ph WHERE ph.id = :id",
                Phone::class.java
            )
                .setParameter("id", id)
                .singleResult
            tx.commit()
        } catch (e: Exception) {
            tx.rollback()
        } finally {
            session.close()
        }

        return phone
    }
}
