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

import java.util.prefs.Base64

import org.apache.commons.io.IOUtils

/**
 * Gets logict to upload files
 *
 * @author Ezequiel Martin Apfel
 */
class FileUploadController {

	def index = {
		String path = grailsApplication.config.filepath ? "${grailsApplication.config.filepath}/" : ""

		def file = request.getFile("uploadfile")
		if (file.empty) {
			flash.message = 'file cannot be empty'
			return
		}

		try {
			String filename = "${path}${request.getParameter('Filename')}".trim()
			file.transferTo new File(filename)

			def output = response.outputStream
			output.println filename
			output.flush()
		}
		catch (e) {
			log.error e.message, e
		}
	}

	def snapshot = {
		String path = grailsApplication.config.filepath ? "${grailsApplication.config.filepath}/" : ""
		String filename = "${path}${params.name}".trim()

		try {
			FileOutputStream fos = new FileOutputStream(new File(filename))
			fos.write Base64.base64ToByteArray(params.image)
			fos.flush()
			fos.close()

			def output = response.outputStream
			output.println filename
			output.flush()
		}
		catch (e) {
			log.error e.message, e
		}
	}

	def get = {
		def file = new File(params.filePath.trim())
		if (!file.exists()) {
			return
		}

		try {
			def input = new FileInputStream(file)
			def output = response.outputStream

			IOUtils.copy input, output

			input.close()
			output.flush()
			output.close()
		}
		catch (e) {
			log.error e.message, e
		}
	}

	def delete = {
		try {
			if (new File(params.filePath.trim()).delete()) {
				response.sendError 200, "Done!"
			}
		}
		catch (e) {
			log.error e.message, e
		}
	}
}
