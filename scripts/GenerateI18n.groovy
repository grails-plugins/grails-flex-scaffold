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

target(generateI18n: "Generates i18n messages for domain classes") {
	depends(generateCommon)

	def name = argsMap["params"][0]
	args = [name: name, runHierarchy: false]

	doGenerateI18n args
}

doGenerateI18n = { Map args = [:] ->

	loadI18nProperties()

	mapI18nGenerated = [:]

	defaultLocale = antProperty('locale.default')
	I18nBuilder = loadClass('org.cubika.labs.scaffolding.i18n.I18nBuilder')
	i18nBuilder = I18nBuilder.newInstance(defaultLocale)

	def locales = antProperty('locale.locales')

	if (args['name'] != "*") {
		//If runHierarchy is true, comes from GenerateAllFlex and we don't want generate all locales
		if (args['runHierarchy']) {
			locales = "$defaultLocale"
		}
		else {
			locales = "$defaultLocale, $locales"
		}

		generateI18nSingle(getDomainClass(args['name']), locales, args['runHierarchy'])
	}
	else if (args['name'] == "*") {
		locales = "$defaultLocale, $locales"
		generateI18nAll grailsApp.domainClasses, locales
	}
}

/**
 * Generates properties
 * @param domanClass
 * @param runHierarchy - if it's true, run all classes related with DomainClass.
 */
void generateI18nSingle(domainClass, locales, runHierarchy) {
	locales.split(",").each {
		print "Building $it:"
		i18nBuilder.changeLocale(it.trim())
		generateProperties domainClass, runHierarchy
		//Save message{suffix}.properties
		i18nBuilder.store()
		mapI18nGenerated = [:]
		println "\nLocale $it Done!"
	}
}

/**
 * Generates all classes properties
 * @param domanClass
 */
void generateI18nAll(domainClasses, locales) {
	locales.split(",").each {
		print "Building $it:"
		i18nBuilder.changeLocale(it.trim())
		domainClasses.each { generateProperties(it, false) }
		//Save message{suffix}.properties
		i18nBuilder.store()
		println "\nLocale $it Done!"
	}
}

void generateProperties(domainClass, runHierarchy = true) {
	if (!domainClass) {
		return
	}

	print " $domainClass.shortName"
	i18nBuilder.build(domainClass)

	if (runHierarchy) {
		mapI18nGenerated[domainClass] = domainClass
		generateHierarchy domainClass
	}
}

void generateHierarchy(domainClass) {
	domainClass.properties.each {
		if (it.isOneToOne() || it.isOneToMany() || it.isManyToOne()) {
			if (!mapI18nGenerated.containsKey(it.referencedDomainClass)) {
				generateProperties it.referencedDomainClass
			}
		}
	}
}

loadI18nProperties = { ->
	String name = "$basedir/grails-app/i18n/i18n.properties"
	if (!new File(name).exists()) {
		// should have been created when installing, but we can re-copy
		ant.copy file: "$pluginDirPath/grails-app/i18n/i18n.properties", tofile: name
	}
	ant.property file: name
}

setDefaultTarget 'generateI18n'
