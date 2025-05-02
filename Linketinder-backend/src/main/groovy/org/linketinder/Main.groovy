package org.linketinder

import org.linketinder.config.DependencyFactory
import org.linketinder.controller.CandidateController
import org.linketinder.controller.CompanyController
import org.linketinder.controller.JobController
import org.linketinder.controller.SkillController
import org.linketinder.view.ViewFacade

class Main {
	static void main(String[] args) {
		DependencyFactory factory = DependencyFactory.getInstance()
		CandidateController candidateController = factory.getCandidateController()
		CompanyController companyController = factory.getCompanyController()
		JobController jobController = factory.getJobController()
		SkillController skillController = factory.getSkillController()
		ViewFacade view = factory.getView()

		System system = new System(
				view,
				candidateController,
				companyController,
				jobController,
				skillController
		)
		system.run()
	}
}