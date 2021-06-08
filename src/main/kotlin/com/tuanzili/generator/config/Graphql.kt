package com.tuanzili.generator.config


/**
 * 默认的路径
 * */
const val PATH_SCHEMA = "src/main/resources/graphql"
const val PATH_RESOLVER = "src/main/java/"

// graphql的模板文件，仅graphql为true的时候使用
const val TEMPLATE_SCHEMA = "/templates/graphql/schema.graphqls.ftl"
const val TEMPLATE_RESOLVER_JAVA = "/templates/graphql/resolver.java.ftl"
const val TEMPLATE_RESOLVER_KOTLIN = "/templates/graphql/resolver.kt.ftl"

/**
 * Created by Panda on 2018/8/21
 * graphql的配置
 */
data class Graphql(
    /**
         * schema的配置
         * */
        var schema: InjectionParameter = InjectionParameter("schema", TEMPLATE_SCHEMA, PATH_SCHEMA, "", "%s","graphqls"),
    /**
         * resolver的配置
         * */
        var resolver: InjectionParameter = object : InjectionParameter("resolver", TEMPLATE_RESOLVER_JAVA, PATH_RESOLVER, "resolver", "%sResolver") {
            override fun switchKotlin() {
                this.template = TEMPLATE_RESOLVER_KOTLIN
            }
        }
) : Injection() {

    override fun outList(): MutableList<InjectionParameter> {
        return mutableListOf(schema, resolver)
    }

    override fun flushList(): MutableList<InjectionParameter> {
        return mutableListOf(resolver)
    }
}
