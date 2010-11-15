import org.springframework.web.servlet.support.RequestContextUtils as RCU
import org.springframework.web.context.request.RequestContextHolder as RCH

import org.springframework.flex.remoting.RemotingDestination
import org.springframework.flex.remoting.RemotingExclude
import org.springframework.flex.remoting.RemotingInclude

<%
if (domainClass.packageName || domainClass.packageName != "") {
	println "import ${domainClass.fullName}"
}
%>
@RemotingDestination(channels=['my-amf'])
class ${className}Service {

	def messageSource

	@RemotingInclude
	def save(${className} ${domainClass.propertyName}) {
		if (${domainClass.propertyName}.id) {
			def ${domainClass.propertyName}Merged = ${domainClass.propertyName}.merge()

			if (${domainClass.propertyName}Merged) {
				${domainClass.propertyName} = ${domainClass.propertyName}Merged
			}
		}
		else {
			${domainClass.propertyName}.save()
		}

		if (!${domainClass.propertyName}.hasErrors()) {
			return ${domainClass.propertyName}
		}

		throw new Exception(getMessages(${domainClass.propertyName}.errors))
	}

	@RemotingInclude
	def list() {
		${className}.list()
	}

	@RemotingInclude
	def paginateList(pageFilter) {
		if (pageFilter.totalChange) {
			int count = Math.ceil(${className}.count()/pageFilter.max);
			pageFilter.setTotal(count);
		}

		def list

		if (pageFilter.query && pageFilter.query.size() > 0) {
<% import org.cubika.labs.scaffolding.utils.FlexScaffoldingUtils as FSU
			String prefix = className[0].toLowerCase()
			def props = FSU.getPropertiesWithoutIdentity(domainClass,true)
			String query = "from ${className} as ${prefix} where"

			String params = "\"\${pageFilter.query}%\""

			props.each {
				if (!it.isOneToOne() && !it.isOneToMany() && !it.isManyToOne() && it.type != Boolean && it.type != Date) {
					query +=" ${prefix}.${it.name} like :search or"
				}
			}

			if (query.endsWith(" or")) {
				query = query.substring(0, query.size() - 3)
			}
%>
			list = ${className}.findAll(
				"${query}",
				[search: ${params}],
				pageFilter.params)
		}
		else {
			list = ${className}.list(pageFilter.getParams())
		}

		pageFilter.list = list
		pageFilter
	}

	@RemotingInclude
	def destroy(${domainClass.propertyName}List) {
		${domainClass.propertyName}List.each {
			it.delete(flush: true)

			if (it.hasErrors()) {
				throw new Exception(getMessages(it.errors))
			}
		}

		${domainClass.propertyName}List
	}

	private getMessages(errors) {
		def locale = RCU.getLocale(RCH.currentRequestAttributes().request)

		String errorString = ""

		errors.allErrors.each {
			<%
			print "errorString +=messageSource.getMessage(it,locale)+\"\\n\""
			%>
		}

		errorString
	}

<%
import org.cubika.labs.scaffolding.utils.ConstraintValueUtils as CVU
CVU.actions(domainClass).each {
	println "		def ${it.toLowerCase()}(${className} ${domainClass.propertyName}) {"
	println "			return \"add logic to ${it}\""
	println "		}"
}
%>
}
