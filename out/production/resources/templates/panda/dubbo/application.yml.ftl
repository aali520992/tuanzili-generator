spring:
  profiles:
    active: dev
    include: database,dubbo

server:
  port: 9902
  servlet:
    context-path: /${name}