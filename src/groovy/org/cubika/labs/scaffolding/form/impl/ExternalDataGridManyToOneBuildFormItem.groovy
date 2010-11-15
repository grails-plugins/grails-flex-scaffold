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
package org.cubika.labs.scaffolding.form.impl

import org.cubika.labs.scaffolding.utils.FlexScaffoldingUtils as FSU

/**
 * Extends AbstractRelationBuildFormItem adding external many to one builder functionality.
 * @author Ezequiel Martin Apfel
 */
class ExternalDataGridManyToOneBuildFormItem extends AbstractRelationBuildFormItem {

	/**
	 * Constructor
	 */
	ExternalDataGridManyToOneBuildFormItem(property, classLoader) {
		super(property, classLoader)
	}

	/**
	 * @see #AbstractRelationBuildFormItem
	 */
	protected String buildFormItemComponent(binding) {
		def sw = new StringWriter()
		def pw = new PrintWriter(sw)

		pw.println "				<${property.name}:${property.referencedDomainClass.shortName}ExternalManyToOneView " +
			"""id="${getID()}" selectedItem="{${binding}}"/>"""

		generateViews(property)

		sw.toString()
	}

	/**
	 * @see #AbstractRelationBuildFormItem
	 */
	String getID() { "r${FSU.capitalize(property.name)}" }

	/**
	 * @see #AbstractRelationBuildFormItem
	 */
	String value() { "selectedItem" }

	/**
	 * @see #AbstractRelationBuildFormItem
	 */
	protected void generateInnerViews(property) {
		String nameDir = antProp.'view.destdir' + "/${property.referencedDomainClass.propertyName}"
		new File(nameDir).mkdir()

		nameDir = "$nameDir/external"
		new File(nameDir).mkdir()

		String classNameDir = "${nameDir}/${property.referencedDomainClass.shortName}ExternalManyToOneView.mxml"
		String templateDir = FSU.resolveResources("/*" + antProp.'view.edotolistfile').toString()

		defaultTemplateGenerator.generateRelationalTemplate(property.domainClass,
			property.referencedDomainClass, templateDir, classNameDir, property.domainClass.propertyName)

		classNameDir = "${nameDir}/${property.referencedDomainClass.shortName}PopSelect.mxml"
		templateDir = FSU.resolveResources("/*" + antProp.'view.externalpopselectfile').toString()

		defaultTemplateGenerator.generateRelationalTemplate(property.domainClass,
			property.referencedDomainClass, templateDir, classNameDir, property.naturalName)
	}
}
