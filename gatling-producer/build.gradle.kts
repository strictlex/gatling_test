plugins {
  kotlin("jvm") version "1.9.24"
  id("io.gatling.gradle") version "3.11.5"
}

repositories {
  mavenCentral()
  maven {
    url = uri("https://packages.confluent.io/maven/")
  }

}
kotlin {
  jvmToolchain(17)
}

dependencies {

  gatling("io.gatling:gatling-core-java:3.11.5")
  gatling("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.1")
  gatling("org.galaxio:gatling-kafka-plugin_2.13:0.15.1")
  gatling("io.github.penolegrus:bank-faker:1.0.0")
  
}



sourceSets {
  named("gatling") {
    kotlin.srcDir("src/gatling/kotlin")
  }
}