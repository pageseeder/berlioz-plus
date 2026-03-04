plugins {
  id("java-library")
  id("maven-publish")
  jacoco
  alias(libs.plugins.jreleaser)
  alias(libs.plugins.sonar)
}

val title: String by project
val gitName: String by project
val website: String by project

group = "org.pageseeder.berlioz"
version = file("version.txt").readText().trim()

// Groovy used "$title" (a project property). In Kotlin DSL, read it explicitly:
description = findProperty("title")?.toString() ?: ""

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
  mavenCentral()
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
  testImplementation(libs.servlet.api)
  testRuntimeOnly(libs.junit.jupiter.engine)
  testRuntimeOnly(libs.slf4j.simple)
}

sonar {
  properties {
    property("sonar.projectKey", "pageseeder_berlioz-plus")
    property("sonar.organization", "pageseeder")
    // Tell SonarCloud where the JaCoCo XML report is
    property(
      "sonar.coverage.jacoco.xmlReportPaths",
      layout.buildDirectory.file("reports/jacoco/test/jacocoTestReport.xml").get().asFile.absolutePath
    )
  }
}

tasks.wrapper {
  gradleVersion = "8.13"
  distributionType = Wrapper.DistributionType.BIN
}

tasks.test {
  useJUnitPlatform()
  // make sure report generation happens after tests when requested
  finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
  dependsOn(tasks.test)
  reports {
    xml.required.set(true)   // Sonar reads this
    html.required.set(true)  // nice to have for CI artifacts/debugging
    csv.required.set(false)
  }
}

tasks.withType<Javadoc> {
  options {
    encoding = "UTF-8"
  }
}


publishing {
  publications {
    create<MavenPublication>("maven") {
      from(components["java"])
      pom {
        name.set(title)
        description.set(project.description)
        url.set(website)
        licenses {
          license {
            name.set("The Apache Software License, Version 2.0")
            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
          }
        }
        organization {
          name.set("Allette Systems")
          url.set("https://www.allette.com.au")
        }
        scm {
          url.set("git@github.com:pageseeder/${gitName}.git")
          connection.set("scm:git:git@github.com:pageseeder/${gitName}.git")
          developerConnection.set("scm:git:git@github.com:pageseeder/${gitName}.git")
        }
        developers {
          developer {
            name.set("Christophe Lauret")
            email.set("clauret@weborganic.com")
          }
        }
      }
    }
  }
  repositories {
    maven {
      url = layout.buildDirectory.dir("staging-deploy").get().asFile.toURI()
    }
  }
}

jreleaser {
  configFile.set(file("jreleaser.toml"))
}
