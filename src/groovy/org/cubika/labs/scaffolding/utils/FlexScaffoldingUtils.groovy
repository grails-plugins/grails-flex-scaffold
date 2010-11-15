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

import grails.util.BuildSettingsHolder

import org.springframework.core.io.support.PathMatchingResourcePatternResolver

import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClassProperty
import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.codehaus.groovy.grails.orm.hibernate.support.ClosureEventTriggeringInterceptor as Events
import org.codehaus.groovy.grails.scaffolding.DomainClassPropertyComparator

import org.cubika.labs.scaffolding.form.FormItemConstants as FIC
import org.cubika.labs.scaffolding.utils.ConstraintValueUtils as CVU

/**
 * Utils used in flex templates.
 *
 * @author Ezequiel Martin Apfel
 */
class FlexScaffoldingUtils {

	/**
	 * Map with equivalent class AS3 / Groovy
	 */
	static private Map typesAS3Map = [:]

	static private resolver = new PathMatchingResourcePatternResolver()

	static grailsApplication

	static {
		//TODO: Add More types
		typesAS3Map.put(Boolean, ['class':"Boolean",         'import':""])
		typesAS3Map.put(Date,    ['class':"Date",            'import':""])
		typesAS3Map.put(Long,    ['class':"Number",          'import':"", 'cast':"Number"])
		typesAS3Map.put(Integer, ['class':"int",             'import':"", 'cast':"Number"])
		typesAS3Map.put(Double,  ['class':"Number",          'import':"", 'cast':"Number"])
		typesAS3Map.put(Float,   ['class':"Number",          'import':"", 'cast':"Number"])
		typesAS3Map.put(String,  ['class':"String",          'import':"", 'cast':"String"])
		typesAS3Map.put(Set,     ['class':"ArrayCollection", 'import':"import mx.collections.ArrayCollection"])
		typesAS3Map.put(List,    ['class':"ArrayCollection", 'import':"import mx.collections.ArrayCollection"])
	}

	/**
	 * Gets equivalent AS3 Class for Groovy TODO: o Java class
	 * @param property - DefaultGrailsDomainClassProperty
	 * @return String - equivalent AS3 class
	 */
	static String getClass4AS3(DefaultGrailsDomainClassProperty property) {
		//If property.type is contained in typeAS3Map
		if (typesAS3Map.containsKey(property.type) && !property.isIdentity() &&
				property.name != "version" && CVU.display(property)) {
			return typesAS3Map.get(property.type)['class']+CVU.defaultValue(property)
		}

		//If property is a Relation
		if ((property.isOneToOne() || property.isManyToOne()) && CVU.display(property)) {
			return "${property.referencedDomainClass.shortName}VO"
		}

		if (property.isIdentity()) {
			return "Object"
		}

		if (property.name == "version") {
			return "Object"
		}

		//otherwise
		if (CVU.display(property)) {
			return "${property.type.propertyName}${CVU.defaultValue(property)}"
		}
	}

	/**
	 * Gets class cast for as3
	 * @param property - DefaultGrailsDomainClassProperty
	 * @return String - cast for as3
	 */
	static String getClassCast(property) {
		if (typesAS3Map.containsKey(property.type)) {
			return typesAS3Map.get(property.type)['cast']
		}
	}

	/**
	 * Gets import for as3.
	 * @param property - DefaultGrailsDomainClassProperty
	 * @return import for AS3 Class
	 */
	static String getImport4AS3(DefaultGrailsDomainClassProperty property) {

		if ((property.isOneToOne() || property.isManyToOne()) && CVU.display(property)) {
			//vo.package.class
			return "import vo.${property.referencedDomainClass.propertyName}.${property.referencedDomainClass.shortName}VO"
		}

		if (typesAS3Map.containsKey(property.type) && CVU.display(property)) {
			return typesAS3Map.get(property.type)['import']
		}
	}

	/**
	 * Gets DataGridColumn per DefaultGrailsDomainClassProperty
	 * TODO: create strategies for column itemRenderer
	 * @param property - DefaultGrailsDomainClassProperty
	 * @return String - DataGridColumn
	 */
	static String getDataGridColumn(DefaultGrailsDomainClassProperty property) {
		String result

		if (!property.isOneToOne() && !property.isOneToMany() && !property.isManyToOne() && CVU.display(property)) {
			def sw = new StringWriter()
			def pw = new PrintWriter(sw)

			if (property.type == Date) {
				pw.println """<cubikalabs:CBKDateFormatterDataGridColumn dataField="${property.name}" """ +
					"""headerText="{MultipleRM.getString(MultipleRM.localePrefix,'${property.domainClass.propertyName}.${property.name}')}" />"""

				return sw.toString()
			}

			if (CVU.richtext(property)) {
				pw.println """
				<mx:DataGridColumn dataField="${property.name}" headerText="{MultipleRM.getString(MultipleRM.localePrefix,'${property.domainClass.propertyName}.${property.name}')}">
					<mx:itemRenderer>
						<mx:Component>
							<mx:Label htmlText="{data.${property.name}}"/>
						</mx:Component>
					</mx:itemRenderer>
				</mx:DataGridColumn>"""
				return sw.toString()
			}

			if (CVU.password(property)) {
				pw.println """
				<mx:DataGridColumn dataField="${property.name}" headerText="{MultipleRM.getString(MultipleRM.localePrefix,'${property.domainClass.propertyName}.${property.name}')}">
					<mx:itemRenderer>\n"+
						<mx:Component>\n"+
							<mx:TextInput displayAsPassword="true" text="{data.${property.name}}" enabled="false" borderThickness="0" borderStyle="none" backgroundAlpha="0"/>"+
						</mx:Component>\n"+
					</mx:itemRenderer>\n"+
				</mx:DataGridColumn>"""
				return sw.toString()
			}

			pw.println """<mx:DataGridColumn dataField="${property.name}" """ +
				"""headerText="{MultipleRM.getString(MultipleRM.localePrefix,'${property.domainClass.propertyName}.${property.name}')}" />"""

			result = sw.toString()
		}

		return result
	}

