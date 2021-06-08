package ${package.Mapper}<#if !kotlin>;</#if>

import ${package.Entity}.${entity}<#if !kotlin>;</#if>
import ${superMapperClassPackage}<#if !kotlin>;</#if>

/**
 * ${table.comment!} Mapper 接口
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {

}
</#if>
