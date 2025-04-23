package org.linketinder.view

import org.linketinder.enums.MenuOption
import org.linketinder.model.Job
import org.linketinder.model.Person
import org.linketinder.model.Skill

class ViewFacade {
	CandidateView candidateView
	CompanyView companyView
	JobView jobView
	GenericView genericView
	SkillView skillView

	ViewFacade() {
		GenericView genericView = new GenericView()
		SkillView skillView = new SkillView(genericView)
		this.candidateView = new CandidateView(genericView, skillView)
		this.companyView = new CompanyView(genericView)
		this.jobView = new JobView(genericView, skillView)
		this.skillView = skillView
		this.genericView = genericView
	}

	void showCandidates(List<Person> candidates){
		candidateView.showCandidates(candidates)
	}

	void showMenu(){
		genericView.showMenu()
	}

	String getUserInput(){
		return genericView.getUserInput()
	}

	Map<String, ?> getCandidateInput(){
		return candidateView.getCandidateInput()
	}

	void showFeedback(String feedback){
		genericView.showFeedback(feedback)
	}

	Integer getIdCandidateInput(){
		return candidateView.getIdCandidateInput()
	}

	void showCompanies(List<Person> companies){
		companyView.showCompanies(companies)
	}

	Map<String, ?> getCompanyInput(){
		return companyView.getCompanyInput()
	}

	Integer getIdCompanyInput(){
		return companyView.getIdCompanyInput()
	}

	void showJobs(List<Job> jobs){
		jobView.showJobs(jobs)
	}

	Map<String, ?> getDataJobInput(){
		return jobView.getDataJobInput()
	}

	Integer getIdJobInput(){
		return jobView.getIdJobInput()
	}

	void showSkills(List<Skill> skills){
		skillView.showSkills(skills)
	}

	Integer getIdSkillInput(){
		return skillView.getIdSkillInput()
	}

	void showExitMessage(){
		genericView.showExitMessage()
	}

	void showInvalidOption(){
		genericView.showInvalidOption()
	}
}
