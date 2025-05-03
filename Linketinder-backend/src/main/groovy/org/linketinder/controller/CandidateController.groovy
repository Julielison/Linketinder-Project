package org.linketinder.controller

import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.linketinder.model.Candidate
import org.linketinder.service.CandidateService

class CandidateController extends BaseController {
	private CandidateService candidateService

	CandidateController(CandidateService candidateService) {
		super()
		this.candidateService = candidateService
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		setJsonResponseHeaders(resp)
		String pathInfo = req.getPathInfo()

		if (pathInfo == null || pathInfo == "/") {
			List<Candidate> candidatos = candidateService.listAllCandidates()

			if (candidatos == null || candidatos.isEmpty()) {
				resp.setStatus(HttpServletResponse.SC_NO_CONTENT)
				resp.getWriter().write(objectMapper.writeValueAsString(
						Map.of("message", "Nenhum candidato encontrado")
				))
			}
			resp.getWriter().write(objectMapper.writeValueAsString(candidatos))
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		validateAcceptHeader(req, resp)
		setJsonResponseHeaders(resp)

		try {
			StringBuilder buffer = new StringBuilder()
			BufferedReader reader = req.getReader()
			String line
			while ((line = reader.readLine()) != null) {
				buffer.append(line)
			}
			Map<String, String> dadosCandidato = objectMapper.readValue(buffer.toString(), Map.class)
			String resultado = candidateService.registerCandidate(dadosCandidato)

			resp.setStatus(HttpServletResponse.SC_CREATED)
			resp.getWriter().write("{\"mensagem\": \"" + resultado + "\"}")
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_CONFLICT)
			resp.getWriter().write("{\"erro\": \"Erro ao processar dados: " + e.getMessage() + "\"}")
		}
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		validateAcceptHeader(req, resp)
		setJsonResponseHeaders(resp)

		String pathInfo = req.getPathInfo()

		if (pathInfo == null || pathInfo == "/") {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST)
			resp.getWriter().write("{\"erro\": \"ID do candidato é obrigatório\"}")
			return
		}

		try {
			int candidatoId = Integer.parseInt(pathInfo.substring(1))
			String resultado = candidateService.removeCandidate(candidatoId)

			resp.getWriter().write("{\"mensagem\": \"" + resultado + "\"}")
		} catch (NumberFormatException ignored) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST)
			resp.getWriter().write("{\"erro\": \"ID inválido\"}")
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
			resp.getWriter().write("{\"erro\": \"Erro ao excluir candidato: " + e.getMessage() + "\"}")
		}
	}
}