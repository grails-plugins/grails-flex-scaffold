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

target(generateView: 'Generate CRUDs views') {
	depends(generateCommon)

	doGenerateView(domainClass: getDomainClass(args))
}

doGenerateView = { Map args = [:] ->

	CVU = loadClass('org.cubika.labs.scaffolding.utils.ConstraintValueUtils')

	def domainClass = args["domainClass"]

	String nameDir = antProperty('view.destdir') + "/$domainClass.propertyName"
	ant.mkdir dir: nameDir

	String classNameFile = "$nameDir/${domainClass.shortName}ListView.mxml"
	String templateFile = pluginDirPath + antProperty('view.listfile')
	generateView domainClass, templateFile, classNameFile

	classNameFile = "$nameDir/${domainClass.shortName}EditView.mxml"
	templateFile = pluginDirPath + antProperty('view.editfile')
	generateView domainClass, templateFile, classNameFile

	classNameFile = "$nameDir/${domainClass.shortName}CRUDView.mxml"
	templateFile = pluginDirPath + antProperty('view.crudfile')
	generateView domainClass, templateFile, classNameFile

	//generate ItemRenderers Actions
	CVU.actions(domainClass).each {
		ant.mkdir dir: "$nameDir/renderers"

		classNameFile = "$nameDir/renderers/$domainClass.shortName${it}ItemRenderer.mxml"
		templateFile = pluginDirPath + antProperty('view.actionitemrenderer')
		templateGenerator.generateTemplate domainClass, templateFile, classNameFile, it
	}
	//end generate ItemRenderers Actions

	//generate ReportingView
	if (CVU.isReportable(domainClass)) {
		classNameFile = "$nameDir/${domainClass.shortName}ReportingView.mxml"
		templateFile = pluginDirPath + antProperty('view.reporting')
		generateView domainClass, templateFile, classNameFile

		classNameFile = "$basedir/grails-app/controllers/GfsDjReportController.groovy"
		if (!new File(classNameFile).exists()) {
			templateFile = pluginDirPath + antProperty('controller.reporting')
			ant.copy file: templateFile, tofile: classNameFile
		}
	}
	//end generate reporting view

	//GROUP VIEW
	addToGroup(domainClass)
	//GROUP VIEW END
}

private void addToGroup(domainClass) {
	String groupName = CVU.groupName(domainClass)
	if (!groupName) {
		return
	}

	groupName = groupName.replaceAll(" ", "")
	String nameDir = antProperty('view.destdir') + "/${groupName.toLowerCase()}"
	String classNameFile = "$nameDir/${groupName}GroupView.mxml"
	String templateFile

	ant.mkdir dir: nameDir

	if (!(new File(classNameFile).exists())) {
		templateFile = pluginDirPath + antProperty('view.groupfile')
		generateView domainClass, templateFile, classNameFile
	}

	file = ant.fileset(dir: nameDir) {
		include(name: "${groupName}GroupView.mxml")
		contains(text: "${domainClass.shortName}CRUDView")
	}

	if (!(file.size() > 0)) {
		ant.replace(file: classNameFile,
		            token: "><!--NS-->",
		            value: """\n	xmlns:view${domainClass.shortName}="view.${domainClass.propertyName}.*"><!--NS-->""")
		ant.replace(file: classNameFile,
		            token: "<!--CRUDVIEWS-->",
		            value: """<!--CRUDVIEWS-->
		<view${domainClass.shortName}:${domainClass.shortName}CRUDView height="100%" 
			label="{MultipleRM.getString(MultipleRM.localePrefix,'${domainClass.propertyName}.label')}"
			name="${domainClass.propertyName}CRUDView"/>""")
	}
}

private void generateView(domainClass, templateFile, classNameFile) {
	templateGenerator.generateTemplate domainClass, templateFile, classNameFile
	println "$classNameFile Done!"
}

setDefaultTarget 'generateView'
