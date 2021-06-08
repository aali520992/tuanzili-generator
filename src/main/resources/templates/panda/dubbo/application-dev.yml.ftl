spring:
  datasource:
    url: ${url}
    driver-class-name: ${driverName}
    hikari:
      username: ${username}
      password: ${password}

dubbo:
  registry:
    address: nacos://${nacosAddress}:${nacosPort}