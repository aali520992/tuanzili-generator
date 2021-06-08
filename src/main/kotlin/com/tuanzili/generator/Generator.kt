package com.tuanzili.generator

import com.baomidou.mybatisplus.generator.config.ITypeConvert
import com.tuanzili.generator.config.Config
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import org.yaml.snakeyaml.Yaml
import java.io.File

/**
 * Created by Panda on 2018/8/5
 */
open class Generator : DefaultTask() {


    /**
     * 配置文件
     * 默认是resources下的generator.yml
     * 这里由于打了注解@InputFile，gradle会负责检查文件是否存在
     * 如果文件不存在会抛出异常，所以后续不需要做容灾判定了
     * */
    @get:InputFile
    var configFile: File = File("${project.projectDir.path}/src/main/resources/generator.yml")

    @set:Input
    var typeMapper: ITypeConvert? = null

    @TaskAction
    fun generate() {
        // 解析一下配置文件,然后执行
        val config = Yaml().loadAs(configFile.inputStream(), Config::class.java)
        println("Start generating")
        val autoGenerator = config.toMybatisPlusConfig()

        if (typeMapper != null) {
            val typeConvert = autoGenerator.dataSource.typeConvert
            autoGenerator.dataSource.apply {
                this.typeConvert = ITypeConvert { globalConfig, fieldType ->
                    typeMapper?.processTypeConvert(globalConfig, fieldType)
                            ?: typeConvert.processTypeConvert(globalConfig, fieldType)
                }
            }
        }

        autoGenerator.execute()
        println("Complete generation")
        // 有几个文件是基于dubbo模式来创建的，其实就是想建议的实现spring Initializr的功能
        // 做出一个脚手架来
        // 这个版本先暂时的简易配置，灵活的配置在以后的使用过程中再说

        // 开始处理dubbo的一些配置文件生成
        if (config.panda && config.dubbo.enable) {
            config.dubbo.writeFiles(config)
        }


    }


}
