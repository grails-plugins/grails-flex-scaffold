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

includeTargets << new File("$flexScaffoldPluginDir/scripts/_FlexScaffoldCommon.groovy")

target(generateEvent: 'Generate Event') {
	depends(generateCommon)

	doGenerateEvent(domainClass: getDomainClass(args))
}

doGenerateEvent = { Map args = [:] ->

	CVU = loadClass('org.cubika.labs.scaffolding.utils.ConstraintValueUtils')

	def domainClass = args["domainClass"]

	String nameDir = antProperty('event.destdir') + "/$domainClass.propertyName"
	ant.mkdir dir: nameDir

	String classNameFile = "$nameDir/${domainClass.shortName}CRUDEvent.as"
	String templateFile = pluginDirPath + antProperty('event.crudfile')
	generateEvent domainClass, templateFile, classNameFile

	classNameFile = "$nameDir/${domainClass.shortName}GetPaginationEvent.as"
	templateFile = pluginDirPath + antProperty('event.paginationfile')
	generateEvent domainClass, templateFile, classNameFile

	classNameFile = "$nameDir/${domainClass.shortName}ExternalGetPaginationEvent.as"
	templateFile = pluginDirPath + antProperty('event.paginationfile')
	generateEvent domainClass, templateFile, classNameFile, "External"

	//generate Event Actions
	CVU.actions(domainClass).each {
		classNameFile = "$nameDir/$domainClass.shortName${it}Event.as"
		templateFile = pluginDirPath + antProperty('event.actionevent')
		templateGenerator.generateTemplate domainClass, templateFile, classNameFile, it
	}
	//end generate Event Actions
}

private void generateEvent(domainClass, templateFile, classNameFile, typeName = "") {
	templateGenerator.generateTemplate domainClass, templateFile, classNameFile, typeName
	println "$classNameFile Done!"
}

setDefaultTarget 'generateEvent'
