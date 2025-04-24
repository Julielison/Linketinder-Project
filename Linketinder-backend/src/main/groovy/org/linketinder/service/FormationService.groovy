package org.linketinder.service

import org.linketinder.model.Formation

import java.time.LocalDate

class FormationService {
	static List<Formation> extractFormationsData(String formationsStr) {
		List<Formation> formations = []
		formationsStr.toString().split(';').each { String formationStr ->
			String[] formationData = formationStr.split(':')
			if (formationData.length > 0){
				Integer id = formationData[0].toInteger()
				String name = formationData[1]
				String institution = formationData[2]
				LocalDate date_start = LocalDate.parse(formationData[3])
				LocalDate date_end = LocalDate.parse(formationData[4])
				formations.add(new Formation(id, institution, name, date_start, date_end))
			}
		}
		return formations
	}

	static List<Formation> extractFormationsFromMap(Map<String, ?> data) {
		List<Formation> formations = []
		for (Map<String, ?> formationMap : data.get("formations") as List<Map<String, ?>>) {
			formations.add(new Formation(
					null,
					formationMap.get('institution') as String,
					formationMap.get('name') as String,
					formationMap.get('dateStart') as LocalDate,
					formationMap.get('dateEnd') as LocalDate
			))
		}
		return formations
	}
}
