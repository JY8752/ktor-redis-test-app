pluginManagement {
    repositories {
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
}
rootProject.name = "ktor-redis-test-app"

include("testadmin")
include("testbatch")
include("testcommon")
