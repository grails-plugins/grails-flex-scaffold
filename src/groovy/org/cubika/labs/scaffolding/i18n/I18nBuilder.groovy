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
package org.cubika.labs.scaffolding.i18n

import grails.util.Environment
import grails.util.GrailsUtil

import groovy.util.ConfigSlurper

import org.apache.log4j.Logger

import org.cubika.labs.scaffolding.utils.ConstraintValueUtils as CVU
import org.cubika.labs.scaffolding.utils.FlexScaffoldingUtils as FSU
import org.cubika.labs.scaffolding.utils.I18nUtils
import org.cubika.labs.scaffolding.utils.UTFProperties

/**
 * Generates message[_locale].properties of i18n
 * @author Ezequiel Martin Apfel
 */
class I18nBuilder {

	private final String PROPERTY_SEPARATOR = ":"
	private final String END_LINE_PROPERTIES = ":)"

	private final Logger log = Logger.getLogger(getClass())

	//Domain Class
	def domainClass
	//Locale selected
	def locale
	//Default local (set into i18n.properties)
	def defaultLocale

	//DefaultMessage - default-message[_locale].properties
	private defaultMessage
	//Properties' map loaded
	private Map mapProperties = [:]

	I18nBuilder(defaultLocale) {
		this.defaultLocale = defaultLocale
	}

	/**
	 * Changes locale, fills message and properties for a new locale
	 * @param locale - String (en, es, .., etc)
	 */
	void changeLocale(String locale) {
		this.locale = locale
		fillDefaultMessages()
		fillProperties()
	}

	/**
	 * Builds properties' map, then It's stored in the .properites file
	 * @param domainClass - a DomainClass
	 */
	void build(domainClass) {

		if (!locale) {
			throw new Exception("Locale must be set. Use i18nBuilder.changeLocale(locale) to generated message")
		}

		this.domainClass = domainClass

		mapProperties.putAll getDefaultLocaleMessage()
		mapProperties.putAll translatedDomainProperties()
	}

	/**
	 * Save properties' map in .properties
	 */
	void store() {
		File out = new File(getFilePath())
		String oldPrefix = ""

		def writer = out.newWriter("UTF-8")

		new TreeMap(mapProperties).each {
			//no anda me funciono el split, por eso indexOf
			def prefix = it.key.indexOf(".")

			if (prefix > -1) {
				prefix = it.key.substring(0,prefix)

				if (oldPrefix == prefix) {
					writer << "${it}\n"
				}
				else {
					writer << "#############################\n"
					writer << "#$prefix\n"
					writer << "${it}\n"
					oldPrefix = prefix
				}
			}
		}

		writer.flush()
		writer.close()

		clear()
	}

	/**
	 * Gets defaultMessage for the locale, if not exists gets the message's file
	 * for the default locale and translate it via google
	 */
	def getDefaultLocaleMessage() {
		def defaultTranslate = ""
		if (!defaultMessage) {
			fillDefaultMessages(false)
			defaultTranslate = translate(defaultMessage)
		}

		if (defaultTranslate == "") {
			defaultMessage.each { defaultTranslate += it }
		}

		defaultTranslate = unmarshallGoogle(defaultTranslate)

		replaceDomainClassProperty(defaultTranslate)
	}

	/**
	 * Translates DomainClass' properties if isn't a defaultLocale via google
	 */
	private translatedDomainProperties() {
		//Armo el mapa para mandar a google, solo con las propiedades que no tiene i18n:[] declarado
		//en la constraint para el locale que esto traduciendo.
		//las que no las pongo en otro array
		def propertiesToTranslate = []
		String propertiesNoTranslate = ""

		FSU.getPropertiesWithoutIdentity(domainClass).each {
			def constraint = it.domainClass.constrainedProperties[it.name]
			//If sets locale in static constriants = { aProperties(i18n:[]) }
			def messageProperty = CVU.i18n(it,locale)

			if (messageProperty) { //valido las constraint
				propertiesNoTranslate += marshallProperty(it,messageProperty)
				if (constraint?.inList) {
					constraint.inList.each { item ->
						propertiesToTranslate.add("${it.domainClass.propertyName}.${it.name}.${item}$PROPERTY_SEPARATOR${item}$END_LINE_PROPERTIES")
					}
				}
			}
			else {
				propertiesToTranslate.add(marshallProperty(it))
				if (constraint?.inList) {
					constraint.inList.each { item ->
						propertiesToTranslate.add("${it.domainClass.propertyName}.${it.name}.${item}$PROPERTY_SEPARATOR${item}$END_LINE_PROPERTIES")
					}
				}

				CVU.actions(domainClass).each {
					propertiesToTranslate.add("${domainClass.propertyName}.${it.toLowerCase()}$PROPERTY_SEPARATOR${it}$END_LINE_PROPERTIES")
				}
			}
		}

		String groupName = CVU.groupName(domainClass)
		if (groupName) {
			propertiesToTranslate.add("${groupName.toLowerCase().replaceAll(" ","")}.label$PROPERTY_SEPARATOR${groupName}$END_LINE_PROPERTIES")
		}

		String propertiesTranslate = ""
		if (defaultLocale != locale) {
			propertiesTranslate = translate(propertiesToTranslate)
		}
		else {
			propertiesToTranslate.each { propertiesTranslate += it }
		}

		propertiesTranslate += propertiesNoTranslate

		unmarshallGoogle(propertiesTranslate)
	}

