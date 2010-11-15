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
package org.cubika.labs.scaffolding.utils

import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClassProperty
import org.cubika.labs.scaffolding.annotation.FlexScaffoldProperty
import org.cubika.labs.scaffolding.form.FormItemConstants as FIC
import org.codehaus.groovy.grails.commons.GrailsClassUtils

/**
 * ConstraintValueUtils is used to give support constraint values or metaconstraint
 * values declared into DomainClass
 *
 * @author Ezequiel Martin Apfel
 */
class ConstraintValueUtils {
	/**
	 * Gets metaConstraint inPlace
	 * @param constraint
	 * @return Boolean - if inPlace is setted return value (true or false). Otherwise
	 * 						return default value false
	 */
	static boolean getInPlace(constraint) {
		def inPlace = constraint?.getMetaConstraintValue(FIC.IN_PLACE)
		if (inPlace == null) {
			inPlace = true
		}

		inPlace
	}

	/**
	 * Gets metaConstraint createView
	 * @param constraint
	 * @return Boolean - if createView is setted return value (true or false). Otherwise
	 * 						return default value false
	 */
	static boolean getCreateView(constraint) {
		def createView = constraint?.getMetaConstraintValue("createView")
		if (createView == null) {
			createView = true
		}

		createView
	}

	/**
	 * Gets metaConstraint editView
	 * @param constraint
	 * @return Boolean - if editView is setted return value (true or false). Otherwise
	 * 						return default value false
	 */
	static boolean getEditView(constraint) {
		def editView = constraint?.getMetaConstraintValue("editView")
		if (editView == null) {
			editView = true
		}

		editView
	}

	static String getLabeledProperty(domainClass) {
		String labeledProperty = getLabelField(domainClass)

		if (labeledProperty != "") {
			labeledProperty = domainClass.propertyName +"."+ labeledProperty
		}

		labeledProperty
	}

	/**
	 * Gets annotation labelField
	 * @param domainClass 	- DefaultGrailsDomainClass
	 * @return String 		- if labelField is setted return value otherwise blank String
	 */
	static String getLabelField(domainClass) {
		def annotation = domainClass.clazz.getAnnotation(FlexScaffoldProperty)
		String result = ""

		if (annotation != null) {
			result = annotation.labelField()
		}

		result
	}

	/**
	 * Gets constraint displayName
	 * @param property - DefaultGrailsDomainClassProperty
	 * @return Boolean
	 */
	static boolean display(DefaultGrailsDomainClassProperty property) {
		property.domainClass.getConstrainedProperties()[property.name]?.display
	}

	/**
	 * Gets metaConstraint defautlValue
	 * @param property - DefaultGrailsDomainClassProperty
	 * @return String - defaultValue if setted. Otherwise blank String
	 */
	static String defaultValue(DefaultGrailsDomainClassProperty property) {
		def constraint = property.domainClass.getConstrainedProperties()[property.name]

		def defaultValue = constraint?.getMetaConstraintValue("defaultValue")

		if (defaultValue == null) {
			return ""
		}

		return " = $defaultValue"
	}

	/**
	 * Gets metaConstrarint dateFormat
	 * @param property - DefaultGrailsDomainClassProperty
	 * @return String - dateFormate if setted. otherwise	default value DD/MM/YYYY
	 */
	static String dateFormat(DefaultGrailsDomainClassProperty property) {
		def constraint = property.domainClass.getConstrainedProperties()[property.name]

		def dateFormat = constraint?.getMetaConstraintValue("dateFormat")

		if (dateFormat == null) {
			dateFormat = "DD/MM/YYYY"
		}

		dateFormat
	}

	/**
	 * Check if the property's constraint widget is setted as richtext
	 * @param property - DefaultGrailsDomainClassProperty
	 * @return Boolean - true if widget is setted richtext. Otherwise false
	 */
	static boolean richtext(DefaultGrailsDomainClassProperty property) {
		def constraint = property.domainClass.getConstrainedProperties()[property.name]

		boolean isRichtext = false

		if (constraint?.widget == FIC.RICH_TEXT) {
			isRichtext = true
		}

		isRichtext
	}

	/**
	 * Check if domain class is reportable
	 * @param domainClass
	 * @return Boolean
	 */
	static Boolean isReportable(domainClass) {
		domainClass.getPropertyValue('reportable') != null
	}

	/**
	 * Gets message for i18n is setted as metaconstraint
	 * @param property - DefaultGrailsDomainClassProperty
	 * @param locale - String
	 * @return String or null if not exist a message
	 */
	static String i18n(DefaultGrailsDomainClassProperty property,String locale) {
		def constraint = property.domainClass.constrainedProperties[property.name]
		def messages = constraint?.getMetaConstraintValue("i18n")

		if (messages && !(messages instanceof Map)) {
			throw new Exception("i18n must be a Map eg: i18n:[us:'message']")
		}

		if (messages && messages[locale]) {
			return messages[locale]
		}
	}

	/**
	 * Gets annotation generate
	 * @param domainClass 	- DefaultGrailsDomainClass
	 * @return String 		- if generate is setted return value otherwise false
	 */
	static String generate(domainClass) {
		def annotation = domainClass.clazz.getAnnotation(FlexScaffoldProperty)
		boolean result = false

		if (annotation != null) {
			result = annotation.generate() == "true"
		}

		result
	}

	static boolean multiselection(domainClass) {
		def action = GrailsClassUtils.getStaticPropertyValue(domainClass.clazz,"action")

		if (action?.datagrid?.multiselection) {
			return true
		}

		false
	}

	static List actions(domainClass) {
		List actions = []
		def action = GrailsClassUtils.getStaticPropertyValue(domainClass.clazz, "action")

		if (action?.datagrid?.actions) {
			if (!(action?.datagrid?.actions instanceof List)) {
				throw new Exception("Property actions into datagrid must be List")
			}

			actions = action.datagrid.actions
		}

		actions
	}

	static String groupName(domainClass) {
		GrailsClassUtils.getStaticPropertyValue(domainClass.clazz, "groupName")
	}

	static boolean password(DefaultGrailsDomainClassProperty property) {
		def constraint = property.domainClass.constrainedProperties[property.name]

		boolean isPassword = false

		if (constraint?.widget == FIC.PASSWORD) {
			isPassword = true
		}

		isPassword
	}
}
