import renderCompanyProfile from "@/components/profileCompany";
import renderCandidatesPage from "./candidatesPage";

export default function renderCompanyPage(): void {
    // Remove o active de candidatos
    const tabButtonCandidates = document.querySelector(".tab-button-candidates");
    if (tabButtonCandidates) {
        tabButtonCandidates.classList.remove("active");
    }

    const mainContent: HTMLElement = renderCompanyProfile();
    const main = document.getElementById("app");

    if (main) {
        main.replaceChildren(mainContent);
    }

    const header = document.querySelector("header");

    if (!header) {
        console.warn("Elemento <header> não encontrado.");
        return;
    }

    // Verifica e adiciona a aba "Candidatos" caso não exista
    if (!header.querySelector(".tab-candidates")) {
        const tabCandidates = document.createElement("nav");
        tabCandidates.className = "tab-candidates";

        const candidatesButton = document.createElement("button");
        candidatesButton.textContent = "Candidatos";
        candidatesButton.className = "tab-button-candidates tab-button";
        candidatesButton.addEventListener("click", () => {
            renderCandidatesPage();
        });

        tabCandidates.appendChild(candidatesButton);
        header.appendChild(tabCandidates);
    }

    // Verifica e adiciona a aba "Perfil da Empresa" caso não exista
    if (!header.querySelector(".tab-company-profile")) {
        const tabProfile = document.createElement("nav");
        tabProfile.className = "tab-company-profile";

        const profileButton = document.createElement("button");
        profileButton.textContent = "Perfil";
        profileButton.className = "tab-button";
        profileButton.addEventListener("click", () => {
            renderCompanyPage();
        });

        tabProfile.appendChild(profileButton);
        header.appendChild(tabProfile);
    }
}
