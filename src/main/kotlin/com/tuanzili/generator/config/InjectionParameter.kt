package com.tuanzili.generator.config

import com.baomidou.mybatisplus.generator.config.FileOutConfig
import com.baomidou.mybatisplus.generator.config.po.TableInfo
import java.io.File

/**
 * Created by Panda on 2018/8/21
 */
open class InjectionParameter(
        /**
         * 注入的自定义配置的key值
         * */
        private var configKey: String,
        /**
         * 使用的模板文件
         * */
        var template: String,
        /**
         * 文件输出路径
         * */
        var outputPath: String,
        /**
         * 包名，默认空（注意，这个只是相对路径的包名，不包含parent）
         * */
        var packageName: String = "",
        /**
         * 文件名的命名方式，默认%s
         * 与Global那边一致，例如： %sAction 生成 UserAction
         * */
        var fileName: String = "%s",
        /**
         * 文件后缀名，如果fileName中没有包含后缀的话，会使用这个
         * 包含了的话，就不会使用了（仅判断文件名中是否有'.'字符）
         * */
        var suffix: String = "java"
) {

    /**
     * 暂时只想到要注入包名
     * */
    fun toInjectionMap(): Map<String, Map<String, Any>> {
        return mapOf(configKey to mapOf("package" to packageName))
    }

    fun toFileOutConfig(): FileOutConfig {
        return object : FileOutConfig(template) {
            /**
             * 输出文件
             */
            override fun outputFile(tableInfo: TableInfo): String {
                val dirName = outputPath + if (packageName.isNotEmpty()) File.separator + packageName.replace(".", File.separator) else ""
                val fileName = String.format(fileName, tableInfo.entityName) + if (fileName.contains(".")) "" else ".$suffix"
                return "$dirName${File.separator}$fileName"
            }

        }
    }

    /**
     * 使用外部的配置，刷新一下属性
     * 这个是个默认的实现，一般来说，就是要重设一下包名什么的，子类可以重写这个函数，定义自己的刷新逻辑
     * */
    fun flushParameter(config: Config) {
        val kotlin = config.global.kotlin
        val parent = config.`package`.parent
        val moduleName = config.`package`.moduleName

        this.outputPath = config.global.outputDir
        this.packageName = "$parent${if (moduleName.isNotEmpty()) ".$moduleName" else ""}${if (packageName.isBlank()) "" else ".$packageName"}"

        if (kotlin) {
            this.suffix = "kt"
            this.switchKotlin()
        }
    }

    open fun switchKotlin() {}

}