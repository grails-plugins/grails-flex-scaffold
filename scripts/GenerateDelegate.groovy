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

target(generateDelegate: 'Generate Domain Delegate') {
	depends(generateCommon)

	doGenerateDelegate(domainClass: getDomainClass(args))
}

doGenerateDelegate = { Map args = [:] ->

	def domainClass = args["domainClass"]

	String nameDir = antProperty('service.destdir') + "/$domainClass.propertyName"
	ant.mkdir dir: nameDir

	String classNameFile = "$nameDir/${domainClass.shortName}BusinessDelegate.as"
	String templateFile = pluginDirPath + antProperty('delegate.domainfile')
	templateGenerator.generateTemplate(domainClass, templateFile, classNameFile)
	println "$classNameFile Done!"
}

setDefaultTarget 'generateDelegate'
