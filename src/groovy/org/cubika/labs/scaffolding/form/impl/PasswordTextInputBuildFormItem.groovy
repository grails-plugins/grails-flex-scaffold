package org.cubika.labs.scaffolding.form.impl

import org.cubika.labs.scaffolding.utils.FlexScaffoldingUtils as FSU

/**
 * Extends AbstractBuildFormItem adding 2 textinput to password building functionality.
 * @author Ezequiel Martin Apfel
 */
class PasswordTextInputBuildFormItem extends AbstractBuildFormItem {

	PasswordTextInputBuildFormItem(property) {
		super(property)
	}

	/**
	 * Build a FormItem
	 * @param binding - String con la propiedad que va a estar mirando el componente
	 */
	String build(binding) {

		def sw = new StringWriter()
		def pw = new PrintWriter(sw)

		pw.println "			<mx:FormItem label=\"{MultipleRM.getString(MultipleRM.localePrefix,'${property.domainClass.propertyName}.${property.name}')}\" id=\"${getFormItemID()}\" width=\"100%\">"

		pw.print	buildFormItemComponent(binding)

		pw.println "			</mx:FormItem>"

		pw.println "			<mx:FormItem label=\"{MultipleRM.getString(MultipleRM.localePrefix,'${property.domainClass.propertyName}.${property.name}')}\" width=\"100%\">"

		pw.print	buildFormItemComponent(binding,"txtRePassword")

		pw.println "			</mx:FormItem>"

		sw.toString()
	}

	String buildValidator() {
		if (validator) {
			return validator.build("txt${FSU.capitalize(property.name)}", "text", constraint)
		}
	}

	protected String buildFormItemComponent(binding) {
		buildFormItemComponent(binding, "txt${FSU.capitalize(property.name)}")
	}

	protected String buildFormItemComponent(binding, id) {

		def sw = new StringWriter()
		def pw = new PrintWriter(sw)

		pw.println "				<mx:TextInput id=\"${id}\" text=\"{${binding}}\" width=\"217\" displayAsPassword=\"true\"/>"

		sw.toString()
	}

	/**
	 * Return id.value for formItem
	 */
	String getFormAttr() { "${getID()}" }

	String getID() {
		"txt${FSU.capitalize(property.name)}.text != _vo.${property.name} ? SHA256.hashToBase64(txt${FSU.capitalize(property.name)}.text) : _vo.${property.name}"
	}

	String value() { "" }
}
