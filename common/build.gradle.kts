dependencies {
    api("org.jgrapht:jgrapht-core:1.4.0")
    api("com.google.code.findbugs:jsr305:3.0.2")
    api("org.spongepowered:configurate-yaml:4.1.2")
    api("org.spongepowered:configurate-gson:4.1.2")
    api("org.spongepowered:math:2.0.1")
    api("net.kyori:adventure-api:4.11.0")
    api("net.kyori:adventure-platform-api:4.1.0")
    api("net.kyori:adventure-text-serializer-legacy:4.11.0")
    api("com.j256.ormlite:ormlite-jdbc:6.0")
    api("com.google.inject:guice:5.1.0")
    api("org.jetbrains.xodus:xodus-openAPI:2.0.1")
    api("org.jetbrains.xodus:xodus-entity-store:2.0.1")
    api("org.jetbrains.xodus:xodus-environment:2.0.1")
    api("org.jetbrains.xodus:xodus-vfs:2.0.1")
    api("co.aikar:acf-core:0.5.1-SNAPSHOT")

    testImplementation(platform("org.junit:junit-bom:5.8.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.javaParameters = true
}