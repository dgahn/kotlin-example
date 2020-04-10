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
    id("io.gitlab.arturbosch.detekt") version "1.7.4" // 코드 분석용용
    id("org.jmailen.kotlinter") version "2.3.2"
    id("com.google.protobuf") version "0.8.8"
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

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("ch.qos.logback:logback-classic:1.2.3")
        implementation("io.github.microutils:kotlin-logging:1.7.9")
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

project(":grpc-server-example") {
    dependencies {
        implementation(project(":protobuf-example"))

        implementation("io.grpc:grpc-netty-shaded:1.28.1")
        implementation("io.grpc:grpc-stub:1.28.1")
    }
}

project(":ktor-example") {
    dependencies {
        implementation(project(":protobuf-example"))

        implementation("io.ktor:ktor-server-netty:1.3.1")
        implementation("ch.qos.logback:logback-classic:1.2.3")

        implementation("com.google.protobuf:protobuf-java-util:3.11.4")

        testImplementation("io.ktor:ktor-server-tests:1.3.1")
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
