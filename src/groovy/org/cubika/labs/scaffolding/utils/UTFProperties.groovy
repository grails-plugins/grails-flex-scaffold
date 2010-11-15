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

/**
 * Loads file with encoding utf-8.
 *
 * @author Ezequiel Martin Apfel
 */
class UTFProperties extends Properties {

	/**
	 * Loads file with encoding utf-8.
	 * @param file - a file
	 */
	void load(File file) {
		int index = 0
		try {
			String text = file.getText("UTF-8")
			text.split("[\\n\\r]+").each {
				index++
				if (!it.startsWith("#")) {
					int pos = it.indexOf("=")
					if (pos > 0) {
						setProperty(it[0..pos-1], it[(pos+1)..-1])
					}
				}
			}
		}
		catch (StringIndexOutOfBoundsException e) {
			throw new Exception("Error trying to load file at line $index. Check the syntax")
		}
	}
}
