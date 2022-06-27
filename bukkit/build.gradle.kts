repositories {
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    api(project(":common"))
    api(project(":extra-kotlin"))
    shadow("com.destroystokyo.paper:paper-api:1.15.2-R0.1-SNAPSHOT")
    shadow("org.spigotmc:spigot:1.16.4-R0.1-SNAPSHOT")
    api("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    api("net.kyori:adventure-platform-bukkit:4.1.0")
}

tasks.register<Copy>("copyJar") {
    dependsOn("build")
    from(layout.buildDirectory.dir("libs")) {
        include("atom-bukkit.jar")
    }
    into("/mnt/47AD37904A487973/Minecraft Servers/FACTION/plugins")
}