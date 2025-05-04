package org.linketinder.controller

import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.linketinder.model.Job
import org.linketinder.service.JobService

class JobController extends BaseController {
	private JobService jobService

	JobController(JobService jobService) {
		this.jobService = jobService
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		validateAcceptHeader(req, resp)
		setJsonResponseHeaders(resp)

		try {
			List<Job> jobs = jobService.listAllJobs()
			if (jobs == null || jobs.isEmpty()) {
				sendResponse(resp, HttpServletResponse.SC_NO_CONTENT, Map.of("message", "Nenhuma vaga encontrada"))
			} else {
				sendResponse(resp, HttpServletResponse.SC_OK, jobs)
			}
		} catch (Exception e) {
			sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao buscar vagas: ${e.message}")
		}
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		validateAcceptHeader(req, resp)
		setJsonResponseHeaders(resp)

		String pathInfo = req.getPathInfo()
		if (!pathInfo || !(pathInfo ==~ /^\/\d+$/)) {
			sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Formato de URL inválido. Esperado: /{jobId}")
			return
		}
		try {
			int jobId = Integer.parseInt(pathInfo.substring(1))
			int status = jobService.removeJobById(jobId) ? HttpServletResponse.SC_NO_CONTENT : HttpServletResponse.SC_GONE
			sendResponse(resp, status)
		} catch (NumberFormatException ignored) {
			sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "ID da vaga inválido")
		} catch (Exception e) {
			sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao excluir vaga: ${e.message}")
		}
	}
}
