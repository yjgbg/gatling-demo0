import com.google.protobuf.gradle.builtins
import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
    id("io.gatling.gradle") version "3.7.6"
    id("com.google.protobuf") version "0.8.18"
}

group = "com.github.yjgbg"
version = "1.0-SNAPSHOT"

dependencies {
    gatling("com.google.protobuf:protobuf-javalite:3.10.0")
    implementation("com.google.protobuf:protobuf-javalite:3.10.0")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.10.0"
    }
    generateProtoTasks {
        all().forEach {
            it.builtins {
                this["java"].option("lite")
            }
        }
    }
}