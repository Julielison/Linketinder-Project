import like from '@/assets/img/like.svg';

interface Candidate {
    name: string;
    location: string;
    education: string;
    skills: string[];
}

export const mockCandidates: Candidate[] = [
    { name: "Anônimo", location: "Local Y", education: "Sistemas para internet", skills: ["java", "groovy", "dart"] },
    { name: "Anônimo", location: "Local Y", education: "Faculdade X", skills: ["java", "groovy", "dart"] },
    { name: "Anônimo", location: "Local Y", education: "Faculdade X", skills: ["java", "groovy", "dart"] },
    { name: "Fulano de tal", location: "Local Y", education: "Faculdade X", skills: ["java", "groovy", "dart"] }
];

export default function createCandidateElement(candidate: Candidate): HTMLElement {
    const candidateElement = document.createElement("div");
    candidateElement.className = "candidate";

    candidateElement.innerHTML = /*html*/`
        <div class='group-left'>
            <div class="candidate-header">
                <h3>${candidate.name}</h3>
            </div>
            <div class='location-education'>
                <div class="candidate-location">${candidate.location}</div>
                <div class="candidate-education">${candidate.education}</div>
            </div>
            <div class="candidate-skills">
                ${candidate.skills.map(skill => `<span class="candidate-skill">#${skill}</span>`).join(" ")}
            </div>
        </div>
        <div class="candidate-actions">
            <button class="like-button">
                <img src="${like}" alt="Like Logo" class="like-logo" />
            </button>
        </div>
    `;

    return candidateElement;
}