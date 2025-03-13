import renderButtonStatistics from "@/components/buttonStatistics";
import createCandidateElement from "@/components/candidateComponent";
import { mockCandidates } from "@/dataMocked/candidatesMock";

export default function renderCandidatesPage(): void {
    const tabButtonCandidates = document.querySelector(".tab-button-candidates");
    if (tabButtonCandidates) {
        tabButtonCandidates.classList.add("active");
    }

    const candidateListContainer = document.createElement("div");
    candidateListContainer.className = "candidate-list";

    const buttonStatistcs = renderButtonStatistics();

    mockCandidates.forEach(candidate => {
        const candidateElement = createCandidateElement(candidate);
        candidateListContainer.appendChild(candidateElement);
    });

    const app = document.getElementById("app");
    if (app) {
        app.innerHTML = "";
        app.appendChild(candidateListContainer);
        app.insertAdjacentElement("afterbegin", buttonStatistcs);
    }
}