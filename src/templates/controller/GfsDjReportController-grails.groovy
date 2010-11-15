import ar.com.fdvs.dj.domain.DynamicReport

import com.adobe.acrobat.sidecar.AffineTransform

import org.apache.commons.io.IOUtils
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

import org.cubika.labs.scaffolding.response.ReportingResponseWrapper

class GfsDjReportController extends DjReportController {

	static final String PDF_TEMP_FILE_NAME = "report.pdf"
	static final String JPG_TEMP_FILE_NAME = "report.jpg"
	static final String DEFAULT_REPORT_FORMAT = "jpg"

	DynamicReport report
	def columns
	def columnNames
	def groupColumns
	def items
	def reportFormat
	def exporter

	File pdfTempFile
	File imgTempFile

	ReportingResponseWrapper responseWrapper

	def pdfStream

	def doReport(domainClass, config, params, request, response) {
		responseWrapper = new ReportingResponseWrapper()
		super.doReport(domainClass, config, params, request, responseWrapper)
		imgTempFile = new File(JPG_TEMP_FILE_NAME)
		pdfTempFile = new File(PDF_TEMP_FILE_NAME)
		doExportReportToPDF()
		convertToImage()
		loadImageToResponseAndReset(response)
	}

	def doExportReportToPDF() {
		//Generates Pdf temporal file2

		def output = new FileOutputStream(pdfTempFile)
		def input = new ByteArrayInputStream(responseWrapper.toByteArray())

		IOUtils.copy(input,output)

		// input.close()
		output.flush()
		output.close()
	}

	private void convertToImage() {
		JFrame dummyFrame = new JFrame()

		PDFDocument pdfDocument = new PDFDocument(pdfTempFile)
		FloatPoint cropBoxSize = pdfDocument.getPageSize(0)//ITERAR
		int width = 1024
		int height = 1448
		dummyFrame.setVisible(true)
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
		float hScale = (float)((width) / cropBoxSize.x)
		float vScale = (float)((height) / cropBoxSize.y)
		float scale = (float)Math.min(hScale, vScale)
		int w = (int)(cropBoxSize.x * scale)
		int h = (int)(cropBoxSize.y * scale)

		Image osImage = dummyFrame.createImage(w, h)
		AffineTransform transform = new AffineTransform(scale, 0, 0, scale, 0, 0)

		def g = img.graphics
		g.setColor(java.awt.Color.white)
		g.fillRect(0,0,width,height)

		pdfDocument.drawPage(0, img, transform, null, dummyFrame)
		ImageIO.write(img,DEFAULT_REPORT_FORMAT,imgTempFile)
		dummyFrame.setVisible(false)
	}

	def loadImageToResponseAndReset(response) {
		response.contentType = "image/jpeg"
		response.contentLength = (int)imgTempFile.size()
		def out = response.outputStream
		IOUtils.copy(new FileInputStream(imgTempFile),out)
		//out.flush()
		//out.close()

		imgTempFile.delete()
		pdfTempFile.delete()
	}
}
