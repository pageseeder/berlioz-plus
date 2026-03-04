plugins {
  id("java-library")
  id("maven-publish")
  id("io.codearte.nexus-staging") version "0.30.0"
}

group = "org.pageseeder.berlioz"
version = file("version.txt").readText().trim()

// Groovy used "$title" (a project property). In Kotlin DSL, read it explicitly:
description = findProperty("title")?.toString() ?: ""

apply(from = "gradle/publish-mavencentral.gradle")

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(11))
  }
  withJavadocJar()
  withSourcesJar()
}

tasks.withType<JavaCompile>().configureEach {
  options.encoding = "UTF-8"
}

repositories {
  maven {
    url = uri("https://maven-central.storage.googleapis.com/maven2")
  }
}

dependencies {
  api(libs.jspecify)
  api(libs.berlioz)
  api(libs.xmlwriter)
  api(libs.slf4j.api)

  compileOnly(libs.servlet.api) {
    because("This is provided by the Servlet container")
  }

  compileOnly(libs.json.api) {
    because("These is an optional dependencies for JSON output using implementations of JSR 374")
  }

  testImplementation(platform(libs.junit.bom))
  testImplementation(libs.bundles.junit)
  testRuntimeOnly(libs.junit.jupiter.engine)
  testRuntimeOnly(libs.slf4j.simple)

}

tasks.wrapper {
  gradleVersion = "8.13"
  distributionType = Wrapper.DistributionType.BIN
}

tasks.withType<Javadoc> {
  options {
    encoding = "UTF-8"
  }
}
