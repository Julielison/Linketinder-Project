package org.linketinder.dao.interfaces

interface IJobDao {
	List<Map<String, Object>> getJobsRawData()
	boolean removeJobById(Integer id)
}