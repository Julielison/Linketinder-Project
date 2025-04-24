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
}
