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

/**
 * @author Bill Bejeck 6/30/2008
 *
 * Used to generate required Flex artifacts for deployment
 * Usage:
 *       $ grails flex-tasks wrapper - will generate a gsp wrapper file to load the flash player, by default file named index.gsp and placed in web-apps
 *       $ grails flex-tasks compile - will compile flex code into a swf file for deployment, by default swf file is placed in web-apps
 *       $ grails flex-tasks - will run all of the above tasks
 */

 import grails.util.Environment
 import grails.util.GrailsUtil
 
includeTargets << new File("$flexScaffoldPluginDir/scripts/_FlexScaffoldCommon.groovy")

basedir = ant.project.properties.basedir

String mxmlFile = antProperty('mxml.file')
String outputFile = antProperty('output.file')
//Set security to compile flex
security = false

ant.taskdef name: 'mxmlc', classname: 'flex.ant.MxmlcTask', classpath: "$flexHome/ant/lib/flexTasks.jar"
ant.taskdef name: 'gspWrapper', classname: 'flex.ant.HtmlWrapperTask', classpath: "$flexHome/ant/lib/flexTasks.jar"

args = System.getProperty("grails.cli.args")

target(flexTasks: 'Can choose to create gsp wrapper file or compile flex to swf or run both') {
	if (!flexHome) {
		println ""
		println "//////////////////////////////////// ERROR ////////////////////////////////////"
		println "// FLEX_HOME must be declarated as enviroment var or grails.plugin.flex.home //"
		println "///////////////////////////////////////////////////////////////////////////////"
		println ""
		exit 1
	}

	println "Flex Home: $flexHome"

	def config = new ConfigSlurper(Environment.current.name).parse(
		new File('grails-app/conf/Config.groovy').toURL())

	security = config?.gfs?.security
	if (!security) {
		security = false
	}

	println "Compile with security in: $security"

	if (args) {
		if (File.separator == "\\") { //Because windows ......
			flexHome = flexHome.replace("\\", "/")
		}

		if ('wrapper'.equals(args)) {
			wrap()
		}
		else if ('compile'.equals(args)) {
			compile()
		}
		else {
			println "'$args' is not a valid option"
		}
	}
	else {
		runAll()
	}

	ant.mkdir dir: "web-app/assets"
	ant.copy(toDir: "web-app/assets") {
		fileset dir: "${antProperty('flex.srcdir')}/assets"
	}

	println "Flex compile Done!"
}

target(runAll: "Runs all Flex tasks") {
	depends(compile, wrap)
}

target(clean: "Deletes the previous SWF file") {
	ant.delete file: outputFile
}

target(compile: 'Compile Flex project into SWF file') {
	// TODO set as properties
	def extraSourcePaths = []
	def compileTimeConstants = ['GFS::security': "$security"]
	def extraLibPaths = []

	def splitFile = { String path ->
		def file = new File(path)
		def parentFile = (file.parentFile ?: new File(basedir)).canonicalFile
		[dir: parentFile.path, name: file.name]
	}

	try {
		ant.mxmlc(file: mxmlFile, output: outputFile,
				incremental: antProperty('incremental'),
				'actionscript-file-encoding': antProperty('encoding'),
				'show-unused-type-selector-warnings': false,
				'show-invalid-css-property-warnings': false,

				services: "$basedir/web-app/WEB-INF/flex/services-config.xml",
				debug: antProperty('debug'),
				'context-root': antProperty('context-root'),
				'keep-generated-actionscript': false) {

					'load-config'(filename: "$flexHome/frameworks/flex-config.xml")

					'compiler.library-path'(dir: "$flexHome/frameworks", append: true) {
						include name: "libs"
						include name: "../bundles/{locale}"
					}
					'compiler.library-path'(dir: splitFile(antProperty('lib.destdir')).dir, append: true) {
						include name: splitFile(antProperty('lib.destdir')).name
					}

					extraLibPaths.each { path ->
						'compiler.library-path'(dir: splitFile(path).dir, append: true) {
							include name: splitFile(path).name
						}
					}

					'source-path'('path-element': "$flexHome/frameworks")
					'source-path'('path-element': "$basedir/grails-app/i18n")
					extraSourcePaths.each { path ->
						'source-path'('path-element': path)
					}

					compileTimeConstants.each { k, v -> define name: k, value: v }
				}
	}
	catch (e) {
		GrailsUtil.sanitize e
		throw e
	}
}

target(wrap: "Creates a gsp wrapper to load the flash player") {
	ant.gspWrapper title: antProperty('title'),
	               file: antProperty('file'),
	               height: antProperty('height'),
	               width: antProperty('width'),
	               bgcolor: antProperty('bgcolor'),
	               application: antProperty('application'),
	               swf: antProperty('swf'),
	               'version-major': antProperty('version-major'),
	               'version-minor': antProperty('version-minor'),
	               'version-revision': antProperty('version-revision'),
	               history: antProperty('history'),
	               output: antProperty('output')

	ant.copy file: "${antProperty('output')}/${antProperty('file')}",
	         tofile: "grails-app/views/${antProperty('file')}",
	         verbose: true
}

setDefaultTarget 'flexTasks'
