import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    id("ru.vyarus.quality") version "6.0.1"
    id("java-library")
    id("jacoco")
    id("maven-publish")
}

group = "com.mbi"
version = "1.0"

val suitesDir = "src/test/resources/suites"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("org.testng:testng:7.11.0")
    implementation("joda-time:joda-time:2.14.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.21.0")
}

tasks.test {
    useTestNG {
        // Automatically include all XML test suite files from suitesDir
        fileTree(suitesDir).matching { include("*.xml") }.files.forEach { suites(it) }
    }

    testLogging {
        events("passed", "skipped", "failed")
        exceptionFormat = TestExceptionFormat.FULL
        showStandardStreams = true
    }
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/coverage"))
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.withType<Javadoc> {
    (options as StandardJavadocDocletOptions).addBooleanOption("Xdoclint:none", true)
}

quality {
    // Enable all supported static analysis tools
    checkstyle = true
    pmd = true
    codenarc = true
    spotbugs = true
}

tasks.check {
    dependsOn(tasks.jacocoTestReport)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "date-handler"
            from(components["java"])
        }
    }
}
