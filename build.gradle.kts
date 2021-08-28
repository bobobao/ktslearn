import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    application
    kotlin("jvm") version "1.5.30"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    maven(url = "https://maven.aliyun.com/repository/central")
    maven(url = "https://maven.aliyun.com/repository/jcenter")
    mavenCentral()
}

dependencies {
    implementation("io.netty:netty-all:4.1.67.Final")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-script-util")
    implementation("org.jetbrains.kotlin:kotlin-script-runtime")
    implementation("org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable")
//    implementation("org.jetbrains.kotlin:kotlin-main-kts")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

application {
    mainClass.set("com.bao.learn.groovy.MainKt")
}

val mainClass = "com.bao.learn.groovy.MainKt"

tasks.register<Jar>("fatJar") {
    archiveClassifier.set("all")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Main-Class"] = mainClass
    }
    from(configurations.runtimeClasspath.get()
        .onEach { println("add from dependencies: ${it.name}") }
        .map { if (it.isDirectory) it else zipTree(it) })
    val sourcesMain = sourceSets.main.get()
    sourcesMain.allSource.forEach { println("add from sources: ${it.name}") }
    from(sourcesMain.output)
}
