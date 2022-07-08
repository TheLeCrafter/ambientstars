import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
    kotlin("jvm") version "1.6.21"
}

group = "dev.thelecrafter.plugins.ambientstars"
version = "1.1.0"

repositories {
    mavenCentral()
    maven("https://libraries.minecraft.net")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
    compileOnly("com.mojang:authlib:1.5.25")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.21")
    implementation("io.papermc:paperlib:1.0.7")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.10")
    implementation("io.github.classgraph:classgraph:4.8.146")
    implementation("org.kohsuke:github-api:1.306")
}

tasks.shadowJar {
    minimize()
    relocate("org.jetbrains", "$group.org.jetbrains")
    relocate("org.reflections", "$group.org.reflections")
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

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}