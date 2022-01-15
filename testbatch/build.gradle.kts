import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
}

group = "me.user"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation("redis.clients:jedis:4.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-native-mt")
    implementation("com.google.code.gson:gson:2.8.9")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.0.3")

}
