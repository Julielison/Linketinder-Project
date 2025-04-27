package org.linketinder.dao.impl

import groovy.sql.Sql
import org.linketinder.dao.interfaces.IFormationDao
import org.linketinder.model.Candidate
import org.linketinder.model.Formation

class FormationDao implements IFormationDao {
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
}