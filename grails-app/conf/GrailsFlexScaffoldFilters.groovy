import org.codehaus.groovy.grails.commons.GrailsClass
import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler

import org.cubika.labs.scaffolding.reporting.query.ReportingQueryBuilder as RQB
import org.cubika.labs.scaffolding.utils.FlexScaffoldingUtils as FSU

/**
 * @author gclavell
 */
class GrailsFlexScaffoldFilters {

	def filters = {
		doGfsDjReport(controller:'*jReport', action:'*') {
			before = {

				String domainClassName = FSU.capitalize(params['entity'])	
				GrailsClass domainClass = grailsApplication.getArtefactByLogicalPropertyName(
					DomainClassArtefactHandler.TYPE, params.entity)
				Class clazz = grailsApplication.getClassForName(domainClassName)

				domainClass.getPropertyValue('reportable').dataSource = { session,params ->
					RQB.getDataSourceFromParams(clazz,domainClass,domainClassName,params)
				}
				domainClass.getPropertyValue('reportable').columns = params['columns']?.split(',')

				List columnTitlesList = params['columnTitles']?.split(',')

				if (columnTitlesList) {
					Map columnTitles = [:]
					columnTitlesList.each {
						def splittedValue = it.split(':')
						def key = splittedValue[0]
						def value = splittedValue[1]
						columnTitles.put(key,value)
					}
					domainClass.getPropertyValue('reportable').columnTitles = columnTitles
				}
			}
		}
	}
}
