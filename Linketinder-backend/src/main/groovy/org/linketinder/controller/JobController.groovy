package org.linketinder.controller

import org.linketinder.model.Job
import org.linketinder.service.JobService

class JobController {
	JobService jobService

	JobController(JobService jobService) {
		this.jobService = jobService
	}

	List<Job> getAllJobs(){
		return jobService.listAllJobs()
	}

	String deleteJobById(Integer id){
		return jobService.removeJobById(id)
	}

	String createJob(Integer idCompany, Map<String, ?> data){
		return jobService.registerJob(idCompany, data)
	}
}
