package org.linketinder.config

import org.apache.catalina.LifecycleException
import org.apache.catalina.startup.Tomcat
import org.linketinder.controller.CandidateController
import org.linketinder.controller.CompanyController
import org.linketinder.controller.JobController
import org.linketinder.controller.SkillController

class TomcatServer implements Runnable {
	private CandidateController candidateController
	private CompanyController companyController
	private JobController jobController
	private SkillController skillController

	TomcatServer(CandidateController candidateController,
				 CompanyController companyController = null,
				 JobController jobController = null,
				 SkillController skillController = null) {
		this.candidateController = candidateController
		this.companyController = companyController
		this.jobController = jobController
		this.skillController = skillController
	}

	@Override
	void run() throws LifecycleException {
		def tomcat = new Tomcat()
		tomcat.setPort(8080)
		tomcat.getConnector()
		tomcat.setBaseDir("build/tomcat")

		def ctx = tomcat.addContext("", new File(".").absolutePath)

		Tomcat.addServlet(ctx, "CandidateController", candidateController)
		ctx.addServletMappingDecoded("/candidates/*", "CandidateController")

		Tomcat.addServlet(ctx, "CompanyController", companyController)
		ctx.addServletMappingDecoded("/companies/*", "CompanyController")

		tomcat.start()
		println "Servidor Tomcat iniciado em http://localhost:8080"

		tomcat.getServer().await()
	}
}