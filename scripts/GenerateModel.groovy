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

boolean addToModel = true

target(generateModel: 'Generate Domain Model for domainClass') {
	depends(generateCommon)

	doGenerateModel domainClass: getDomainClass(args.trim())
}

doGenerateModel = { Map args = [:] ->

	CVU = loadClass('org.cubika.labs.scaffolding.utils.ConstraintValueUtils')

	addToModel = args["addToModel"]

	def domainClass = args["domainClass"]

	String nameDir = antProperty('model.destdir') + "/$domainClass.propertyName"
	ant.mkdir dir: nameDir

	String classNameFile = "$nameDir/${domainClass.shortName}Model.as"
	String templateFile = pluginDirPath + antProperty('model.domainfile')
	templateGenerator.generateTemplate(domainClass, templateFile, classNameFile)
	println "$classNameFile Done!"
	addToModelLocator(addToModel, domainClass)

	if (CVU.isReportable(domainClass)) {
		classNameFile = "$nameDir/${domainClass.shortName}ReportingModel.as"
		templateFile = pluginDirPath + antProperty('model.reportingfile')
		templateGenerator.generateTemplate(domainClass, templateFile, classNameFile)
		addToModelLocator(addToModel, domainClass, "Reporting")
		println "$classNameFile Done!"
	}
}

private void addToModelLocator(addToModel, domainClass, prefix="") {
	if (!addToModel) {
		return
	}

	String modelName = antProperty('model.destdir') + "/ApplicationModelLocator.as"

	if (!new File(modelName).exists()) {
		ant.copy file: pluginDirPath + antProperty('model.locatorfile'),
		         tofile: modelName, verbose: true
	}

	file = ant.fileset(dir: antProperty('model.destdir')) {
		include(name: "ApplicationModelLocator.as")
		contains(text: "import model.${domainClass.propertyName}.$domainClass.shortName${prefix}Model;",
		         casesensitive: false)
	}

	if (file.size() > 0) {
		return
	}

	ant.replace file: modelName,
	            token: "/*IMPORTS*/",
	            value: "/*IMPORTS*/\n	" +
						"import model.${domainClass.propertyName}.$domainClass.shortName${prefix}Model;"

	ant.replace file: modelName,
	            token: "/*PROPERTIES*/",
	            value: "/*PROPERTIES*/\n		" +
						"private var _$domainClass.propertyName${prefix}Model:$domainClass.shortName${prefix}Model;"

	String getter = """/*GETTERS*/
		public function get $domainClass.propertyName${prefix}Model():$domainClass.shortName${prefix}Model {
			if (!_$domainClass.propertyName${prefix}Model) {
				_$domainClass.propertyName${prefix}Model = new $domainClass.shortName${prefix}Model();
			}
			return _$domainClass.propertyName${prefix}Model;
		}"""

	ant.replace file: modelName, token: "/*GETTERS*/", value: getter
}

setDefaultTarget 'generateModel'
