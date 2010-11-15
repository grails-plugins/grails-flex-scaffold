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

generateDefaults = { Map args = [:] ->
	generateNavigation()
	generateLocales()
	generateBusinessDelegate()
	generateController()
	generateLogin()
}

void generateNavigation() {
	String nameDir = antProperty('event.destdir')
	String className = "$nameDir/DefaultNavigationEvent.as"
	String template = pluginDirPath + antProperty('event.defaultnavigationfile')

	ant.copy file: template, tofile: className
	println "$className Done!"

	nameDir = antProperty('command.destdir')
	className = "$nameDir/DefaultNavigationCommand.as"
	template = pluginDirPath + antProperty('command.defaultnavigationfile')

	ant.copy file: template, tofile: className
	println "$className Done!"

	nameDir = antProperty('event.destdir')
	className = "$nameDir/AlternativeNavigationEvent.as"
	template = pluginDirPath + antProperty('event.alternatenavigationfile')

	ant.copy file: template, tofile: className
	println "$className Done!"

	nameDir = antProperty('command.destdir')
	className = "$nameDir/AlternativeNavigationCommand.as"
	template = pluginDirPath + antProperty('command.alternatenavigationfile')

	ant.copy file: template, tofile: className
	println "$className Done!"

	nameDir = antProperty('command.destdir')
	ant.mkdir dir: "$nameDir/gfs"

	className = "$nameDir/gfs/AbstractNavigationCommand.as"
	template = pluginDirPath + antProperty('command.abstractnavigationfile')

	ant.copy file: template, tofile: className

	println "$className Done!"
}

void generateLocales() {
	String nameDir = antProperty('event.destdir')
	String className = "$nameDir/LocaleEvent.as"
	String template = pluginDirPath + antProperty('event.localefile')

	ant.copy file: template, tofile: className
	println "$className Done!"

	nameDir = antProperty('command.destdir')
	className = "$nameDir/LocaleCommand.as"
	template = pluginDirPath + antProperty('command.localefile')

	ant.copy file: template, tofile: className
	println "$className Done!"
}

void generateBusinessDelegate() {
	String nameDir = antProperty('service.destdir')
	String className = "$nameDir/BusinessDelegate.as"

	if (new File(className).exists()) {
		return
	}

	String template = pluginDirPath + antProperty('delegate.businessfile')
	ant.copy file: template, tofile: className
	println "$className Done!"
}

void generateController() {
	String nameDir = antProperty('controller.destdir')
	String className = "$nameDir/ApplicationController.as"
	String template = pluginDirPath + antProperty('controller.applicationfile')

	if (new File(className).exists()) {
		return
	}

	ant.copy file: template, tofile: className
	println "$className Done!"
}

void generateLogin() {
	//Copy LoginEvent
	String nameDir = antProperty('event.destdir')
	String className = "$nameDir/gfs/LoginEvent.as"
	String template = pluginDirPath + antProperty('event.login')

	ant.mkdir dir: "$nameDir/gfs"

	if (!new File(className).exists()) {
		ant.copy file: template, tofile: className
		println "$className Done!"
	}

	//Copy LoginCommand
	nameDir = antProperty('command.destdir')
	className = "$nameDir/gfs/LoginCommand.as"
	template = pluginDirPath + antProperty('command.login')

	if (!new File(className).exists()) {
		ant.copy file: template, tofile: className
		println "$className Done!"
	}
}
