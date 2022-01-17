import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.5.31"
}

allprojects {
 group = "me.user"
 version = "1.0-SNAPSHOT"
 
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
//configure(listOf(project("testadmin"))) {
//    dependencies {
//    }
//}
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
