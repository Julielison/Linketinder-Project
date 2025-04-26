package org.linketinder.dao.interfaces

import org.linketinder.model.Candidate

interface IFormationDao {
	Candidate insertFormations(Candidate candidate);
}