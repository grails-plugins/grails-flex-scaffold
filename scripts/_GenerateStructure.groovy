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

generateFlexDefaultStructure = { Map args = [:] ->

	ant.mkdir dir: antProperty('flex.basedir')
	ant.mkdir dir: antProperty('vo.destdir')
	ant.mkdir dir: antProperty('view.destdir')
	ant.mkdir dir: antProperty('service.destdir')
	ant.mkdir dir: antProperty('model.destdir')
	ant.mkdir dir: antProperty('lib.destdir')
	ant.mkdir dir: antProperty('event.destdir')

	ant.mkdir dir: antProperty('css.destdir')
	ant.copy file: pluginDirPath + antProperty('css.styleselected'),
	         tofile: antProperty('css.file'), verbose: true

	ant.mkdir dir: antProperty('controller.destdir')

	ant.mkdir dir: antProperty('command.destdir') + "/gfs"
	ant.mkdir dir: antProperty('command.destdir')

	if (!new File(antProperty('assets.destdir')).exists()) {
		ant.mkdir dir: antProperty('assets.destdir')

		ant.copy(toDir: antProperty('assets.destdir')) {
			fileset dir: pluginDirPath + antProperty('css.assetsselected')
		}
	}
}
