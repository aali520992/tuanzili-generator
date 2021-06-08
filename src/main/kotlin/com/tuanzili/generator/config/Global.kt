package com.tuanzili.generator.config

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.generator.config.GlobalConfig
import com.baomidou.mybatisplus.generator.config.rules.DateType
import java.io.File

/**
 * Created by Panda on 2018/8/12
 */
data class Global(
        /**
         * 是否覆盖已有文件
         */
        var fileOverride: Boolean = false,
        /**
         * 是否打开输出目录
         */
        var open: Boolean = false,
        /**
         * 是否在xml中添加二级缓存配置
         */
        var enableCache: Boolean = false,
        /**
         * 是否生成为 Kotlin 代码
         */
        var kotlin: Boolean = false,
        /**
         * 开启 swagger2 模式
         */
        var swagger2: Boolean = false,
        /**
         * 开启 ActiveRecord 模式
         */
        var activeRecord: Boolean = false,
        /**
         * 是否在生成的XML文件中创建 BaseResultMap
         */
        var baseResultMap: Boolean = false,
        /**
         * 生成的时间类型对应的java类（默认是Java8 的LocalDateTime类）
         */
        var dateType: DateType = DateType.TIME_PACK,

        /**
         * 是否在生成的XML文件中创建 baseColumnList
         */
        var baseColumnList: Boolean = false,
        /**
         * 各层文件名称方式，例如： %sAction 生成 UserAction
         */
        var entityName: String = "",
        var mapperName: String = "",
        var xmlName: String = "",
        var serviceName: String = "",
        var serviceImplName: String = "",
        var controllerName: String = "",
        /**
         * 指定生成的主键的ID类型（默认idWorker）
         */
        var idType: IdType = IdType.ID_WORKER
) : MybatisPlusAssembler<GlobalConfig> {

    /**
     * 开发人员
     */
    var author: String = ""
        get() {
            if (field.isBlank() || field == "user.name") {
                field = System.getProperties().getProperty("user.name")
            }
            return field
        }

    /**
     * 生成文件的输出目录【默认】
     */
    var outputDir: String = ""
        get() {
            if (field.isBlank()) {
                field = "src${File.separator}main${File.separator}${if (kotlin) "kotlin" else "java"}"
            }
            return field
        }

    fun rebuild4Panda(panda: Boolean): Global {
        if (panda){
            this.serviceName = "%sService"
            this.baseResultMap = true
            this.baseColumnList = true
            this.fileOverride = true
            this.activeRecord = false
            this.kotlin = true
            this.swagger2 = true
        }
        return this
    }

    override fun toMybatisPlusConfig(): GlobalConfig {
        val config = GlobalConfig()
        config.outputDir = outputDir
        config.author = author
        config.isFileOverride = fileOverride
        config.isOpen = open
        config.isEnableCache = enableCache
        config.isKotlin = kotlin
        config.isSwagger2 = swagger2
        config.isActiveRecord = activeRecord
        config.isBaseResultMap = baseResultMap
        config.dateType = dateType
        config.isBaseColumnList = baseColumnList
        config.entityName = entityName
        config.mapperName = mapperName
        config.xmlName = xmlName
        config.serviceName = serviceName
        config.serviceImplName = serviceImplName
        config.controllerName = controllerName
        config.idType = idType
        return config
    }
}

