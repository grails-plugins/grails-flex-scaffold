package org.cubika.labs.scaffolding.form.impl

import org.cubika.labs.scaffolding.utils.FlexScaffoldingUtils as FSU

/**
 * Extends AbstractRelationBuildFormItem adding in place many to one builder functionality.
 * @author Ezequiel Martin Apfel
 */
class OneToManyBuildFormItem extends AbstractRelationBuildFormItem {

	/**
	 * Contructor
	 */
	OneToManyBuildFormItem(property, classLoader) {
		super(property, classLoader)
	}

	/**
	 * @see #AbstractRelationBuildFormItem
	 */
	String buildFormItemComponent(binding) {
		def sw = new StringWriter()
		def pw = new PrintWriter(sw)

		pw.println "				<${property.name}:${property.referencedDomainClass.shortName}OneToManyListView " +
			"""id="${getID()}" dataProvider="{${binding}}"/>"""

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
	String value() { "getVO()" }

	/**
	 * @see #AbstractRelationBuildFormItem
	 */
	protected void generateInnerViews(property) {
		String nameDir = antProp.'view.destdir' + "/${property.referencedDomainClass.propertyName}"
		new File(nameDir).mkdir()

		nameDir = "$nameDir/external"
		new File(nameDir).mkdir()

		String classNameDir = "${nameDir}/${property.referencedDomainClass.shortName}OneToManyListView.mxml"
		String templateDir = FSU.resolveResources("/*"+antProp.'view.otmlistfile').toString()

		defaultTemplateGenerator.generateTemplate(property.referencedDomainClass,templateDir,classNameDir,property.domainClass.propertyName)

		classNameDir = "${nameDir}/${property.referencedDomainClass.shortName}RelationEditView.mxml"
		templateDir = FSU.resolveResources("/*"+antProp.'view.relationeditfile').toString()

		defaultTemplateGenerator.generateTemplate(property.referencedDomainClass,templateDir,classNameDir,property.domainClass.propertyName)
	}
}
