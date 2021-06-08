package com.tuanzili.generator.config

import com.baomidou.mybatisplus.generator.config.PackageConfig

/**
 * Created by Panda on 2018/8/14
 */
data class Package(
        /**
         * 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
         */
        var parent: String = "com.baomidou",
        /**
         * 父包模块名。
         */
        var moduleName: String = "",
        /**
         * Entity包名
         */
        var entity: String = "entity",
        /**
         * Service包名
         */
        var service: String = "service",
        /**
         * Service Impl包名
         */
        var serviceImpl: String = "service.impl",
        /**
         * Mapper包名
         */
        var mapper: String = "mapper",
        /**
         * Mapper XML包名
         */
        var xml: String = "mapper.xml",
        /**
         * Controller包名
         */
        var controller: String = "controller",
        /**
         * 路径配置信息
         */
        var pathInfo: MutableMap<String, String>? = null
) : MybatisPlusAssembler<PackageConfig> {

    fun rebuild4Panda(panda: Boolean, dubbo: Dubbo): Package {
        if (panda && dubbo.enable) {
            // dubbo模式下，与service同包名
            this.serviceImpl = this.service
        }
        return this
    }

    override fun toMybatisPlusConfig(): PackageConfig {
        val config = PackageConfig()
        config.parent = parent
        config.moduleName = moduleName
        config.entity = entity
        config.service = service
        config.serviceImpl = serviceImpl
        config.mapper = mapper
        config.xml = xml
        config.controller = controller
        config.pathInfo = pathInfo
        return config
    }

}