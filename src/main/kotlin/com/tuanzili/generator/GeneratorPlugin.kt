package com.tuanzili.generator

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by Panda on 2018/8/5
 */
class GeneratorPlugin : Plugin<Project> {
    override fun apply(project: Project) {
//        println(project.project)
        project.tasks.create("generator", Generator::class.java)
    }
}