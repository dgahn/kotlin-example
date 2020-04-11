package me.dgahn.person

import javax.persistence.Persistence

class PersonHibernateRepository : PersonRepository {

    private val emf = Persistence.createEntityManagerFactory("person")

    override fun findOne(id: Long): Person? {
        val em = emf.createEntityManager()
        val tx = em.transaction
        tx.begin()
        var person: Person? = null
        try {
            person = em.createQuery(
                "SELECT p FROM ${Person::class.java.simpleName} p WHERE p.id = :id",
                Person::class.java
            )
                .setParameter("id", id)
                .singleResult
            tx.commit()
        } catch (e: Exception) {
            tx.rollback()
        } finally {
            em.close()
        }

        return person
    }
}
