package me.dgahn.person

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
class PersonHibernateRepositoryTest {

    companion object {
        @JvmStatic
        @Container
        val sqlContainer = PostgreSQLContainer<Nothing>().apply {
            withDatabaseName("testdb")
            withUsername("scott")
            withPassword("tiger")
            withExposedPorts(15432)
        }
    }

    @Test
    fun `id가 1인 Person을 조회할 수 있다`() {
        val id = 1L
        val personRepo = PersonHibernateRepository()
        val person = personRepo.findOne(id)

        assertThat(person).isNotNull
    }
}
