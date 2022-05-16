import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    id("ru.vyarus.quality").version("4.7.0")
    id("java-library")
    id("jacoco")
    id("maven-publish")
}

val suitesDir = "src/test/resources/suites/"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("org.testng:testng:7.5")
    implementation("joda-time:joda-time:2.10.14")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")
}

tasks.test {
    useTestNG {
        // Add test suites
        File(projectDir.absolutePath + "/" + suitesDir)
                .walk()
                .forEach {
                    if (it.isFile) {
                        suites(it)
                    }
                }

        testLogging {
            events("passed", "skipped", "failed")
            exceptionFormat = TestExceptionFormat.FULL
            showStandardStreams = true
        }
    }
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("${buildDir}/reports/coverage").get().asFile)
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

quality {
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
            groupId = "com.mbi"
            artifactId = "date-handler"
            version = "1.0"

            from(components["java"])
        }
    }
}
