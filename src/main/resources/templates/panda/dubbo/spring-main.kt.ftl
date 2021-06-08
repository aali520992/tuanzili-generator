package ${package}

import org.apache.dubbo.config.annotation.Service
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(includeFilters = [ComponentScan.Filter(classes = [Service::class])])
class ${name}Application

fun main(args: Array<${"String"}>) {
    runApplication<${name + "Application"}>(*args)
}