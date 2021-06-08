import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("idea")
    kotlin("jvm") version "1.3.41"
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "0.10.1"
}

object Project {
    const val GROUP = "com.jxpanda"
    // 声明插件的ID，其实就是包名
    const val ID = "$GROUP.generator"
    const val VERSION = "2.1.9"
    const val DESCRIPTION = "A plugin base on mybatis-plus-generator"
    val TAGS = arrayListOf("mybatis-plus", "generator", "plugin")
}

object Version {
    const val MYBATIS_PLUS = "3.2.0"
    const val FREE_MARKER = "2.3.28"
    const val SPRING_CORE = "5.1.9.RELEASE"
    const val MYSQL_CONNECTOR = "8.0.17"
}

group = Project.GROUP
version = Project.VERSION
java.sourceCompatibility = JavaVersion.VERSION_1_8


repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("mysql:mysql-connector-java:${Version.MYSQL_CONNECTOR}")
    implementation("com.baomidou:mybatis-plus-generator:${Version.MYBATIS_PLUS}")
    implementation("org.yaml:snakeyaml:+")
    implementation("org.freemarker:freemarker:${Version.FREE_MARKER}")
    implementation("org.springframework:spring-core:${Version.SPRING_CORE}")
}

gradlePlugin {
    plugins {
        create("jxpandaGeneratorPlugin") {
            id = Project.ID
            implementationClass = "${Project.ID}.GeneratorPlugin"
        }
    }
}
///batis-plus-generator的主函数弄成一个gradle插件而已。自定义参数通过配置文件来配置仿照着自己写一下kotlin
pluginBundle {
    website = "https://www.jxpanda.com"
    vcsUrl = "https://gitee.com/JXPanda/jxpanda-generator.git"
    description = Project.DESCRIPTION
    tags = Project.TAGS
    // 这个写法和上面那个gradlePlugin的写法是等效的，之所以这里用两种写法，是为了以后看见的时候记得这两种写法都可以
    (plugins) {
        "jxpandaGeneratorPlugin" {
            displayName = Project.ID
            description = Project.DESCRIPTION
            tags = Project.TAGS
            version = Project.VERSION
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}