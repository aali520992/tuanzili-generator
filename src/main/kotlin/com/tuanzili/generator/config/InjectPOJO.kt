package com.tuanzili.generator.config

/**
 * 路径默认值
 * */
const val PATH_DTO = "src/main/java/"

/**
 * 模板默认值
 * */
const val TEMPLATE_DTO = "/templates/panda/entityDTO.kt.ftl"

/**
 * Created by Panda on 2018/8/27
 * 注入的pojo配置
 * 只有我自己在用
 */
data class InjectPOJO(
        /**
         * DTO对象注入配置
         * */
        var dto: InjectionParameter = InjectionParameter("dto", TEMPLATE_DTO, PATH_DTO, "dto", "%sDTO")
) : Injection() {
    override fun outList(): MutableList<InjectionParameter> {
        return mutableListOf(dto)
    }

    override fun flushList(): MutableList<InjectionParameter> {
        return mutableListOf(dto)
    }
}