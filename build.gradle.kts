import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.ofSourceSet
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

val projectJvmTarget = "1.8"
val kotlinVersion: String by project
val ktorVersion: String by project
val logback: String by project

plugins {
    java
    idea
    id("org.jetbrains.kotlin.jvm") version "1.3.71"
    id("org.jetbrains.kotlin.plugin.noarg") version "1.3.71"
    id("io.gitlab.arturbosch.detekt") version "1.7.4" // 코드 분석용용
    id("org.jmailen.kotlinter") version "2.3.2"
    id("com.google.protobuf") version "0.8.8"
    id("org.asciidoctor.convert") version "1.5.9.2"
}

allprojects {
    group = "me.dgahn"
    version = "0.1.0"
    repositories {
        maven("https://plugins.gradle.org/m2/")
        mavenCentral()
        jcenter()
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "org.jmailen.kotlinter") // 코드 포맷
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "kotlin-jpa")

    tasks.test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("ch.qos.logback:logback-classic:1.2.3")
        implementation("io.github.microutils:kotlin-logging:1.7.9")

        testImplementation("org.assertj:assertj-core:3.15.0")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.1")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.1")

        testImplementation("org.testcontainers:junit-jupiter:1.13.0")
        testImplementation("org.testcontainers:postgresql:1.13.0")
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

project(":common") {
    dependencies {
        implementation("org.hibernate:hibernate-entitymanager:5.4.14.Final")
    }
}

project(":grpc-server-example") {
    dependencies {
        implementation(project(":protobuf-example"))

        implementation("io.grpc:grpc-netty-shaded:1.28.1")
        implementation("io.grpc:grpc-stub:1.28.1")
    }
}

project(":ktor-example") {
    apply(plugin = "org.asciidoctor.convert")
    dependencies {
        implementation(project(":common"))
        implementation(project(":protobuf-example"))
        implementation(project(":hibernate-example"))

        implementation("io.ktor:ktor-server-netty:1.3.1")
        implementation("org.koin:koin-ktor:2.1.5")
        implementation("ch.qos.logback:logback-classic:1.2.3")

        implementation("com.google.protobuf:protobuf-java-util:3.11.4")

        asciidoctor("org.springframework.restdocs:spring-restdocs-asciidoctor:2.0.4.RELEASE")
        testImplementation("org.springframework.restdocs:spring-restdocs-restassured:2.0.4.RELEASE")

        testImplementation("io.ktor:ktor-server-tests:1.3.1")
    }
}

project(":hibernate-example") {
    dependencies {
        implementation(project(":common"))
        implementation("org.hibernate:hibernate-entitymanager:5.4.14.Final")
        implementation("org.postgresql:postgresql:42.2.12")
        implementation("com.h2database:h2:1.4.200")
    }
}

project(":protobuf-example") {
    apply(plugin = "com.google.protobuf")
    dependencies {
        if (JavaVersion.current().isJava9Compatible) {
            // Workaround for @javax.annotation.Generated
            // see: https://github.com/grpc/grpc-java/issues/3633
            implementation("javax.annotation:javax.annotation-api:1.3.1")
        }

        implementation("com.google.protobuf:protobuf-java:3.11.4")
        implementation("io.grpc:grpc-protobuf:1.28.1")
        implementation("io.grpc:grpc-stub:1.28.1")
    }

    protobuf {
        generatedFilesBaseDir = "$projectDir/build/generated/source"
        protoc {
            artifact = "com.google.protobuf:protoc:3.11.4"
        }
        plugins {
            id("grpc") {
                artifact = "io.grpc:protoc-gen-grpc-java:1.28.1"
            }
        }
        generateProtoTasks {
            ofSourceSet("main").forEach {
                it.plugins {
                    id("grpc")
                }
            }
        }
    }

    sourceSets {
        create("proto") {
            proto.srcDir("src/main/proto")
        }
        main {
            java.srcDir("build/generated/source/main/java")
            java.srcDir("build/generated/source/main/grpc")
        }
    }
}
