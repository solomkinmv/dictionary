plugins {
    java
    jacoco
    id("org.springframework.boot") version "3.0.2"
    id("io.spring.dependency-management") version "1.1.0"
    /*
    temporary disabled due to absence of Mockito support and strange behavior of native build (Spring Security not working as with JVM)
    https://github.com/spring-projects/spring-boot/issues/32195
    id("org.graalvm.buildtools.native") version "0.9.19"
     */
    id("pl.allegro.tech.build.axion-release") version "1.14.3"
    id("io.freefair.lombok") version "6.6.1"
}

scmVersion {
    repository {
        directory.set(project.rootProject.file("../"))
    }
}

group = "in.solomk"
project.version = scmVersion.version
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // spring
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // tools
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.mapstruct:mapstruct:${property("mapstructVersion")}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${property("mapstructVersion")}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mongodb")
    implementation("pl.rzrz:assertj-reactor:${property("assertjReactorVersion")}")
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report

    reports.xml.required.set(true)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

/*
Custom configuration for native build on ARM

tasks.withType<BootBuildImage> {
    if (org.gradle.nativeplatform.platform.internal.DefaultNativePlatform.getCurrentArchitecture().isArm()) {
        builder.set("dashaun/builder-arm:tiny")
    }
}
 */
