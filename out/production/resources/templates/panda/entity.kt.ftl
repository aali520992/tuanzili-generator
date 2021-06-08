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
package ${package.Entity}

import com.baomidou.mybatisplus.annotation.TableName
import com.baomidou.mybatisplus.annotation.TableField
<#list table.importPackages as pkg>
    <#if !pkg?contains("IdType") && !pkg?contains("TableId")>
import ${pkg}
    </#if>
</#list>
<#if swagger2>
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
</#if>
import com.jxpanda.common.utils.formatting

/**
 * ${table.comment}
 *
 * @author ${author}
 * @since ${date}
 */
@TableName("`${table.name}`")
<#if swagger2>
@ApiModel(value="${entity}对象", description="${table.comment!}")
</#if>
data class ${entity}(
<#------------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>
    <#if field.comment!?length gt 0>
        <#if swagger2>
        @ApiModelProperty(value = "${field.comment}")
        <#else>
        /**
         * ${field.comment}
         */
        </#if>
    </#if>
    <#if field.keyFlag>
    <#-- 主键 -->
        <#if field.keyIdentityFlag>
        @TableId(value = "${field.name}", type = IdType.AUTO)
        <#elseif idType ??>
        @TableId(value = "${field.name}", type = IdType.${idType})
        <#elseif field.convert>
        @TableId("${field.name}")
        </#if>
    <#-- 普通字段 -->
    <#elseif field.fill??>
    <#-------   存在字段填充设置   ----->
        <#if field.convert>
        @TableField(value = "${field.name}", fill = FieldFill.${field.fill})
        <#else>
        @TableField(fill = FieldFill.${field.fill})
        </#if>
    <#else>
        @TableField("`${field.name}`")
    </#if>
    <#-- 乐观锁注解 -->
    <#if (versionFieldName!"") == field.name>
        @Version
    </#if>
    <#-- 逻辑删除注解 -->
    <#if (logicDeleteFieldName!"") == field.name>
        @TableLogic
    </#if>
    <#if field.propertyType == "Integer">
        var ${field.propertyName}: Int = ${field.customMap["DEFAULT"]}<#if field_has_next>,</#if>
    <#else>
        var ${field.propertyName}: ${field.propertyType} = <@defaultValue field=field></@defaultValue><#if field_has_next>,</#if>
    </#if>
</#list>
<#------------  END 字段循环遍历  ---------->
) : ${superEntityClass}() {

<#-- 要生成一个Updater来更新对象 -->
    data class Updater(
        <#list table.fields as field>
            <#if field.propertyType == "Integer">
                var ${field.propertyName}: Int? = null<#if field_has_next>,</#if>
            <#else>
                var ${field.propertyName}: ${field.propertyType}? = null<#if field_has_next>,</#if>
            </#if>
        </#list>
    ) : Entity.Updater<${entity}>(){
        override val entityClass: Class<${entity}> = ${entity}::class.java
    }

<#if entityColumnConstant>
    companion object {
    <#list table.fields as field>
        const val ${field.name?upper_case} : String = "${field.name}"
    </#list>
    }
</#if>

    override fun toString(): String {
        return """
            `{
                `"id":"$id",
                `"createdDate":"${r'${createdDate.formatting()}'}",
                `"updatedDate":"${r'${updatedDate.formatting()}'}",
                `"deletedDate":"${r'${deletedDate.formatting()}'}",
                `"deleted":$deleted,
            <#list table.fields as field>
                `"${field.propertyName}":<@toString field=field></@toString><#if field_has_next>,</#if>
            </#list>
            `}
        """.trimMargin("`").replace("\n","")
    }
}
