package com.tuanzili.generator.config

import com.baomidou.mybatisplus.generator.InjectionConfig
import java.util.*

/**
 * Created by Panda on 2018/8/27
 */
open class Injection() : InjectionConfig() {

    /**
     * 子类不会持有该指针
     * */
    private lateinit var config: Config

    /**
     * 子类的集合
     * */
    private var injections: MutableList<Injection?> = ArrayList()

    /**
     * 是否初始化过的标记
     * 看了下源码，mybatis-plus是在循环中每次都会调用initMap()的
     * */
    private var initialized: Boolean = false

    constructor(config: Config, injections: MutableList<Injection?>) : this() {
        this.config = config
        this.injections = injections
    }


    /**
     * 注入自定义 Map 对象
     */
    override fun initMap() {
        if (!initialized) {
            this.map = HashMap()
            this.fileOutConfigList = ArrayList()
            injections.filterNotNull().forEach {
                it.flushList().forEach { param -> param.flushParameter(config) }
                it.outList().forEach { injection ->
                    this.map.putAll(injection.toInjectionMap())
                    this.fileOutConfigList.add(injection.toFileOutConfig())
                }
            }
            initialized = true
        }
    }


    /**
     * 需要输出的列表
     * */
    open fun outList(): MutableList<InjectionParameter> = ArrayList()

    /**
     * 需要基于外部config刷新的列表
     * */
    open fun flushList(): MutableList<InjectionParameter> = ArrayList()

}