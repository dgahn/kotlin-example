plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.71"
    id("io.gitlab.arturbosch.detekt") version "1.7.4"
    id("org.jmailen.kotlinter") version "2.3.2"
}

allprojects {
    group = "me.dgahn"
    version = "0.1.0"
    repositories {
        mavenCentral()
        jcenter()
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "org.jmailen.kotlinter")

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    }

    kotlinter {
        ignoreFailures = false
        indentSize = 4
        reporters = arrayOf("checkstyle", "plain")
        experimentalRules = false
        disabledRules = emptyArray<String>()
        fileBatchSize = 30
    }

    tasks {
        compileKotlin {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "1.8"
            }
            dependsOn(processResources) // kotlin 에서 ConfigurationProperties
        }


        compileTestKotlin {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "1.8"
            }
        }
    }
}

project(":ktor-example") {
    dependencies {
        implementation("io.ktor:ktor-server-netty:1.3.1")
        implementation("ch.qos.logback:logback-classic:1.2.3")
        testImplementation("io.ktor:ktor-server-tests:1.3.1")
    }
}
