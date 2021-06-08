package com.tuanzili.generator.config

import com.baomidou.mybatisplus.generator.config.ConstVal
import com.baomidou.mybatisplus.generator.config.TemplateConfig
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine

val ENGINE_MAP = mapOf(
        "velocity" to VelocityTemplateEngine(),
        "freemarker" to FreemarkerTemplateEngine()
)

/**
 * 我自己用的模板文件，仅panda为true的时候使用
 * */
const val TEMPLATE_ENTITY_JAVA = "/templates/panda/entity.java"
const val TEMPLATE_ENTITY_KT = "/templates/panda/entity.kt"
const val TEMPLATE_MAPPER = "/templates/panda/mapper.java"
const val TEMPLATE_XML = "/templates/panda/mapper.xml"
const val TEMPLATE_SERVICE = "/templates/panda/service.java"
const val TEMPLATE_SERVICE_IMPL = "/templates/panda/serviceImpl.java"
const val TEMPLATE_SERVICE_IMPL_DUBBO = "/templates/panda/dubbo/serviceImpl.java"
const val TEMPLATE_CONTROLLER = "/templates/panda/controller.java"

/**
 * Created by Panda on 2018/8/14
 */
data class Template(
        var engine: AbstractTemplateEngine = FreemarkerTemplateEngine(),
        var entity: String = ConstVal.TEMPLATE_ENTITY_JAVA,
        var entityKt: String = ConstVal.TEMPLATE_ENTITY_KT,
        var service: String = ConstVal.TEMPLATE_SERVICE,
        var serviceImpl: String = ConstVal.TEMPLATE_SERVICE_IMPL,
        var mapper: String = ConstVal.TEMPLATE_MAPPER,
        var xml: String = ConstVal.TEMPLATE_XML,
        var controller: String = ConstVal.TEMPLATE_CONTROLLER
) : MybatisPlusAssembler<TemplateConfig> {

    var template: String = "freemarker"
        set(value) {
            field = value
            engine = ENGINE_MAP[value] ?: FreemarkerTemplateEngine()
        }

    /**
     * 重写一下路径
     * */
    fun rebuild4Panda(panda: Boolean, dubbo: Dubbo): Template {
        if (panda) {
            engine = object : FreemarkerTemplateEngine() {
                override fun init(configBuilder: ConfigBuilder): FreemarkerTemplateEngine {
                    return super.init(resetPath(configBuilder, dubbo))
                }
            }
            entity = TEMPLATE_ENTITY_JAVA
            entityKt = TEMPLATE_ENTITY_KT
            service = TEMPLATE_SERVICE
            serviceImpl = if (dubbo.enable) TEMPLATE_SERVICE_IMPL_DUBBO else TEMPLATE_SERVICE_IMPL
            mapper = TEMPLATE_MAPPER
            xml = TEMPLATE_XML
            // 因为用了graphql，所以暂时不生成controller
            controller = ""
        }
        return this
    }


    /**
     * 重设路径
     * */
    fun resetPath(configBuilder: ConfigBuilder, dubbo: Dubbo): ConfigBuilder {
        val pathInfo = configBuilder.pathInfo
        val apiPrefix = if (dubbo.enable) "${dubbo.apiPath}/" else ""
        val servicePrefix = if (dubbo.enable) "${dubbo.servicePath}/" else ""
        pathInfo.forEach {
            val prefix = if (it.key == ConstVal.ENTITY_PATH || it.key == ConstVal.SERVICE_PATH) {
                apiPrefix
            } else {
                servicePrefix
            }
            pathInfo[it.key] = "$prefix${it.value}"
        }
        // 我喜欢把xml文件放在resources/mapper下
        pathInfo[ConstVal.XML_PATH] = "$servicePrefix${Dubbo.PATH_RESOURCES}/mapper"
        return configBuilder
    }

    override fun toMybatisPlusConfig(): TemplateConfig {
        val config = TemplateConfig()
        config.setEntity(entity)
        config.entityKt = entityKt
        config.service = service
        config.serviceImpl = serviceImpl
        config.mapper = mapper
        config.xml = xml
        config.controller = controller
        return config
    }

}