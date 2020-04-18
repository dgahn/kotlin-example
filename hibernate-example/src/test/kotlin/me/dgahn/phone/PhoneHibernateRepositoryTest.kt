package me.dgahn.phone

import me.dgahn.config.HibernateManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
class PhoneHibernateRepositoryTest {
    companion object {
        @JvmStatic
        @Container
        val sqlContainer = PostgreSQLContainer<Nothing>().apply {
            withDatabaseName("testdb")
            withUsername("scott")
            withPassword("tiger")
        }
    }

    @Test
    fun `id가 1인 Phone을 조회할 수 있다`() {
        val id = 1L
        val phoneNumberRepo = PhoneNumberHibernateRepository(HibernateManager)
        val phone = phoneNumberRepo.findOne(id)

        assertThat(phone).isNotNull
    }
}
