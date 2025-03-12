import createJobElement, { mockJobs } from "@/components/jobComponent";


export default function renderJobsPage(): void {
    const tabButtonJobs = document.querySelector(".tab-button-jobs");
    if (tabButtonJobs) {
        tabButtonJobs.classList.add("active");
    }

    const jobListContainer = document.createElement("div");
    jobListContainer.className = "job-list";

    mockJobs.forEach(job => {
        const jobElement = createJobElement(job);
        jobListContainer.appendChild(jobElement);
    });

    const app = document.getElementById("app");
    if (app) {
        app.innerHTML = "";
        app.appendChild(jobListContainer);
    }
}