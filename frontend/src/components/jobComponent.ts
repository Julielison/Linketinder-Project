interface Job {
    title: string;
    location: string;
    company: string;
    tags: string[];
}

export const mockJobs: Job[] = [
    {
        title: "FullStack Developer",
        location: "Local",
        company: "Anônima",
        tags: ["java", "groovy", "dart"]
    },
    {
        title: "FullStack Developer",
        location: "Local",
        company: "Anônima",
        tags: ["java", "groovy", "dart"]
    },
    {
        title: "FullStack Developer",
        location: "Local",
        company: "Anônima",
        tags: ["java", "groovy", "dart"]
    },
    {
        title: "FullStack Developer",
        location: "Local",
        company: "ZG soluções",
        tags: ["java", "groovy", "dart"]
    }
];


export default function createJobElement(job: Job): HTMLElement {
    const jobElement = document.createElement("div");
    jobElement.className = "job";

    jobElement.innerHTML = /*html*/`
        <div class="job-header">
            <h3>${job.title}</h3>
        </div>
        <div class='location-company'>
            <div class="job-location">${job.location}</div>
            <div class="job-company">${job.company}</div>
        </div>
        <div class="job-tags">
            ${job.tags.map(tag => `<span class="job-tag">#${tag}</span>`).join(" ")}
        </div>
        <div class="job-actions">
            <button class="like-button">👍</button>
        </div>
    `;

    return jobElement;
}