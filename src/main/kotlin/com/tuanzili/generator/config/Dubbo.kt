package com.tuanzili.generator.config

//import com.baomidou.mybatisplus.extension.toolkit.PackageHelper
import org.gradle.internal.impldep.org.apache.commons.io.FileUtils
import java.io.File
import java.io.FileInputStream
import java.nio.channels.Channels
import java.nio.channels.ReadableByteChannel
import java.nio.charset.Charset
import java.nio.file.CopyOption
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * 试行阶段
 * 这个是dubbo的一些小配置
 * 打开dubbo模式的话，会改变一些行为
 * 主要有以下几点：
 * 1.entity和service接口，分到另外一个子项目中（用gradle维护）
 * 2.同理，mapper、serviceImpl、mapper*。xml会生成到另外一个项目（用gradle维护）
 * 3.serviceImpl中的@Service注解会替换为dubbo的@Service注解（包名不同）
 * 4.同步创建build.gradle.kts文件，同步初始化项目结构，同步初始化一些配置文件（application.yml、application-database.yml、application-dev.yml等）
 * 5.其他还没想到的等用的时候想到了再来补充迭代
 *
 * 备注：dubbo模式只有在panda模式为true的时候才会生效（试行阶段，我自己用，话说也没有别人在用这个）
 * */
