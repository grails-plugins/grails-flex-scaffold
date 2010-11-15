/**
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import grails.util.Environment

includeTargets << grailsScript('_GrailsBootstrap')

pluginDirPath = flexScaffoldPluginDir.path

//Private scripts
includeTargets << new File("$pluginDirPath/scripts/_CreateFlexProperties.groovy")
includeTargets << new File("$pluginDirPath/scripts/_GenerateDefaults.groovy")
includeTargets << new File("$pluginDirPath/scripts/_GenerateStructure.groovy")
includeTargets << new File("$pluginDirPath/scripts/_ValidateDomainClass.groovy")

def configClass = new GroovyClassLoader(getClass().classLoader).parseClass(
	new File("$basedir/grails-app/conf/Config.groovy"))
def config = new ConfigSlurper(Environment.current.name).parse(configClass)
flexConfig = config.grails.plugin.flex

ant.property environment: 'env'
ant.property file: "$basedir/flex.properties"
ant.property file: "$pluginDirPath/scripts/flexScaffold.properties"

flexHome = ant.project.properties.'env.FLEX_HOME'
if (!flexHome) {
	flexHome = flexConfig.home
}

ant.property name: 'FLEX_HOME', value: flexHome

grailsHome = ant.project.properties.'environment.GRAILS_HOME'

target(generateCommon: 'Common dependencies') {
	depends(classpathPluginClasses, validateDomainClass, generateFlexDefaultStructure,
	        createFlexProperties, generateDefaults)

	DefaultFlexTemplateGenerator = loadClass('org.cubika.labs.scaffolding.generator.DefaultFlexTemplateGenerator')
	templateGenerator = DefaultFlexTemplateGenerator.newInstance(classLoader)
	
	BuildFormItemFactory = loadClass('org.cubika.labs.scaffolding.form.factory.BuildFormItemFactory')
	BuildFormItemFactory.setClassLoader classLoader

	String libDir = antProperty('lib.destdir')
	ant.mkdir dir: libDir
	ant.copy(toDir: libDir) {
		fileset dir: "$flexScaffoldPluginDir/src/flex/libs"
	}
}

target(classpathPluginClasses: 'Add plugin classes to classpath') {
	classLoader.addURL grailsSettings.pluginClassesDir.toURI().toURL()
}

loadClass = { String name -> classLoader.loadClass name }
antProperty = { String key -> ant.project.properties[key] }
