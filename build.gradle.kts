import com.google.protobuf.gradle.*

plugins {
    id("org.screamingsandals.plugin-builder") version "1.0.76"
    id("com.google.protobuf") version "0.8.18"

    kotlin("jvm") version "1.6.20-RC"
}

group = "net.hoz"

apply {
    plugin("java-library")
    plugin("idea")
}

object DependencyVersions {
    const val SLIB = "2.0.1-SNAPSHOT"
    const val SLF4J = "1.7.32"
    const val REACTOR = "3.4.12"
    const val RSOCKET_RPC = "0.3.0"
    const val PROTOBUF = "3.19.1"
    const val RESULTER = "1.1.7"
}

repositories {
    mavenCentral()
    maven("https://repo.screamingsandals.org/public")
}

dependencies {
    api("org.slf4j", "slf4j-api", DependencyVersions.SLF4J)

    api("io.projectreactor", "reactor-core", DependencyVersions.REACTOR)

    api("io.rsocket.rpc", "rsocket-rpc-core", DependencyVersions.RSOCKET_RPC)
    api("io.rsocket.rpc", "rsocket-rpc-protobuf", DependencyVersions.RSOCKET_RPC)

    api("com.google.protobuf", "protobuf-java", DependencyVersions.PROTOBUF)

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
        id("rsocketRpc") {
            artifact = "io.rsocket.rpc:rsocket-rpc-protobuf:${DependencyVersions.RSOCKET_RPC}"
        }
    }

    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("rsocketRpc")
            }
        }
    }

    generatedFilesBaseDir = "${project.buildDir}/generatedProto"
}