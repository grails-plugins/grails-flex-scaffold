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
package org.cubika.labs.scaffolding.response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mock.web.DelegatingServletOutputStream;

/**
 * @author gclavell
 */
public class ReportingResponseWrapper implements HttpServletResponse {

	private final ByteArrayOutputStream byteOS;
	private final DelegatingServletOutputStream dsos;
	private final PrintWriter pWriter;

	public ReportingResponseWrapper() {
		byteOS = new ByteArrayOutputStream();
		dsos = new DelegatingServletOutputStream(byteOS);
		pWriter = new PrintWriter(dsos);
	}

	public ServletOutputStream getOutputStream() throws IOException {
		return dsos;
	}

	public byte[] toByteArray() {
		return byteOS.toByteArray();
	}

	public void flushBuffer() throws IOException {
		dsos.flush();
	}

	public PrintWriter getWriter() throws IOException {
		return pWriter;
	}

	public void addCookie(Cookie arg0) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public boolean containsHeader(String arg0) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public String encodeURL(String arg0) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public String encodeRedirectURL(String arg0) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public String encodeUrl(String arg0) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public String encodeRedirectUrl(String arg0) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void sendError(int arg0, String arg1) throws IOException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void sendError(int arg0) throws IOException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void sendRedirect(String arg0) throws IOException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setDateHeader(String arg0, long arg1) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void addDateHeader(String arg0, long arg1) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setHeader(String arg0, String arg1) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void addHeader(String arg0, String arg1) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setIntHeader(String arg0, int arg1) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void addIntHeader(String arg0, int arg1) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setStatus(int arg0) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setStatus(int arg0, String arg1) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public String getCharacterEncoding() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public String getContentType() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setCharacterEncoding(String arg0) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setContentLength(int arg0) {
		//
	}

	public void setContentType(String arg0) {
		//
	}

	public void setBufferSize(int arg0) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public int getBufferSize() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void resetBuffer() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public boolean isCommitted() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void reset() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setLocale(Locale arg0) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Locale getLocale() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
