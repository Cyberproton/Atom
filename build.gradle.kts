import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

group = "project.cyberproton"
version = "1.0"
val projectGroup = group.toString()
val projectVersion = version.toString()
val artifactName = "atom"
val exposedVersion = "0.37.3"

plugins {
    id("java-library")
    kotlin("jvm") version "1.7.0"
    kotlin("plugin.serialization") version "1.6.0"
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

subprojects {
    plugins.apply("java-library")
    plugins.apply("com.github.johnrengelman.shadow")
    plugins.apply("maven-publish")
    plugins.apply("org.jetbrains.kotlin.jvm")
    plugins.apply("org.jetbrains.kotlin.plugin.serialization")

    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://repo.lucko.me/")
        maven("https://repo.alessiodp.com/releases/")
        maven("https://repo.aikar.co/content/groups/aikar/")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        withSourcesJar()
        withJavadocJar()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            javaParameters = true
        }
    }

    publishing {
        publications {
            val publish = create<MavenPublication>("maven") {
                groupId = projectGroup
                artifactId = "$artifactName-${this@subprojects.name}"
                version = projectVersion
            }
            project.shadow.component(publish)
        }
    }

    tasks.withType<ShadowJar> {
        archiveBaseName.set("$artifactName-${this@subprojects.name}")
        archiveClassifier.set("")
        exclude("**/*.kotlin_metadata")
        relocate("net.kyori", "project.cyberproton.libs.net.kyori")
    }
}