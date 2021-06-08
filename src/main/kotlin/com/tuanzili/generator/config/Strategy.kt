package com.tuanzili.generator.config

import com.baomidou.mybatisplus.generator.config.ConstVal
import com.baomidou.mybatisplus.generator.config.StrategyConfig
import com.baomidou.mybatisplus.generator.config.po.TableFill
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy

/**
 * Created by Panda on 2018/8/13
 */
data class Strategy(
        /**
         * 是否大写命名
         */
        var capitalMode: Boolean = false,
        /**
         * 是否跳过视图
         */
        var skipView: Boolean = false,
        /**
         * 数据库表映射到实体的命名策略
         * 这里默认改为下划线转驼峰
         */
        var naming: NamingStrategy = NamingStrategy.underline_to_camel,
        /**
         * 数据库表字段映射到实体的命名策略
         * 未指定按照 naming 执行
         */
        var columnNaming: NamingStrategy = NamingStrategy.underline_to_camel,
        /**
         * 表前缀
         */
        var tablePrefix: MutableList<String> = ArrayList(),
        /**
         * 字段前缀
         */
        var fieldPrefix: MutableList<String> = ArrayList(),
        /**
         * 自定义继承的Entity类全称，带包名
         */
        var superEntityClass: String = "",
        /**
         * 自定义基础的Entity类，公共字段
         */
        var superEntityColumns: MutableList<String> = ArrayList(),
        /**
         * 自定义继承的Mapper类全称，带包名
         */
        var superMapperClass: String = ConstVal.SUPER_MAPPER_CLASS,
        /**
         * 自定义继承的Service类全称，带包名
         */
        var superServiceClass: String = ConstVal.SUPER_SERVICE_CLASS,
        /**
         * 自定义继承的ServiceImpl类全称，带包名
         */
        var superServiceImplClass: String = ConstVal.SUPER_SERVICE_IMPL_CLASS,
        /**
         * 自定义继承的Controller类全称，带包名
         */
        var superControllerClass: String = "",
        /**
         * 需要包含的表名，允许正则表达式（与exclude二选一配置）
         */
        var include: MutableList<String> = ArrayList(),
        /**
         * 需要排除的表名，允许正则表达式
         */
        var exclude: MutableList<String> = ArrayList(),
        /**
         * 【实体】是否生成字段常量（默认 true）
         *  public static final String ID = "test_id";
         */
        var entityColumnConstant: Boolean = true,
        /**
         * 【实体】是否为构建者模型（默认 false）
         * public User setName(String name) { this.name = name; return this; }
         */
        var entityBuilderModel: Boolean = false,
        /**
         * 【实体】是否为lombok模型（默认 false）
         * [document](https://projectlombok.org/)
         */
        var entityLombokModel: Boolean = false,
        /**
         * Boolean类型字段是否移除is前缀（默认 false）
         * 比如 : 数据库字段名称 : 'is_xxx',类型为 : tinyint. 在映射实体的时候则会去掉is,在实体类中映射最终结果为 xxx
         */
        var entityBooleanColumnRemoveIsPrefix: Boolean = false,
        /**
         * 生成 `@RestController` 控制器
         * `@Controller` -> `@RestController`
         */
        var restControllerStyle: Boolean = false,
        /**
         * 驼峰转连字符
         * `@RequestMapping("/managerUserActionHistory")` -> `@RequestMapping("/manager-user-action-history")`
         *
         */
        var controllerMappingHyphenStyle: Boolean = false,
        /**
         * 是否生成实体时，生成字段注解
         */
        var entityTableFieldAnnotationEnable: Boolean = false,
        /**
         * 乐观锁属性名称
         */
        var versionFieldName: String = "",
        /**
         * 逻辑删除属性名称
         */
        var logicDeleteFieldName: String = "",
        /**
         * 表填充字段
         */
        var tableFillList: MutableList<TableFill> = ArrayList()

) : MybatisPlusAssembler<StrategyConfig> {

    fun rebuild4Panda(panda: Boolean): Strategy {
        if (panda) {
            this.superEntityClass = "com.jxpanda.common.base.Entity"
            this.superMapperClass = "com.jxpanda.common.base.KtMapper"
            this.superServiceClass = "com.jxpanda.common.base.KtService"
            this.superServiceImplClass = "com.jxpanda.common.base.KtServiceImpl"
            this.superEntityColumns = arrayListOf("id", "created_date", "updated_date", "deleted_date", "is_deleted")
            this.naming = NamingStrategy.underline_to_camel
            this.entityColumnConstant = true
            this.logicDeleteFieldName = "deleted_date"
        }
        return this
    }

    override fun toMybatisPlusConfig(): StrategyConfig {
        val config = StrategyConfig()
        config.isCapitalMode = capitalMode
        config.columnNaming = columnNaming
        config.isControllerMappingHyphenStyle = controllerMappingHyphenStyle
        config.isEntityBooleanColumnRemoveIsPrefix = entityBooleanColumnRemoveIsPrefix
        config.isEntityBuilderModel = entityBuilderModel
        config.isEntityColumnConstant = entityColumnConstant
        config.isEntityLombokModel = entityLombokModel
        config.isEntityTableFieldAnnotationEnable = entityTableFieldAnnotationEnable
        config.setExclude(*exclude.toTypedArray())
        config.setFieldPrefix(*fieldPrefix.toTypedArray())
        config.setInclude(*include.toTypedArray())
        config.logicDeleteFieldName = logicDeleteFieldName
        config.naming = naming
        config.isRestControllerStyle = restControllerStyle
        config.isSkipView = skipView
        config.superControllerClass = superControllerClass
        config.superEntityClass = superEntityClass
        config.setSuperEntityColumns(*superEntityColumns.toTypedArray())
        config.superMapperClass = superMapperClass
        config.superServiceClass = superServiceClass
        config.superServiceImplClass = superServiceImplClass
        config.tableFillList = tableFillList
        config.setTablePrefix(*tablePrefix.toTypedArray())
        config.versionFieldName = versionFieldName
        return config
    }
}