	/**
	 * Fills default message
	 */
	private fillDefaultMessages(boolean existMessage = true) {
		String suffix
		if (existMessage) {
			suffix = I18nUtils.suffix(locale)
		}
		else {
			suffix = I18nUtils.suffix(defaultLocale)
		}

		String filePath = I18nUtils.resolveResourcesPath("default-messages${suffix}")

		if (filePath) {
			if (new File(filePath).exists()) {
				def props = new UTFProperties()

				props.load(new File(filePath))
				defaultMessage = marshallMap(new ConfigSlurper(Environment.current.name).parse(props).flatten())
			}
		}
		else {
			print " Default message doesn't exists for $locale, GFS will use default messages of $defaultLocale in order to translate it "
		}
	}

	/**
	 * Fills properties of grails-app/i18n/message[_locale].properties
	 */
	private void fillProperties() {
		String filePath = getFilePath()

		if (new File(filePath).exists()) {
			def props = new UTFProperties()

			props.load(new File(filePath))

			mapProperties = new ConfigSlurper(Environment.current.name).parse(props).flatten()
		}
		else {
			mapProperties = [:]
		}
	}

	/**
	 * Marshalls property that will be sent to google
	 * @param properties - DomainClassProperites
	 * @param message - String representing the value of property
	 */
	private String marshallProperty(property, String message) {
		String key = "${property.domainClass.propertyName}.${property.name}"
		String value = "${message}"

		"11${key}11$PROPERTY_SEPARATOR${value}$END_LINE_PROPERTIES"
	}

	/**
	 * Marshalls property that will be sent to google
	 * @param properties - DomainClassProperites
	 */
	private String marshallProperty(property) {
		String key = "${property.domainClass.propertyName}.${property.name}"
		String value = "${property.naturalName}"

		"11${key}11$PROPERTY_SEPARATOR${value}$END_LINE_PROPERTIES"
	}

	/**
	 * Marshalls properties' map
	 * @param map - Map
	 */
	private List marshallMap(Map map) {
		List aux = []
		map.each {
			aux.add("11${it.key}11$PROPERTY_SEPARATOR${it.value}$END_LINE_PROPERTIES")
		}
		aux
	}

	/**
	 * Unmarshalls message recived from google translator
	 * @param properites - String of properties translated
	 */
	private Map unmarshallGoogle(properties) {

		Map unmarshalProperties = [:]

		String endLinePattern = ":[)]+"
		String separatorPattern = PROPERTY_SEPARATOR

		String rightBracket = ")"
		String leftBracket = "("

		properties = properties.replace("）",")")
		properties = properties.replace("：",":")
		properties = properties.replace("：）",":)")
		properties = properties.replace("： ）",":)")
		properties = properties.replace("：  ）",":)")
		properties = properties.replace("}",")")
		properties = properties.replace(":}",":)")
		properties = properties.replace(": }",":)")
		properties = properties.replace(": )",":)")

		properties.split(endLinePattern).each {
			if (it) {
				it = it.replace(leftBracket, "{")
				it = it.replace(rightBracket, "}")

				it = it.trim()

				def keyValue = it.split(separatorPattern)
				if (keyValue.size() == 2) {
					unmarshalProperties.put(keyValue[0].trim().replaceAll("11","").replaceAll(" ",""),keyValue[1].trim())
				}
			}
		}

		unmarshalProperties
	}

	/**
	 * Translates via google translator
	 * @param message - String[] will be sent to google to translate
	 */
	private String translate(message) {
		String result = ""
		String bufferMessage = ""
		int j = 9
		try {
			message.eachWithIndex { it, i ->

				if (i <= j) {
					bufferMessage += it
				}
				else {
					bufferMessage += it
					result += I18nUtils.getTranslateWord(bufferMessage, defaultLocale, locale)
					bufferMessage = ""
					j += 9
				}
			}
			if (bufferMessage != "") {
				result += I18nUtils.getTranslateWord(bufferMessage, defaultLocale, locale)
			}
		}
		catch(e) {
			log.error "Problem with Google Translate: $e.message", GrailsUtil.sanitize(e)
		}

		result
	}

	/**
	 * Replace tag domainClass of the properites' file
	 * @param message - String
	 */
	private Map replaceDomainClassProperty(message) {
		Map aux = [:]

		String domainClassToTranslate = domainClass.naturalName
		String domainClassTranslated

		//Start workaround bug GRAILS-4164 open
		String pkg = domainClass.packageName

		if (pkg) {
			pkg = pkg.replace('.', '')
			pkg = pkg[0].toUpperCase() + pkg[1..-1] + " "
			domainClassToTranslate = domainClassToTranslate.replace(pkg,"")
		}
		domainClassTranslated = domainClassToTranslate
		//End workaround bug GRAILS-4164 open

		if (defaultLocale != locale) {
			domainClassTranslated = translate(domainClassToTranslate)
		}

		if (domainClassTranslated == "") {
			domainClassTranslated = domainClassToTranslate
		}

		message.each {
			if (it.key && it.value) {
				aux.put(it.key.replace("domainClass", domainClass.propertyName),
				        it.value.replace("domainClass", domainClassTranslated))
			}
		}

		aux
	}

	/**
	 * Gets file path for .properties
	 */
	private String getFilePath() {
		if (locale == "en") {
			return "grails-app/i18n/messages.properties"
		}
		return "grails-app/i18n/messages_${locale}.properties"
	}

	/**
	 * Clears default message
	 */
	private void clear() {
		defaultMessage = null
	}
}
