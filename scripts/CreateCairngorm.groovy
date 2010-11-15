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

import org.codehaus.groovy.grails.commons.GrailsClassUtils as GCU

includeTargets << new File("$flexScaffoldPluginDir/scripts/_FlexScaffoldCommon.groovy")

target(createCairngorm: "Creates Cairngorm's event and command") {
	depends(checkVersion, packageApp)

	Map map = [:]
	String pkg
	String artifactName

	loadApp()

	args = args.trim()

	int lastIndex = args.lastIndexOf(".")

	if (lastIndex > -1) {
		pkg = args[0..lastIndex-1]
		artifactName = args[lastIndex + 1..-1]
		pkg = ".$pkg"
	}
	else {
		pkg = "."
		artifactName = args.replace(".", "")
	}

	createAll artifactName, pkg

	println "Create Cairngorm Command and Event Done!"
}

void createAll(artifactName, pkg) {
	String destPath = getDestPath(pkg)
	createEvent destPath, artifactName, pkg
	createCommand destPath, artifactName, pkg
}

void createCommand(destPath, artifactName, pkg) {
	String templateFile = pluginDirPath + antProperty('command.cairngorm')
	String className = GCU.getClassNameRepresentation(artifactName)

	destPath = antProperty('flex.srcdir') + "/command$destPath"
	ant.mkdir dir: destPath

	templateGenerator.generateSimpleTemplate templateFile, "$destPath/${className}Command.as",
		artifactName, pkg, className
}

void createEvent(destPath, artifactName, pkg) {
	String templateFile = pluginDirPath + antProperty('event.cairngorm')
	String className = GCU.getClassNameRepresentation(artifactName)

	destPath = antProperty('flex.srcdir') + "/event$destPath"
	ant.mkdir dir: destPath

	templateGenerator.generateSimpleTemplate(templateFile, "$destPath/${className}Event.as",
		artifactName, pkg, className)
}

String getDestPath(pkg) { pkg.size() > 1 ? "/" + pkg.replace(".", "/") : "/" }

setDefaultTarget 'createCairngorm'
