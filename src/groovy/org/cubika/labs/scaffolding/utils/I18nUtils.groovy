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

import com.google.api.translate.Language
import com.google.api.translate.Translate

import org.apache.commons.lang.StringUtils

import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClassProperty

import org.cubika.labs.scaffolding.utils.FlexScaffoldingUtils as FSU

import org.springframework.core.io.support.PathMatchingResourcePatternResolver

/**
 * I18n Utils.
 *
 * @author Ezequiel Martin Apfel
 */
class I18nUtils {

	static ant = new AntBuilder()
	static resolver = new PathMatchingResourcePatternResolver()

	private static antProp

	private static Map languages = [
		ar:      Language.ARABIC,
		bg:      Language.BULGARIAN,
		ca:      Language.CATALAN,
		zh:      Language.CHINESE,
		zh_CN:   Language.CHINESE_SIMPLIFIED,
		'zh-TW': Language.CHINESE_TRADITIONAL,
		hr:      Language.CROATIAN,
		cs:      Language.CZECH,
		da:      Language.DANISH,
		nl:      Language.DUTCH,
		en:      Language.ENGLISH,
		tl:      Language.FILIPINO,
		fi:      Language.FINNISH,
		fr:      Language.FRENCH,
		gl:      Language.GALICIAN,
		de:      Language.GERMAN,
		el:      Language.GREEK,
		iw:      Language.HEBREW,
		hi:      Language.HINDI,
		hu:      Language.HUNGARIAN,
		id:      Language.INDONESIAN,
		'it':    Language.ITALIAN,
		ja:      Language.JAPANESE,
		ko:      Language.KOREAN,
		lv:      Language.LATVIAN,
		lt:      Language.LITHUANIAN,
		mt:      Language.MALTESE,
		no:      Language.NORWEGIAN,
		pl:      Language.POLISH,
		pt_BR:   Language.PORTUGUESE,
		pt:      Language.PORTUGUESE,
		ro:      Language.ROMANIAN,
		ru:      Language.RUSSIAN,
		sr:      Language.SERBIAN,
		sk:      Language.SLOVAK,
		sl:      Language.SLOVENIAN,
		es:      Language.SPANISH,
		sv:      Language.SWEDISH,
		th:      Language.THAI,
		tr:      Language.TURKISH,
		uk:      Language.UKRANIAN,
		vi:      Language.VIETNAMESE
	]

	/**
	 * Returns the flex metatags for resource bundles.
	 *
	 * @return String with Flex MetaTags format.
	 */
	static String getMetaTags() {
		def locales = getTags(getLocalesProperties('locale.default') + ", " + getLocalesProperties('locale.locales'))

		StringBuilder result = new StringBuilder()
		locales.each {
			result.append "		[ResourceBundle(\"messages${suffix(it.trim())}\")]\n"
		}
		result.toString()
	}

	/**
	 * Returns AS <code>ArrayCollection()</code> populated
	 * with locales array.
	 *
	 * @return String with locales.
	 */
	static String getLocalesCollection() {
		String collection = "private var localesCollection:ArrayCollection = new ArrayCollection("

		def locales = getTags(getLocalesProperties('locale.default') + ", " + getLocalesProperties('locale.locales'))

		StringBuilder array = new StringBuilder()

		locales.eachWithIndex { it, i ->
			array.append "{label:'"
			array.append getLanguageValue(it.trim())
			array.append "', locale:'"
			array.append it.trim()
			array.append "', data:'messages${suffix(it.trim())}'}"
			if (i != locales.size() - 1) {
				array.append ", "
			}
		}

		"${collection}[$array]);"
	}

	/**
	 * Split String to an Array
	 *
	 * @param String delimitated with a comma
	 * @return String[]
	 */
	private static String[] getTags(coll) { coll.split(",") }

	/**
	 * Search for avilables locales in <b>flexScaffold.properties</b> file.
	 * This read <code>locale.locales</code> value.
	 *
	 * @return String with available locales.
	 */
	static String resolveResourcesPath(fileName) {
		FSU.resolveResources("/*/src/resources/i18n/${fileName}.properties")
	}

	/**
	 * Esto es cualquiera
	 * TODO - refactoriza el metodo y como levanta el antprop
	 */
	static getLocalesProperties(path) {
		if (!antProp) {
			String fileProperties = "./grails-app/i18n/i18n.properties"//resolveResourcesPath("i18n")//
			ant.property(file: fileProperties)
			antProp = ant.project.properties
		}

		antProp[path]
	}

	static String getTranslateWord(String word, String fromLang, String toLang) {
		Translate.setHttpReferrer CH.config.grails.serverURL ?: "http://localhost:8080/"
		Translate.execute word, convertStringToLanguage(fromLang), convertStringToLanguage(toLang)
	}

	static String suffix(String locale) {
		locale == "en" ? "" : "_${locale}"
	}

	static private convertStringToLanguage(String locale) {
		languages[locale]
	}

	static private getLanguageValue(String locale) {
		StringUtils.capitalize(convertStringToLanguage(locale).toString().toLowerCase().replace("Language.", "").replace("_", " "))
	}
}
