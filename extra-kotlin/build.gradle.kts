val exposedVersion = "0.38.2"

plugins.apply("org.jetbrains.kotlin.jvm")
plugins.apply("org.jetbrains.kotlin.plugin.serialization")

dependencies {
    api(project(":common"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")
    api("org.jetbrains.exposed:exposed-core:$exposedVersion")
    api("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    api("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
}