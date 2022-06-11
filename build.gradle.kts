import com.google.protobuf.gradle.*
import org.cadixdev.gradle.licenser.LicenseProperties

plugins {
    id("org.screamingsandals.plugin-builder") version "1.0.77"
    id("com.google.protobuf") version "0.8.18"

    kotlin("jvm") version "1.7.0"
    id("nebula.release") version "16.0.0"
}

group = "net.hoz"

apply {
    plugin("java-library")
    plugin("idea")
}

object DependencyVersions {
    const val SLIB = "2.0.2-SNAPSHOT"
    const val SLF4J = "1.7.32"
    const val PROTOBUF = "3.21.1"
    const val GRPC = "1.47.0"
    const val GRPC_KOTLIN = "1.3.0"
    const val RESULTER = "1.1.8"
    const val COROUTINES = "1.6.2"
}

repositories {
    mavenCentral()
    maven("https://repo.screamingsandals.org/public")
}

dependencies {
    api("org.slf4j", "slf4j-api", DependencyVersions.SLF4J)

    api("com.google.protobuf", "protobuf-kotlin", DependencyVersions.PROTOBUF)
    api("io.grpc", "grpc-kotlin-stub", DependencyVersions.GRPC_KOTLIN)
    api("io.grpc", "grpc-stub", DependencyVersions.GRPC)
    api("io.grpc", "grpc-protobuf", DependencyVersions.GRPC)

    api("org.jetbrains.kotlinx", "kotlinx-coroutines-core", DependencyVersions.COROUTINES)

    api("org.screamingsandals.lib", "core-common", DependencyVersions.SLIB)

    api("com.iamceph.resulter", "resulter-core", DependencyVersions.RESULTER)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

sourceSets.forEach {
    it.java.srcDirs(
        "${project.buildDir}/generatedProto/${it.name}/java",
        "${project.buildDir}/generatedProto/${it.name}/grpc"
    )
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${DependencyVersions.PROTOBUF}"
    }

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${DependencyVersions.GRPC}"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${DependencyVersions.GRPC_KOTLIN}:jdk8@jar"
        }
    }

    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                id("kotlin")
            }
        }
    }

    generatedFilesBaseDir = "${project.buildDir}/generatedProto"
}

afterEvaluate {
    license {
        include("**/*.proto ")
        exclude("**/generatedProto")
    }
}