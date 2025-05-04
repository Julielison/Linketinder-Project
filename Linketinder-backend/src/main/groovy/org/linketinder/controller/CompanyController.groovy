package org.linketinder.controller

import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.linketinder.model.Company
import org.linketinder.service.CompanyService

class CompanyController extends BaseController {
	private CompanyService companyService

	CompanyController(CompanyService companyService) {
		super()
		this.companyService = companyService
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.setJsonResponseHeaders(resp)
		String pathInfo = req.getPathInfo()

		if (pathInfo == null || pathInfo == "/") {
			List<Company> companies = companyService.listAllCompanies()

			if (companies == null || companies.isEmpty()) {
				resp.setStatus(HttpServletResponse.SC_NO_CONTENT)
				resp.getWriter().write(objectMapper.writeValueAsString(
						Map.of("message", "Nenhuma empresa encontrada")
				))
			}
			resp.getWriter().write(objectMapper.writeValueAsString(companies))
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.validateAcceptHeader(req, resp)
		this.setJsonResponseHeaders(resp)
		req.setCharacterEncoding("UTF-8")
		try {
			StringBuilder buffer = new StringBuilder()
			BufferedReader reader = req.getReader()
			String line
			while ((line = reader.readLine()) != null) {
				buffer.append(line)
			}
			Map<String, String> companyData = objectMapper.readValue(buffer.toString(), Map.class)
			String result = companyService.registerCompany(companyData)

			resp.setStatus(HttpServletResponse.SC_CREATED)
			resp.getWriter().write("{\"message\": \"" + result + "\"}")
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_CONFLICT)
			resp.getWriter().write("{\"error\": \"Erro ao processar dados: " + e.getMessage() + "\"}")
		}
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo()

		if (pathInfo == null || pathInfo == "/") {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST)
			resp.getWriter().write("{\"error\": \"ID da empresa é obrigatório\"}")
			return
		}
		try {
			int companyId = Integer.parseInt(pathInfo.substring(1))
			int status = companyService.removeCompany(companyId) ? HttpServletResponse.SC_NO_CONTENT : HttpServletResponse.SC_GONE
			resp.setStatus(status)
		} catch (NumberFormatException ignored) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST)
			resp.getWriter().write("{\"error\": \"ID inválido\"}")
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
			resp.getWriter().write("{\"error\": \"Erro ao excluir empresa: " + e.getMessage() + "\"}")
		}
	}
}