val projectJvmTarget = "1.8"
val kotlinVersion: String by project
val ktorVersion: String by project
val logback: String by project

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.71"
    id("io.gitlab.arturbosch.detekt") version "1.7.4" // 코드 분석용용
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
    apply(plugin = "org.jmailen.kotlinter") // 코드 포맷
    apply(plugin = "io.gitlab.arturbosch.detekt")

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    }

    kotlinter {
        ignoreFailures = false
        indentSize = 4
        reporters = arrayOf("checkstyle", "plain")
        experimentalRules = false
        disabledRules = emptyArray()
        fileBatchSize = 30
    }

    tasks {
        compileKotlin {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = projectJvmTarget
            }
            dependsOn(processResources)
        }


        compileTestKotlin {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = projectJvmTarget
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
