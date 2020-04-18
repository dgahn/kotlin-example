package me.dgahn.config

import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.Key
import com.natpryce.konfig.stringType
import me.dgahn.person.Person
import me.dgahn.phone.Phone
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.cfg.Configuration
import org.hibernate.service.ServiceRegistry
import java.util.*

object HibernateManager {
    private val driver = Key("database.driver", stringType)
    private val user = Key("database.username", stringType)
    private val password = Key("database.password", stringType)
    private val url = Key("database.url", stringType)
    private val dialect = Key("hibernate.dialect", stringType)

    private val config = ConfigurationProperties.fromResource("application.properties")

    private val props = Properties().apply {
        setProperty("hibernate.connection.driver_class", config[driver])
        setProperty("hibernate.connection.username", config[user])
        setProperty("hibernate.connection.password", config[password])
        setProperty("hibernate.connection.url", config[url])
        setProperty("hibernate.dialect", config[dialect])
        setProperty("hibernate.show_sql", "true")
        setProperty("hibernate.format_sql", "true")
        setProperty("hibernate.use_sql_comments", "true")
        setProperty("hibernate.id.new_generator_mappings", "true")
        setProperty("hibernate.jdbc.batch_size", "10")
        setProperty("hibernate.hbm2ddl.auto", "create-drop")
        setProperty("hibernate.hbm2ddl.import_files", "/data.sql")
    }

    private val cfg = Configuration().apply {
        addProperties(props)
        addPackage("me.dgahn")
        addAnnotatedClass(Person::class.java)
        addAnnotatedClass(Phone::class.java)
    }

    private val serviceRegistry: ServiceRegistry = StandardServiceRegistryBuilder()
        .applySettings(cfg.properties)
        .build()

    val sessions = cfg.buildSessionFactory(serviceRegistry)!!
}
