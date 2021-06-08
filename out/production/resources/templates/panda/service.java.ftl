package ${package.Service}<#if !kotlin>;</#if>

import ${package.Entity}.${entity}<#if !kotlin>;</#if>
import ${superServiceClassPackage}<#if !kotlin>;</#if>

/**
 * ${table.comment!} 服务类
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

}
</#if>
