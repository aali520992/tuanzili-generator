package com.tuanzili.generator.config

import com.baomidou.mybatisplus.generator.AutoGenerator
import com.baomidou.mybatisplus.generator.InjectionConfig

/**
 * Created by Panda on 2018/8/8
 * 配置文件的解析类，属性、结构和mybatis-plus的一样
 * 之所以单独写一个，是因为无法直接把读取到的yml文件转为AutoGenerator类，所以这里用一个类中转一下
 * 该类中所有用到的类都与AutoGenerator类保持一致
 */
data class Config(
    /**
         * 是否是我自己用
         * */
        var panda: Boolean = false,
    /**
         * 是否跳过生成服务类（用于只刷新pojo，不改变service的场景）
         * */
        var skipService: Boolean = false,
    /**
         * 如果要用dubbo。会把生成的包和服务实现放在两个子模块里
         * 把entity和service接口放在名为api的子模块里
         * 把mapper和service的实现类放在service模块里
         * 这个配置目前处于试验阶段，暂时不支持灵活配置，部分配置规则写死
         * */
        var dubbo: Dubbo = Dubbo(),
    /**
         * 是否生成一些自定义的pojo，默认不生成
         * */
        var pojo: InjectPOJO? = null,
    /**
         * 全局配置
         * */
        var global: Global = Global(),
    /**
         * 数据源配置
         * */
        var dataSource: DataSource = DataSource(),
    /**
         * 策略配置
         * */
        var strategy: Strategy = Strategy(),
    /**
         * 包配置
         * */
        var `package`: Package = Package(),
    /**
         * 模板配置
         * */
        var template: Template = Template()

) : MybatisPlusAssembler<AutoGenerator> {

    override fun toMybatisPlusConfig(): AutoGenerator {
        val autoGenerator = AutoGenerator()
        autoGenerator.globalConfig = global.rebuild4Panda(panda).toMybatisPlusConfig()
        autoGenerator.dataSource = dataSource.rebuild4Panda(panda).toMybatisPlusConfig()
        autoGenerator.strategy = strategy.rebuild4Panda(panda).toMybatisPlusConfig()
        autoGenerator.packageInfo = `package`.rebuild4Panda(panda, dubbo).toMybatisPlusConfig()
        autoGenerator.template = template.rebuild4Panda(panda, dubbo).toMybatisPlusConfig()
        autoGenerator.templateEngine = template.engine

//        2.0.0版本开始，不再生成DTO了，所以不用注入了
//        if (panda) {
//            pojo = pojo ?: InjectPOJO()
//        }

        val injections = listOf(pojo)

        autoGenerator.cfg = if (injections.any { injection -> injection != null }) {
            Injection(this, injections.toMutableList())
        } else {
            Injection()
        }

        if (skipService) {
            autoGenerator.template.service = ""
            autoGenerator.template.serviceImpl = ""
        }

        return autoGenerator
    }
}
