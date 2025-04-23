package org.linketinder.dao

import groovy.sql.Sql
import org.linketinder.model.Candidate
import org.linketinder.model.Formation

import java.time.LocalDate

class FormationDao {
    Sql sql

    FormationDao(Sql sql){
        this.sql = sql
    }

    Candidate insertFormations(Candidate candidate) {
        try {
            candidate.formations.each { Formation formation ->
                List<List<Object>> result = sql.executeInsert(
                        """INSERT INTO formacao (nome,  instituicao) VALUES (?, ?)""",
                        [formation.nameCourse, formation.institution])
                if (result){
                    formation.setId(result[0][0] as Integer)
                }
            }
        } catch (Exception e) {
            e.printStackTrace()
        }
        return candidate
    }

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
}