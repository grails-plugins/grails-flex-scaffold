class FlexScaffoldGrailsPlugin {
	def version = '0.3'
	def grailsVersion = '1.2.2 > *'
	def dependsOn = [flex: '0.4 > *']
	def pluginExcludes = [
		'grails-app/domain/**'
	]

	def observe = ['services']
	def loadAfter = ['services']

	def author = 'Ezequiel Martin Apfel'
	def authorEmail = 'eapfel@cubika.com'
	def title = 'Grails Flex Scaffold (GFS)'
	def description = '''\
Grails Flex Scaffold (GFS) is a plugin that deals Flex code generation by
scaffolding methodology, including support for presentation and service (integration with BlazeDS)
layers by providing embeded data in your domain classes as original Grails Scaffolding does with Ajax and HTML'''

	def documentation = 'http://grails.org/plugin/flex-scaffold'
}
