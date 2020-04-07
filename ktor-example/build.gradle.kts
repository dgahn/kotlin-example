plugins {
    application
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

application {
    mainClassName = "me.dgahn.WebEngineKt"
}