	/**
	 * Capitalize a propertyname
	 * @param s - String
	 * @return s capitalizate
	 */
	static String capitalize(String s) {
		return s[0].toUpperCase() + s[1..-1]
	}

	/**
	 * Gets array of DefaultGrailsDomainClassProperty with identity (id, version)
	 * @parm	domainClass - DefaultGrailsDomainClass
	 * @return DefaultGrailsDomainClassProperty[] with id and version
	 */
	static DefaultGrailsDomainClassProperty[] getPropertiesWithIdentity(
				DefaultGrailsDomainClass domainClass, Boolean inherited = true) {

		def excludedProps = [Events.ONLOAD_EVENT,
		                     Events.BEFORE_DELETE_EVENT,
		                     Events.BEFORE_INSERT_EVENT,
		                     Events.BEFORE_UPDATE_EVENT]

		if (inherited) {
			return domainClass.properties.findAll { !excludedProps.contains(it.name)}
		}

		return domainClass.properties.findAll { !excludedProps.contains(it.name) && !it.inherited}
	}

	/**
	 * Gets array of DefaultGrailsDomainClassProperty without identity (id, version)
	 * @param order			- if order is true the array is order by DomainClassPropertyComparator
	 * @param domainClass		- DefaultGrailsDomainClass
	 * @return DefaultGrailsDomainClassProperty[] without id and version
	 */
	static DefaultGrailsDomainClassProperty[] getPropertiesWithoutIdentity(DefaultGrailsDomainClass domainClass, Boolean order = false, inherited = true) {
		def excludedProps = ['id', 'version',
		                     Events.ONLOAD_EVENT,
		                     Events.BEFORE_DELETE_EVENT,
		                     Events.BEFORE_INSERT_EVENT,
		                     Events.BEFORE_UPDATE_EVENT]

		def properties
		if (inherited) {
			properties = domainClass.properties.findAll { !excludedProps.contains(it.name)}
		}
		else {
			properties = domainClass.properties.findAll { !excludedProps.contains(it.name) && !it.inherited}
		}

		if (order) {
			Collections.sort(properties, new DomainClassPropertyComparator(domainClass))
		}

		properties
	}

	/**
	 * Gets namespace of RelationView
	 * @param property	- DefaultGrailsDomainClassProperty
	 * @return String	- namespace of RelationView
	 */
	static String getNameSpace(properties) {
		StringBuilder ns = new StringBuilder()
		properties.each {
			if ((it.isOneToMany() || it.isOneToOne() || it.isManyToOne()) && CVU.display(it)) {
				ns.append "\nxmlns:${it.name}=\"view.${it.referencedDomainClass.propertyName}.external.*\" "
			}
		}

		ns.toString()
	}

	/**
	 * Resolve path for pluging dir
	 * @param pattern - String with partial path
	 * @return a concrete path
	 */
	static resolveResources(pattern) {
		def settings = BuildSettingsHolder.settings
		try {
			// check local dir first
			def path = resolveResource("file:${settings.baseDir.canonicalFile}${pattern}")
			if (!path) {
				// then local plugin
				path = resolveResource("file:${settings.projectPluginsDir.canonicalFile}${pattern}")
			}
			if (!path) {
				// then global plugin
				if (settings.globalPluginsDir.exists()) {
					path = resolveResource("file:${settings.globalPluginsDir.canonicalFile}${pattern}")
				}
			}
			path
		}
		catch (e) {
			throw new Exception("${settings.projectPluginsDir.canonicalFile}${pattern} Not Found $e", e)
		}
	}

	private static File resolveResource(String path) {
		def dir = resolver.getResources(path)
		if (dir) {
			return dir.file[0]
		}
	}

	static String getSuperClassName(domainClass) {

		Class superClass = domainClass.clazz.superclass

		if (!superClass.equals(Object) && !superClass.equals(GroovyObject)) {
			return grailsApplication.getDomainClass(superClass.name).propertyName
		}
	}
}