data class Dubbo(
        /**
         * 模式开关，默认是false
         * */
        var enable: Boolean = false,
        /**
         * maven坐标
         * */
        var group: String = "",
        /**
         * maven坐标
         * */
        var artifactIdAPI: String = "",
        /**
         * maven坐标
         * */
        var artifactIdService: String = "",
        /**
         * 项目包名
         * */
        var packageName: String = "",
        /**
         * 项目名
         * */
        var name: String = "",
        /**
         * api子模块的名称，默认：api
         * */
        var api: String = "api",
        /**
         * service子模块的名称，默认：service
         * */
        var service: String = "service",
        /**
         * 应用名称，这个属性用来注册到Nacos的
         * */
        var applicationName: String = "",
        /**
         * 生成的nacos配置文件中，nacos的地址
         * */
        var nacosAddress: String = "",
        /**
         * nacos的端口，默认8848
         * */
        var nacosPort: Int = 8848,
        /**
         * dubbo模式会生成一些默认的配置文件，这个是redis的配置文件，如果没有的话就不会写入文件
         * */
        var redisAddress: String = "",
        /**
         * maven私服的数据
         * */
        var nexus: String = "",
        var nexusUser: String = "",
        var nexusPassword: String = "",
        /**
         * kotlin和此插件的版本信息
         * */
        var kotlinVersion: String = "1.3.50",
        var generatorVersion: String = "2.1.5",
        /**
         * 是否是初次运行的标记
         * */
        var init: Boolean = false,
        /**
         * 是否覆盖配置文件，默认是false
         * 如果是true的话，会强行覆盖配置文件（目前只有几个yml文件而已）
         * */
        var overwriteConfig: Boolean = false
) {

    /**
     * api包的路径
     * */
    val apiPath: String
        get() {
            return "$name-$api"
        }
    /**
     * service包的路径
     * */
    val servicePath: String
        get() {
            return "$name-$service"
        }

    /**
     * 写入文件
     * 分两步
     * 1：基于freemarker写入文件
     * 2：复制需要复制的配置文件
     * */
    fun writeFiles(config: Config) {
        // 创建freemarker生成的文件
        val engine = config.template.engine
        templateFiles(config).forEach {
            val outFile = "$servicePath/${it.outFile}"
            if (isCreate(outFile)) {
                engine.writer(it.param, it.templatePath, outFile)
            }
        }
        // 复制文件
        copyFiles().forEach {
            val outFile = "$servicePath/${it.outFile}"
            if (isCreate(outFile, init)) {
                copyToFile(it.sourceFile, outFile)
            }
        }
        // 为api包创建一个空的build.gradle.kts文件
        val apiBuildFile = "$apiPath/build.gradle.kts"
        if (isCreate(apiBuildFile)) {
            File(apiBuildFile).createNewFile()
        }
        if (init) {
            // 单独刷新一次根目录的配置文件
            initTemplate().forEach {
                if (isCreate(it.outFile, init)) {
                    // 尝试删除不带.kts后缀的文件
                    val file = File(it.outFile.replace(".kts", ""))
                    if (file.exists()) {
                        file.delete()
                    }
                    engine.writer(it.param, it.templatePath, it.outFile)
                }
            }
        }
    }

    private fun initTemplate(): List<TemplateParam> {
        val apiArtifactId = if (artifactIdAPI.isBlank()) apiPath else artifactIdAPI
        val artifactIdService = if (artifactIdService.isBlank()) servicePath else artifactIdService
        return listOf(
                TemplateParam(templateName = "parent-build.gradle.kts.ftl",
                        outPath = "./",
                        outFileName = "build.gradle.kts",
                        param = mapOf(
                                "kotlinVersion" to kotlinVersion,
                                "generatorVersion" to generatorVersion,
                                "name" to name,
                                "group" to group,
                                "apiArtifactId" to apiArtifactId,
                                "serviceArtifactId" to artifactIdService,
                                "nexus" to if (nexus.contains("http")) nexus else "http://$nexus",
                                "nexusUser" to nexusUser,
                                "nexusPassword" to nexusPassword
                        )
                ),
                TemplateParam(templateName = "parent-settings.gradle.kts.ftl",
                        outPath = "./",
                        outFileName = "settings.gradle.kts",
                        param = mapOf(
                                "name" to name,
                                "apiArtifactId" to apiArtifactId,
                                "serviceArtifactId" to artifactIdService
                        )
                )
        )
    }


    private fun templateFiles(config: Config): List<TemplateParam> {
        val `package` = config.`package`
        val dataSource = config.dataSource
        val applicationRootPath = (if (packageName.isBlank()) `package`.parent else packageName).replace(".", "/")
        return listOf(
                TemplateParam("application.yml.ftl", PATH_RESOURCES, mapOf(
                        "name" to name
                )),
                TemplateParam("application-dubbo.yml.ftl", PATH_CONFIG, mapOf(
                        "dubboScanPackage" to "${`package`.parent}.${`package`.serviceImpl}"
                )),
                TemplateParam("bootstrap.yml.ftl", PATH_RESOURCES, mapOf(
                        "applicationName" to if (applicationName.isBlank()) "$name-$service" else applicationName,
                        "nacosAddress" to nacosAddress,
                        "nacosPort" to "$nacosPort"
                )),
                TemplateParam("application-dev.yml.ftl", PATH_CONFIG, mapOf(
                        "url" to dataSource.url,
                        "driverName" to dataSource.driverName,
                        "username" to dataSource.username,
                        "password" to dataSource.password,
                        "nacosAddress" to nacosAddress,
                        "nacosPort" to "$nacosPort"
                )),
                TemplateParam("build.gradle.kts.ftl", "", mapOf(
                        "api" to apiPath
                )),
                TemplateParam(templateName = "spring-main.kt.ftl",
                        outPath = "$PATH_SOURCE/$applicationRootPath/$name/",
                        outFileName = "${name.capitalize()}Application.kt",
                        param = mapOf(
                                "package" to "$packageName.$name",
                                "name" to name.capitalize()
                        )
                ),
                TemplateParam(templateName = "mybatis-config.kt.ftl",
                        outPath = "$PATH_SOURCE/$applicationRootPath/$name/config/database/",
                        outFileName = "MybatisPlusConfig.kt",
                        param = mapOf(
                                "package" to "$packageName.$name",
                                "mapperPackage" to "${`package`.parent}.${`package`.mapper}"
                        )
                )
        )
    }

    private fun copyFiles(): List<CopyParam> {
        return listOf(
                CopyParam("application-database.yml", PATH_CONFIG),
                CopyParam("log4j2-spring.xml", PATH_RESOURCES)
        )
    }

    /**
     * 检测文件是否存在
     *
     * @return 文件是否存在
     */
    private fun isCreate(filePath: String, forceOver: Boolean = overwriteConfig): Boolean {
        // 全局判断【默认】
        val file = File(filePath)
        val exist = file.exists()
//        if (!exist) {
//            PackageHelper.mkDir(file.parentFile)
//        }
        return !exist || forceOver
    }


    private fun copyToFile(sourceFile: String, outFile: String) {

        val inputStream = this.javaClass.getResourceAsStream(sourceFile).buffered()
        val outputStream = File(outFile).outputStream().buffered()

        inputStream.copyTo(outputStream)

        inputStream.close()
        outputStream.close()
    }

    companion object {
        private const val TEMPLATE_DIRECTORY = "/templates/panda/dubbo"
        const val PATH_SOURCE = "src/main/kotlin"
        const val PATH_RESOURCES = "src/main/resources"
        private const val PATH_CONFIG = "src/main/resources/config"
    }

    inner class TemplateParam(
        private val templateName: String,
        private val outPath: String,
        val param: Map<String, Any>,
        val templatePath: String = "$TEMPLATE_DIRECTORY/$templateName",
        private val outFileName: String = templateName.replace(".ftl", ""),
        val outFile: String = "$outPath/$outFileName"
    )

    inner class CopyParam(
        private val sourceFileName: String,
        private val outPath: String,
        val sourceFile: String = "$TEMPLATE_DIRECTORY/$sourceFileName",
        val outFile: String = "$outPath/$sourceFileName"
    )

}