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

target(generateCommand: 'Generate CRUD Commands') {
	depends(generateCommon)

	doGenerateCommand(domainClass: getDomainClass(args))
}

doGenerateCommand = { Map args = [:] ->

	CVU = loadClass('org.cubika.labs.scaffolding.utils.ConstraintValueUtils')

	def domainClass = args["domainClass"]

	String nameDir = antProperty('command.destdir') + "/$domainClass.propertyName"
	ant.mkdir dir: nameDir

	String classNameFile = "$nameDir/${domainClass.shortName}CreateCommand.as"
	String templateFile = pluginDirPath + antProperty('command.createfile')
	generateCommand domainClass, templateFile, classNameFile

	classNameFile = "$nameDir/${domainClass.shortName}DeleteCommand.as"
	templateFile = pluginDirPath + antProperty('command.deletefile')
	generateCommand domainClass, templateFile, classNameFile

	classNameFile = "$nameDir/${domainClass.shortName}ListCommand.as"
	templateFile = pluginDirPath + antProperty('command.listfile')
	generateCommand domainClass, templateFile, classNameFile

	classNameFile = "$nameDir/${domainClass.shortName}GetPaginationListCommand.as"
	templateFile = pluginDirPath + antProperty('command.paginationlistfile')
	generateCommand domainClass, templateFile, classNameFile

	classNameFile = "$nameDir/${domainClass.shortName}ExternalGetPaginationListCommand.as"
	templateFile = pluginDirPath + antProperty('command.paginationlistfile')
	generateCommand(domainClass, templateFile, classNameFile, "External")

	classNameFile = "$nameDir/${domainClass.shortName}SaveOrUpdateCommand.as"
	templateFile = pluginDirPath + antProperty('command.saveorupdatefile')
	generateCommand domainClass, templateFile, classNameFile

	classNameFile = "$nameDir/${domainClass.shortName}SelectCommand.as"
	templateFile = pluginDirPath + antProperty('command.selectfile')
	generateCommand domainClass, templateFile, classNameFile

	classNameFile = "$nameDir/${domainClass.shortName}CancelCommand.as"
	templateFile = pluginDirPath + antProperty('command.cancelfile')
	generateCommand domainClass, templateFile, classNameFile

	//generate Command Actions
	CVU.actions(domainClass).each {
		classNameFile = "$nameDir/$domainClass.shortName${it}Command.as"
		templateFile = pluginDirPath + antProperty('command.actioncommand')
		templateGenerator.generateTemplate domainClass, templateFile, classNameFile, it
	}
	//end generate Command Actions
}

private void generateCommand(domainClass, templateFile, classNameFile, typeName = "") {
	templateGenerator.generateTemplate domainClass, templateFile, classNameFile, typeName
	println "$classNameFile Done!"
}

setDefaultTarget 'generateCommand'
