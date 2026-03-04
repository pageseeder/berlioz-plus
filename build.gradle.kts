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
  api("org.pageseeder.berlioz:pso-berlioz:0.11.0")
  api("org.pageseeder.xmlwriter:pso-xmlwriter:1.0.2")

  implementation("org.slf4j:slf4j-api:1.7.12")

  compileOnly("javax.servlet:javax.servlet-api:3.1.0") {
    because("This is provided by the Servlet container")
  }

  compileOnly("javax.json:javax.json-api:1.0") {
    because("These is an optional dependencies for JSON output using implementations of JSR 374")
  }

  compileOnly("org.eclipse.jdt:org.eclipse.jdt.annotation:2.0.0") {
    because("This used for Null safety and better interop with Kotlin")
  }

  testImplementation("junit:junit:4.12")
  testImplementation("org.slf4j:slf4j-simple:1.7.12")
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
