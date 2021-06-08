package com.tuanzili.generator.config

import com.baomidou.mybatisplus.annotation.DbType
import com.baomidou.mybatisplus.generator.config.DataSourceConfig
import com.baomidou.mybatisplus.generator.config.GlobalConfig
import com.baomidou.mybatisplus.generator.config.IDbQuery
import com.baomidou.mybatisplus.generator.config.ITypeConvert
import com.baomidou.mybatisplus.generator.config.converts.*
import com.baomidou.mybatisplus.generator.config.querys.*
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType
import com.baomidou.mybatisplus.generator.config.rules.IColumnType

/**
 * Created by Panda on 2018/8/12
 */

/**
 * 默认的查询类
 *
 * 以下几个类型mybatis-plus也没有默认的实现，暂时不实现了
 * DbType.H2
 * DbType.HSQL
 * DbType.SQLITE
 * DbType.SQL_SERVER2005
 * */
val QUERY_MAP = mapOf<DbType, IDbQuery>(
        DbType.MYSQL to MySqlQuery(),
        DbType.MARIADB to MariadbQuery(),
        DbType.ORACLE to OracleQuery(),
        DbType.DB2 to DB2Query(),
        DbType.POSTGRE_SQL to PostgreSqlQuery(),
        DbType.SQL_SERVER to SqlServerQuery()
)

val CONVERT_MAP = mapOf(
        DbType.MYSQL to MySqlTypeConvert(),
        DbType.MARIADB to MySqlTypeConvert(),
        DbType.ORACLE to OracleTypeConvert(),
        DbType.DB2 to DB2TypeConvert(),
        DbType.POSTGRE_SQL to PostgreSqlTypeConvert(),
        DbType.SQL_SERVER to SqlServerTypeConvert()
)


data class DataSource(
        /**
         * PostgreSQL schemaName
         */
        var schemaName: String = "",
        /**
         * 驱动连接的地址
         */
        var host: String = "",
        /**
         * 驱动连接的端口，默认3306
         * */
        var port: Int = 3306,
        /**
         * 驱动名称，默认是mysql的驱动（请确保依赖）
         */
        var driverName: String = "com.mysql.cj.jdbc.Driver",
        /**
         * 数据库名称
         * */
        var database: String = "",
        /**
         * 数据库连接用户名
         */
        var username: String = "",
        /**
         * 数据库连接密码
         */
        var password: String = "",
        /**
         * 编码，默认：UTF-8
         * */
        var encoding: String = "UTF8",
        /**
         * ssl参数，默认：false
         * */
        var ssl: Boolean = false,
        /**
         * 服务器时区，默认：Asia/Shanghai
         * */
        var serverTimezone: String = "Asia/Shanghai",
        /**
         * Mysql 8.0 在重启数据库之后，初次连接要设置这个参数为true才能连接
         * */
        var allowPublicKeyRetrieval: Boolean = false

) : MybatisPlusAssembler<DataSourceConfig> {

    /**
     * URL默认是基于host:port等参数构建的
     * 但是如果外部主动设定了这个属性，那么，之前设定的host等参数就会被覆盖掉
     * */
    @SuppressWarnings
    var url: String = ""
        get() {
            if (field.isBlank()) {
                field = "jdbc:${dbType.db}://$host:$port/$database?characterEncoding=$encoding&useSSL=$ssl&serverTimezone=$serverTimezone&allowPublicKeyRetrieval=$allowPublicKeyRetrieval"
            }
            return field
        }


    /**
     * 数据库类型，默认MySql
     */
    @SuppressWarnings
    var dbType: DbType = DbType.MYSQL
        set(value) {
            field = value
            dbQuery = QUERY_MAP[dbType] ?: MySqlQuery()
            typeConvert = CONVERT_MAP[dbType] ?: MySqlTypeConvert()
        }

    /**
     * 下面两个属性使用yml是无法注入进来的
     * 但是可以在build.gradle文件中使用闭包注入进来
     * */

    /**
     * 数据库信息查询，默认是MySql
     * 增加默认值的查询，用来生成带的时候，如果数据库中指定了默认值，把默认值加上去
     */
    private var dbQuery: IDbQuery = QUERY_MAP[dbType] ?: MySqlQuery()

    /**
     * 类型转换，也是基于MySqlTypeConvert
     */
    private var typeConvert: ITypeConvert = CONVERT_MAP[dbType] ?: MySqlTypeConvert()


    fun rebuild4Panda(panda: Boolean): DataSource {
        if (panda) {
            /**
             * 由于我有在建表的时候给默认值的习惯，所以这里自定义查询一下默认值
             * 然后在模板中，把默认值在初始化的时候就加上去（仅限kotlin）
             * */
            dbQuery = object : MySqlQuery() {
                override fun fieldCustom(): Array<String> {
                    return arrayOf("DEFAULT")
                }
            }
            /**
             * 我喜欢用String类型做主键，因为Long类型的话，回传给前端JS代码的时候会出现精度丢失的问题（踩过好几次这个坑了）
             * 我喜欢用bigint(20)声明键
             * */
            typeConvert = object : MySqlTypeConvert() {
                override fun processTypeConvert(globalConfig: GlobalConfig, fieldType: String): IColumnType {
                    if (fieldType.contains("bigint(20)")) {
                        return DbColumnType.STRING
                    }
                    return super.processTypeConvert(globalConfig, fieldType)
                }
            }
        }
        return this
    }

    override fun toMybatisPlusConfig(): DataSourceConfig {
        val config = DataSourceConfig()
        config.dbType = dbType
        config.dbQuery = dbQuery
        config.schemaName = schemaName
        config.typeConvert = typeConvert
        config.url = url
        config.driverName = driverName
        config.username = username
        config.password = password
        return config
    }

}