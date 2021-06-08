<#macro defaultValue field>
    <#if field.propertyType == "Long">
        ${field.customMap["DEFAULT"]!0}<#t>
    <#elseif field.propertyType == "BigDecimal">
        BigDecimal(${field.customMap["DEFAULT"]!0})<#t>
    <#elseif field.propertyType == "String">
        <#if field.type?lower_case?contains("int")>""<#else>"${field.customMap["DEFAULT"]!""}"</#if><#t>
    <#elseif field.propertyType == "Boolean">
        ${(field.customMap["DEFAULT"] == "0")?string("false","true")}<#t>
    <#elseif field.propertyType == "LocalDateTime">
        LocalDateTime.now()<#t>
    </#if>
</#macro>
<#macro toString field>
    <#if field.propertyType == "LocalDateTime">
        "${r'${'}${field.propertyName}.formatting()}"<#t>
    <#elseif field.propertyType == "BigDecimal">
        ${r'${'}${field.propertyName}.formatting()}<#t>
    <#elseif field.propertyType == "String">
        "$${field.propertyName}"<#t>
    <#else>
        $${field.propertyName}<#t>
    </#if>
</#macro>
package ${cfg.dto.package}

<#list table.importPackages as pkg>
<#if !pkg?contains("mybatisplus") && !pkg?contains(superEntityClass)>
import ${pkg}
</#if>
</#list>
import ${package.Entity}.${entity}
import com.fasterxml.jackson.annotation.JsonIgnore
import com.jxpanda.common.utils.formatting
import com.jxpanda.common.base.DTO

/**
 *
 * 数据传输对象，设计为【空安全】的【只读】对象
 *
 * @author ${author}
 * @since ${date}
 */
data class ${entity}DTO(
        @JsonIgnore
        override val entity: ${entity},
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
        <#if field.comment!?length gt 0>
        /**
         * ${field.comment}
         */
        </#if>
        <#if field.propertyType == "Integer">
        val ${field.propertyName}: Int = entity.${field.propertyName} ?: ${field.customMap["DEFAULT"]}<#if field_has_next>,</#if>
        <#else>
        val ${field.propertyName}: ${field.propertyType} = entity.${field.propertyName} ?: <@defaultValue field=field></@defaultValue><#if field_has_next>,</#if>
        </#if>
</#list>
<#-- ----------  END 字段循环遍历  ---------->
) : DTO<${entity}>(entity) {

    @JvmOverloads
    constructor(${entity?uncap_first}: ${entity}? = null) : this(${entity?uncap_first} ?: ${entity}())

    override fun toString(): String {
        return """
            `{
                `"id":"$id",
                `"createdDate":"${r'${createdDate.formatting()}'}",
                `"updatedDate":"${r'${updatedDate.formatting()}'}",
                `"deleted":$deleted,
<#list table.fields as field>
                `"${field.propertyName}":<@toString field=field></@toString><#if field_has_next>,</#if>
</#list>
            `}
        """.trimMargin("`").replace("\n","")
    }
}
