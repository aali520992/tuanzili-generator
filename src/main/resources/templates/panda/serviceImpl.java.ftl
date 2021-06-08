package ${package.ServiceImpl}<#if !kotlin>;</#if>

import ${package.Entity}.${entity}<#if !kotlin>;</#if>
import ${package.Mapper}.${table.mapperName}<#if !kotlin>;</#if>
import ${package.Service}.${table.serviceName}<#if !kotlin>;</#if>
import ${superServiceImplClassPackage}<#if !kotlin>;</#if>
import org.springframework.stereotype.Service<#if !kotlin>;</#if>

/**
 * ${table.comment!} 服务实现类
 *
 * @author ${author}
 * @since ${date}
 */
@Service
<#if kotlin>
class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {
    override val emptyEntity: ${entity} = ${entity}()
}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

}
</#if>
