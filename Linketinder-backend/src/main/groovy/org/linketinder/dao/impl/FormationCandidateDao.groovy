	package org.linketinder.dao.impl

	import groovy.sql.Sql

	import org.linketinder.model.Candidate
	import org.linketinder.model.Formation

	class FormationCandidateDao {
		Sql sql

		FormationCandidateDao(Sql sql) {
			this.sql = sql
		}

		void insertIdsAndDatesFromFormation(Candidate candidate) {
			try {
				String valuesSql = candidate.formations.collect { "(?, ?, ?, ?)" }.join(", ")
				String sqlQuery = """
				INSERT INTO formacao_candidato (id_formacao, id_candidato, data_inicio, data_fim_previsao)
				VALUES ${valuesSql}"""

				List<Object> params = candidate.formations.collectMany { Formation formation ->
					[
							formation.id,
							candidate.id,
							formation.dateStart,
							formation.dateEnd
					]
				}
				sql.execute(sqlQuery, params)
			} catch (Exception e) {
				throw new Exception(e)
			}
		}
	}
