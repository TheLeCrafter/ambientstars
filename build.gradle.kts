import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("xyz.jpenilla.run-paper") version "2.1.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    kotlin("jvm") version "1.9.0"
}

group = "dev.thelecrafter.plugins.ambientstars"
version = "1.1.0-1.19"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
    implementation("io.papermc:paperlib:1.0.8")
    implementation("org.kohsuke:github-api:1.315")
}

tasks.shadowJar {
    minimize()
    relocate("org.jetbrains", "$group.org.jetbrains")
    relocate("io.papermc.lib", "$group.io.papermc.lib")
    relocate("org.kohsuke", "$group.org.kohsuke")
    archiveFileName.set("${project.name}-${project.version}.jar")
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "17"
    javaParameters = true
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "17"
    javaParameters = true
}

tasks.build {
    dependsOn.add("shadowJar")
}

tasks.jar {
    enabled = false
}

tasks.runServer {
    minecraftVersion("1.19")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}
