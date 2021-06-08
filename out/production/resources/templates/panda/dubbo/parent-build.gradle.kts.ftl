import com.jxpanda.generator.Generator
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("idea")
    id("maven-publish")
    kotlin("jvm") version "${kotlinVersion}"
    id("com.jxpanda.generator") version "${generatorVersion}"
}

object Project {
    const val GROUP = "${group}"
    const val GROUP_ID = "$GROUP.${name}"
    const val ARTIFACT_ID_API = "${apiArtifactId}"
    const val ARTIFACT_ID_SERVICE = "${serviceArtifactId}"
    const val VERSION = "1.0.0"
}

object Maven {
    private const val DOMAIN = "${nexus}"
    const val URL_RELEASE = "$DOMAIN/repository/maven-releases/"
    const val URL_SNAPSHOTS = "$DOMAIN/repository/maven-releases/"
    const val USERNAME = "${nexusUser}"
    const val PASSWORD = "${nexusPassword}"
}

object Version {
    const val PANDA_COMMONS = "2.1.5"
    const val SWAGGER2 = "2.9.2"
}

subprojects {

    apply(plugin = "idea")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = Project.GROUP
    version = Project.VERSION
    java.sourceCompatibility = JavaVersion.VERSION_1_8

    repositories {
        mavenCentral()
        mavenLocal()
        maven(Maven.URL_RELEASE) {
            credentials {
                username = Maven.USERNAME
                password = Maven.PASSWORD
            }
        }
    }

    dependencies {
        implementation(kotlin("stdlib-jdk8"))
        implementation("com.jxpanda:jxpanda-commons:${r'${Version.PANDA_COMMONS}'}")
        implementation("io.springfox:springfox-swagger2:${r'${Version.SWAGGER2}'}")
    }

    tasks.withType<${"KotlinCompile"}> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }

}

project(":${r'${Project.ARTIFACT_ID_API}'}") {

    apply(plugin = "maven-publish")

    publishing {
        publications {
            create<${"MavenPublication"}>("maven") {
                groupId = Project.GROUP_ID
                artifactId = Project.ARTIFACT_ID_API
                version = Project.VERSION

                from(components["java"])
            }
        }
        repositories {
            maven {
                url = uri(if (version.toString().endsWith("SNAPSHOT")) Maven.URL_SNAPSHOTS else Maven.URL_RELEASE)
                credentials {
                    username = Maven.USERNAME
                    password = Maven.PASSWORD
                }
            }
        }
    }

}

tasks.withType<${"Generator"}> {
    this.configFile = File("./generator.yml")
}