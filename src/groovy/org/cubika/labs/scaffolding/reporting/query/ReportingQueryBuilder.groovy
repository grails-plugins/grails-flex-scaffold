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
package org.cubika.labs.scaffolding.reporting.query

import org.apache.log4j.Logger

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler

import org.cubika.labs.scaffolding.reporting.query.QueryObject
import org.cubika.labs.scaffolding.reporting.query.factory.QueryBuilderFactory as QBF
import org.cubika.labs.scaffolding.reporting.query.impl.AbstractQueryBuilder
import org.cubika.labs.scaffolding.utils.FlexScaffoldingUtils as FSU
import org.cubika.labs.scaffolding.utils.ConstraintValueUtils as CVU

/**
 * Prepare reporting datasource by received params.
 *
 * @author Gonzalo Javier Clavell
 */
class ReportingQueryBuilder {

	static Logger logger = Logger.getLogger(ReportingQueryBuilder)

	/**
	 * @param Class
	 * @param GrailsDomainClass
	 * @param String params containing http request params
	 * @return List containing dataSource for Report
	 */
	static List getDataSourceFromParams(clazz, domainClass, domainClassName, params) {
		List result
		QueryObject queryObject = new QueryObject()
		Boolean hasParams = checkParams(domainClass, params)
		queryObject.queryString += "from ${domainClassName}"

		if (hasParams) {
			queryObject.queryString += " where "
			def queryBuilder
			domainClass.properties.each {
				String valueString
				if (it.isManyToOne()) {
					valueString = params["${it.name}~${CVU.getLabelField(it.referencedDomainClass)}"]
				}
				else if (params[it.name] != null) {
					valueString = params[it.name]
				}
				if (valueString) {
					queryBuilder = QBF.getQueryBuilder(it, valueString, queryObject)
					if (queryBuilder) {
						queryBuilder.build()
					}
				}
			}
			queryObject.queryString = queryObject.queryString.substring(0, queryObject.queryString.size() -
				AbstractQueryBuilder.DEFAULT_QUERY_ASSOCIATION.size())
			result = clazz.findAll(queryObject.queryString, queryObject.queryParameters)
		}
		else {
			result = clazz.findAll(queryObject.queryString)
		}
		result
	}

	/**
	 * Check into params if filtering is needed
	 * @param GrailsDomainClass domainClass
	 * @param	String params
	 */
	static boolean checkParams(domainClass, params) {
		boolean result = false
		FSU.getPropertiesWithoutIdentity(domainClass).each {
			if (params.containsKey(it.name)) {
				result = true
			}
		}
		result
	}
}
