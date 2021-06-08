package com.tuanzili.generator.config

/**
 * Created by Panda on 2018/8/14
 */
interface MybatisPlusAssembler<T> {

    fun toMybatisPlusConfig(): T

}