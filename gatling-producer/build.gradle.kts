plugins {
  kotlin("jvm") version "1.9.24"
  id("io.gatling.gradle") version "3.11.5"
}

repositories {
  mavenCentral()
}

kotlin {
  jvmToolchain(17)
}

dependencies {
  // Gatling Java DSL
  gatling("io.gatling:gatling-core-java:3.11.5")
  gatling("io.gatling:gatling-http-java:3.11.5")

  // Kafka + JSON для simulation
  gatlingImplementation("org.apache.kafka:kafka-clients:3.7.0")
  gatlingImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.1")

}



sourceSets {
  named("gatling") {
    kotlin.srcDir("src/gatling/kotlin")
  }
}


