import renderCandidateProfile from "@/components/profileCandidate";

export default function renderPageProfileCandidate(): void {
    const mainContent: HTMLElement = renderCandidateProfile();
    const main = document.getElementById("app");

    if (main) {
        main.replaceChildren(mainContent);
    }

    const header = document.querySelector("header");

    if (!header) {
        console.warn("Elemento <header> não encontrado.");
        return;
    }

    // Verifica e adiciona a aba "Vagas" caso não exista
    if (!header.querySelector(".tab-jobs")) {
        const tabJobs = document.createElement("nav");
        tabJobs.className = "tab-jobs";

        const vagasButton = document.createElement("button");
        vagasButton.textContent = "Vagas";
        vagasButton.className = "tab-button";
        vagasButton.addEventListener("click", () => {
            alert("Você clicou em Vagas!");
        });

        tabJobs.appendChild(vagasButton);
        header.appendChild(tabJobs);
    }

    // Verifica e adiciona a aba "Perfil do Candidato" caso não exista
    if (!header.querySelector(".tab-profile")) {
        const tabProfile = document.createElement("nav");
        tabProfile.className = "tab-profile";

        const profileButton = document.createElement("button");
        profileButton.textContent = "Perfil";
        profileButton.className = "tab-button";
        profileButton.addEventListener("click", () => {
            renderPageProfileCandidate();
        });

        tabProfile.appendChild(profileButton);
        header.appendChild(tabProfile);
    }
}
