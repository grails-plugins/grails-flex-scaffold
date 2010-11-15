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
 * @author Gonzalo Javier Clavell
 */

import grails.util.GrailsNameUtils as GNU

includeTargets << grailsScript("_GrailsCreateArtifacts")

target(validateDomainClass: "Validate domain class name") {
	depends(checkVersion, configureProxy, parseArguments, classpath, loadApp)

	promptForName type: "Domain Class"

	FSU = loadClass('org.cubika.labs.scaffolding.utils.FlexScaffoldingUtils')

	//Adds Grails Application to FlexScaffoldUtils
	FSU.grailsApplication = grailsApp
	try {
		String name = argsMap["params"][0]
		if (!name || name == "*") {
			return
		}
		if (name && !getDomainClass(name)) {
			println "\nError: Domain Class '$name' doesn't exist\n"
			exit 1
		}
	}
	catch(e) {
		logError("Error running validate domain class", e)
		exit(1)
	}
}

// @return DomainClass representation if exist otherwise null
getDomainClass = { String name ->
	name = name.indexOf('.') > -1 ? name : GNU.getClassNameRepresentation(name)
	grailsApp.getDomainClass(name)
}
