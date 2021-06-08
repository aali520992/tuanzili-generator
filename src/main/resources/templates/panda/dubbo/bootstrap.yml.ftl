spring:
  application:
    name: ${applicationName}
  main:
    allow-bean-definition-overriding: true
  cloud:
    nacos:
      config:
        server-addr: ${nacosAddress}:${nacosPort}
      discovery:
        enabled: true
        register-enabled: true
        server-addr: ${nacosAddress}:${nacosPort}