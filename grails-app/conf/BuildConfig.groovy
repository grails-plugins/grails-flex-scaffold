grails.project.class.dir = 'target/classes'
grails.project.test.class.dir = 'target/test-classes'
grails.project.test.reports.dir = 'target/test-reports'

grails.project.dependency.resolution = {

	inherits 'global'

	log 'warn'

	repositories {
		grailsPlugins()
		grailsHome()
		grailsCentral()
		mavenCentral()
	}

	runtime 'com.adobe.acrobat:acrobat:1.1'
	runtime 'org.fontbox:fontbox:0.1.0'
	runtime('pdfbox:pdfbox:0.7.3') {
		transitive = false
	}

//	runtime 'com.google.translate:google-api-translate-java:0.92' // not in a repo
}
