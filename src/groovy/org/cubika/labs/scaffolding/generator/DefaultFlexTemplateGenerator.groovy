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
package org.cubika.labs.scaffolding.generator

import groovy.text.SimpleTemplateEngine

/**
 * Used by plugin scripts to convert templates into actionscript archives,
 * which are compiled when "grails flex-tasks" command is executed.
 * Gives support for relationship except many-to-many.
 *
 * @author Ezequiel Martin Apfel
 */
class DefaultFlexTemplateGenerator {

	private engine
	private ant = new AntBuilder()

	DefaultFlexTemplateGenerator(classLoader) {
		engine = new SimpleTemplateEngine(classLoader)
	}

	/**
	 * Create Flex templates
	 *
	 * @param domainClass
	 * @param templateFile			Template file to be used
	 * @param filePath				Path for result as file
	 */
	void generateTemplate(domainClass, templateFile, filePath, String typeName = "") {
		Map binding = [domainClass: domainClass,
		               className: domainClass.shortName,
		               typeName: typeName]
		generate templateFile, filePath, binding
	}

	/**
	 * Create Flex relational templates
	 *
	 * @param domainClass
	 * @param templateFile			Template file to be used
	 * @param filePath				Path for result as file
	 */
	void generateRelationalTemplate(relationDomainClass, domainClass, templateFile,
	                                filePath, String typeName = "") {
		Map binding = [domainClass: domainClass,
		               className: domainClass.shortName,
		               relationDomainClass: relationDomainClass,
		               typeName: typeName]
		generate templateFile, filePath, binding
	}

	/**
	 * Create a Simple Templates
	 */
	void generateSimpleTemplate(templateFile, filePath, artifactName, pkg, className) {
		Map binding = [artifactName: artifactName,
		               className: className,
		               pkg: pkg]
		generate templateFile, filePath, binding
	}

	private void generate(templateFile, filePath, Map binding) {
		String templateText = new File(templateFile).text
		Writer w = new File(filePath).newWriter()
		engine.createTemplate(templateText).make(binding).writeTo(w)
		w.flush()
		w.close()
	}
}
