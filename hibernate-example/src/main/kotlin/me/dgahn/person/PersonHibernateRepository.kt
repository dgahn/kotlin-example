package me.dgahn.person

import me.dgahn.config.HibernateManager

class PersonHibernateRepository(private val hm: HibernateManager) : PersonRepository {

    override fun findOne(id: Long): Person? {
        val session = hm.sessions.openSession()
        val tx = session.transaction
        tx.begin()
        var person: Person? = null
        try {
            person = session.createQuery(
                "SELECT p FROM ${Person::class.java.simpleName} p WHERE p.id = :id",
                Person::class.java
            )
                .setParameter("id", id)
                .singleResult
            tx.commit()
        } catch (e: Exception) {
            tx.rollback()
        } finally {
            session.close()
        }

        return person
    }
}
