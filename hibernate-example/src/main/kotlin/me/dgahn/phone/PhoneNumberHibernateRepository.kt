package me.dgahn.phone

import javax.persistence.Persistence
import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

class PhoneNumberHibernateRepository : PhoneNumberRepository {

    private val emf = Persistence.createEntityManagerFactory("person")

    override fun findOne(id: Long): Phone? {
        val em = emf.createEntityManager()
        val tx = em.transaction
        tx.begin()
        var phone: Phone? = null
        try {
            phone = em.createQuery(
                "SELECT ph FROM ${Phone::class.java.simpleName} ph WHERE ph.id = :id",
                Phone::class.java
            )
                .setParameter("id", id)
                .singleResult
            tx.commit()
        } catch (e: Exception) {
            tx.rollback()
        } finally {
            em.close()
        }

        return phone
    }
}
