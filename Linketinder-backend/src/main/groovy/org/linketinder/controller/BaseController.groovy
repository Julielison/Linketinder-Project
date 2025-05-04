package org.linketinder.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

abstract class BaseController extends HttpServlet {
	protected ObjectMapper objectMapper

	BaseController() {
		this.objectMapper = new ObjectMapper()
		this.objectMapper.registerModule(new JavaTimeModule())
	}

	protected static void validateAcceptHeader(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String acceptHeader = req.getHeader("Accept")
		if (acceptHeader == null || !acceptHeader.contains("application/json")) {
			resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE)
			resp.getWriter().write("{\"erro\": \"Este endpoint aceita apenas 'application/json' no cabeçalho 'Accept'.\"}")
			throw new ServletException("Header 'Accept' inválido.")
		}
	}

	protected static void setJsonResponseHeaders(HttpServletResponse resp) {
		resp.setContentType("application/json;charset=UTF-8")
		resp.setCharacterEncoding("UTF-8")
	}
}