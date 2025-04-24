package org.linketinder.dao.interfaces

import org.linketinder.model.Job

interface IJobDao {
	List<Map<String, Object>> getJobsRawData()
}