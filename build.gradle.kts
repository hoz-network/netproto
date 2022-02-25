import com.google.protobuf.gradle.*

plugins {
    `java-library`
    idea
    id("org.screamingsandals.plugin-builder") version "1.0.76"
    id("com.google.protobuf") version "0.8.18"
}

group = "net.hoz"
version = "1.0.0-SNAPSHOT"

object DependencyVersions {
    const val SLIB = "2.0.1-SNAPSHOT"
    const val SLF4J = "1.7.32"
    const val REACTOR = "3.4.12"
    const val RSOCKET_RPC = "0.3.0"
    const val PROTOBUF = "3.19.1"
    const val RESULTER = "1.1.4"
}

repositories {
    mavenCentral()
    maven("https://repo.screamingsandals.org/public")
}

dependencies {
    api(group = "org.slf4j", name = "slf4j-api", version = DependencyVersions.SLF4J)

    api(group = "io.projectreactor", name = "reactor-core", version = DependencyVersions.REACTOR)

    api(group = "io.rsocket.rpc", name = "rsocket-rpc-core", version = DependencyVersions.RSOCKET_RPC)
    api(group = "io.rsocket.rpc", name = "rsocket-rpc-protobuf", version = DependencyVersions.RSOCKET_RPC)

    api(group = "com.google.protobuf", name = "protobuf-java", version = DependencyVersions.PROTOBUF)

    api(group = "org.screamingsandals.lib", name = "core-common", version = DependencyVersions.SLIB)

    api(group = "com.iamceph.resulter", name = "resulter-core", version = DependencyVersions.RESULTER)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
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