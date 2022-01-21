import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.5.31"
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

allprojects {
 repositories {
     mavenCentral()
 }

 apply(plugin = "kotlin")
 
 tasks.withType<KotlinCompile>() {
     kotlinOptions.jvmTarget = "11"
 }
 
 tasks.withType<Test> {
     useJUnitPlatform()
 }
 
 dependencies {
     implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-native-mt")
     testImplementation("io.kotest:kotest-runner-junit5-jvm:5.0.3")
 }
}

//admin
configure(listOf(project("testadmin"))) {
    val ktor_version: String by project
    val kotlin_version: String by project
    val logback_version: String by project

    group = "testadmin"
    version = "0.0.1"

    dependencies {
        implementation(project(":testcommon"))
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
        implementation("io.ktor:ktor-server-netty:$ktor_version")
        implementation("ch.qos.logback:logback-classic:$logback_version")
        implementation("io.ktor:ktor-server-core:$ktor_version")
        implementation("io.ktor:ktor-thymeleaf:$ktor_version")
        testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    }

    kotlin.sourceSets["main"].kotlin.srcDirs("src")
    kotlin.sourceSets["test"].kotlin.srcDirs("test")

    sourceSets["main"].resources.srcDirs("resources")
    sourceSets["test"].resources.srcDirs("testresources")
}
//batch
configure(listOf(project("testbatch"))) {
    dependencies {
        implementation(project(":testcommon"))
    }
}
//common
configure(listOf(project("testcommon"))) {
    dependencies {
      implementation("redis.clients:jedis:4.0.0")
    }
}
