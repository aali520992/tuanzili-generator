plugins {
    id("org.springframework.boot") version "2.1.7.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    kotlin("plugin.allopen") version "1.3.41"
    kotlin("plugin.spring") version "1.3.31"
}

object Version {
    const val MYBATIS_PLUS = "3.2.0"
    const val JJWT = "0.9.1"
    const val JETCACHE = "2.5.11"
    const val SWAGGER2 = "2.9.2"
    const val ALIBABA_CLOUD = "2.1.0.RELEASE"
}

allOpen {
    annotations("org.apache.dubbo.config.annotation.Service")
}

// 从spring-boot的依赖中去除logback，改用log4j2
configurations {
    all {
        exclude(module = "spring-boot-starter-logging")
        exclude(module = "logback-classic")
        exclude(module = "log4j-over-slf4j")
    }
}

// 非spring依赖，分开一下，不然眼花
dependencies {
    implementation(project(":${api}"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.jsonwebtoken:jjwt:${r'${Version.JJWT}'}")
}

// spring相关依赖
dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    // alibaba-cloud
    implementation("com.alibaba.cloud:spring-cloud-alibaba-dubbo:${r'${Version.ALIBABA_CLOUD}'}")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config:${r'${Version.ALIBABA_CLOUD}'}")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery:${r'${Version.ALIBABA_CLOUD}'}")
    // jetCache
    implementation("com.alicp.jetcache:jetcache-starter-redis-lettuce:${r'${Version.JETCACHE}'}")
    // swagger2
    implementation("io.springfox:springfox-swagger-ui:${r'${Version.SWAGGER2}'}")
    // spring-boot 单元测试
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

// 数据库相关依赖
dependencies {
    runtime("mysql:mysql-connector-java")
    implementation("com.baomidou:mybatis-plus-boot-starter:${r'${Version.MYBATIS_PLUS}'}")
}