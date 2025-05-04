package org.linketinder.controller

import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.linketinder.model.Company
import org.linketinder.model.Job
import org.linketinder.service.CompanyService
import org.linketinder.service.JobService

import java.util.regex.Matcher

class CompanyController extends BaseController {
	private final CompanyService companyService
	private final JobService jobService

	CompanyController(CompanyService companyService, JobService jobService) {
		super()
		this.companyService = companyService
		this.jobService = jobService
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		validateAcceptHeader(request, response)
		setJsonResponseHeaders(response)
		String pathInfo = request.getPathInfo()

		if (pathInfo == null || pathInfo == "/") {
			this.getAllCompanies(response)
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Endpoint não encontrado")
		}
	}

	private void getAllCompanies(HttpServletResponse response) throws IOException {
		List<Company> companies = companyService.listAllCompanies()

		if (companies == null || companies.isEmpty()) {
			sendResponse(response, HttpServletResponse.SC_NO_CONTENT,
					Map.of("message", "No companies found"))
			return
		}
		sendResponse(response, HttpServletResponse.SC_OK, companies)
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8")
		validateAcceptHeader(request, response)
		setJsonResponseHeaders(response)

		String pathInfo = request.getPathInfo()
		String requestBody = readRequestBody(request)

		if (!pathInfo || pathInfo == "/") {
			createCompany(requestBody, response)
			return
		}

		Matcher matcher = (pathInfo =~ /^\/(\d+)\/jobs\/?$/) // /{id}/jobs
		if (matcher.matches()) {
			try {
				int companyId = Integer.parseInt(matcher.group(1))
				createJobForCompany(companyId, requestBody, response)
			} catch (NumberFormatException ignored) {
				sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "ID da empresa inválido")
			}
		} else {
			sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Endpoint inválido")
		}
	}


	private void createCompany(String requestBody, HttpServletResponse response) throws IOException {
		try {
			Map<String, Object> companyData = objectMapper.readValue(requestBody, Map.class)
			Company result = companyService.registerCompany(companyData)

			sendResponse(response, HttpServletResponse.SC_CREATED, result)
		} catch (Exception e) {
			sendErrorResponse(response, HttpServletResponse.SC_CONFLICT, "Error processing data: " + e.getMessage())
		}
	}

	private void createJobForCompany(int companyId, String requestBody, HttpServletResponse response) throws IOException {
		try {
			Map<String, Object> jobData = objectMapper.readValue(requestBody, Map.class)
			Job createdJob = jobService.registerJob(companyId, jobData)

			sendResponse(response, HttpServletResponse.SC_CREATED, createdJob)
		} catch (Exception e) {
			sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error creating job: " + e.getMessage())
		}
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		validateAcceptHeader(request, response)
		setJsonResponseHeaders(response)
		String pathInfo = request.getPathInfo()

		if (pathInfo == null || pathInfo == "/") {
			sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Company ID is required")
			return
		}
		Matcher matcher = (pathInfo =~ /^\/(\d+)$/) // /{id}

		try {
			if (matcher.matches()) {
				int companyId = matcher[0][1] as int
				deleteCompany(companyId, response)
			} else {
				sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid endpoint")
			}
		} catch (NumberFormatException ignored) {
			sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid company ID")
		}
	}

	private void deleteCompany(int companyId, HttpServletResponse response) throws IOException {
		try {
			int status = companyService.removeCompany(companyId) ? HttpServletResponse.SC_NO_CONTENT : HttpServletResponse.SC_GONE
			sendResponse(response, status)
		} catch (Exception e) {
			sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error deleting company: " + e.getMessage())
		}
	}
}