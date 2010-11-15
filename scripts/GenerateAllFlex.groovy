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

/**
 * @author Ezequiel Martin Apfel
 */

includeTargets << new File("$flexScaffoldPluginDir/scripts/_FlexScaffoldCommon.groovy")

//Public scripts
includeTargets << new File("$pluginDirPath/scripts/GenerateVo.groovy")
includeTargets << new File("$pluginDirPath/scripts/GenerateView.groovy")
includeTargets << new File("$pluginDirPath/scripts/GenerateEvent.groovy")
includeTargets << new File("$pluginDirPath/scripts/GenerateModel.groovy")
includeTargets << new File("$pluginDirPath/scripts/GenerateCommand.groovy")
includeTargets << new File("$pluginDirPath/scripts/GenerateDelegate.groovy")
includeTargets << new File("$pluginDirPath/scripts/GenerateService.groovy")
includeTargets << new File("$pluginDirPath/scripts/GenerateI18n.groovy")

target(generateAllFlex: "Generates all artifacts for the specified domain class(es)") {
	depends(generateCommon)

	I18nUtils = loadClass('org.cubika.labs.scaffolding.utils.I18nUtils')
	CVU = loadClass('org.cubika.labs.scaffolding.utils.ConstraintValueUtils')

	mapGenerated = [:]

	generateAllFlex(args.trim())
}

void generateAllFlex(String name) {
	def domainClasses = []
	//If use generate-all-flex domainClass
	boolean generate = false

	if (name == "*") {
		domainClasses = grailsApp.domainClasses
	}
	else {
		//ValidateDomainClass method
		domainClasses << getDomainClass(name)
		generate = true
	}

	domainClasses.each {
		if (generate || CVU.generate(it)) {
			println "Generate Scaffold for $it.propertyName\n"

			doGenerateVo domainClass: it
			doGenerateView domainClass: it
			doGenerateEvent domainClass: it
			doGenerateCommand domainClass: it
			doGenerateModel domainClass: it, addToModel: true
			doGenerateDelegate domainClass: it
			doGenerateService domainClass: it
			doGenerateI18n name: it.fullName, runHierarchy: true
			addToNavigationModel it
			addToMain it
			addToMainLocale it

			println "-------------------------------------"
		}
	}

	println "Generate Done!"
}

void addToNavigationModel(domainClass) {
	if (!new File(antProperty('model.destfile')).exists()) {
		ant.copy file: pluginDirPath + antProperty('model.navigationfile'),
		         tofile: antProperty('model.destfile'), verbose: true
	}

	file = ant.fileset(dir: antProperty('model.destdir')) {
		include name: antProperty('model.navigation')
		contains text: "\"$domainClass.propertyName\"", casesensitive: false
	}

	if (file.size() > 0) {
		return
	}

	String propertyName = domainClass.propertyName
	String groupName = CVU.groupName(domainClass)

	if (groupName) {
		groupName = groupName.replaceAll(" ", "")
		groupName = "${groupName.toLowerCase()}GroupView#"
		println "Setting groupName " + groupName
	}
	else {
		groupName = ""
	}

	ant.replace file: antProperty('model.destfile'),
	            token: "//DefaultNavigationMap - Not Remove",
	            value: "//DefaultNavigationMap - Not Remove\n	" +
						"		defaultNavigationMap[\"$propertyName\"] = " +
						"{name:\"$groupName${propertyName}CRUDView\",list:\"$groupName${propertyName}List\"," +
						"edit:\"$groupName${propertyName}Edit\",nav:{list:\"edit\",edit:\"list\"}};"
}

void addToMain(domainClass) {
	ant.copy file: pluginDirPath + antProperty('main.file'),
	         tofile: antProperty('main.destdir'), verbose: true

	ant.copy file: pluginDirPath + antProperty('view.login'),
	         tofile: antProperty('login.destdir'), verbose: true

	ant.copy file: pluginDirPath + antProperty('view.principal'),
	         tofile: antProperty('principal.destdir'), verbose: true

	String groupName = CVU.groupName(domainClass)
	String tabName // name to be showed in the upper tab
	String propertyName
	String name

	if (!groupName) {
		groupName = "${domainClass.shortName}CRUDView"
		propertyName = domainClass.propertyName
		name = "${propertyName}CRUDView"
		tabName = propertyName
	}
	else {
		groupName = groupName.replaceAll(" ", "")
		propertyName = groupName.toLowerCase()
		groupName = "${groupName}GroupView"
		name = "${propertyName}GroupView"
		tabName = CVU.groupName(domainClass)
	}

	file =  ant.fileset(dir: antProperty('view.destdir')) {
		include name: "PrincipalView.mxml"
		contains text: "<view${groupName}:$groupName", casesensitive: false
	}

	if (!(file.size() > 0)) {
		ant.replace file: antProperty('principal.destdir'),
		            token: "><!--NS-->",
		            value: "\n	xmlns:view${groupName}=\"view.${propertyName}.*\"><!--NS-->"

		ant.replace file: antProperty('principal.destdir'),
		            token: "<!--CRUDVIEWS-->",
		            value: "<!--CRUDVIEWS-->\n		" +
							"<view${groupName}:$groupName height=\"100%\" " +
							"label=\"{MultipleRM.getString(MultipleRM.localePrefix,'${propertyName}.label')}\" name=\"$name\"/>"
	}

	ant.replaceregexp file: antProperty('principal.destdir'),
	                  match: ".*Resource.*\n*",
	                  replace: "@metadata@"

	ant.replaceregexp file: antProperty('principal.destdir'),
	                  match: "@metadata@",
	                  replace: "${I18nUtils.getMetaTags()}"

	ant.replaceregexp file: antProperty('principal.destdir'),
	                  match: "private var localesCollection:ArrayCollection.*",
	                  replace: "${I18nUtils.getLocalesCollection()}", byline: "false", flags: "m"
}

void addToMainLocale(domainClass) {
	String stringFile = new File(antProperty('principal.destdir')).text

	stringFile = stringFile.replaceAll(".*Resource.*\n*", "##")
	stringFile = stringFile.replaceAll("##.*#", I18nUtils.getMetaTags())
	stringFile = stringFile.replaceAll(
		"private var localesCollection:ArrayCollection.*",
		"${I18nUtils.getLocalesCollection()}")

	def writer = new File(antProperty('principal.destdir')).newWriter()
	writer << stringFile
	writer.flush()
	writer.close()
}

setDefaultTarget 'generateAllFlex'
