import editPen from "@/assets/img/edit-pen.svg";
import { Candidate } from "@/interfaces/Candidate";
import { mockCandidates } from "@/persistenceMock/candidatesMock";

export default function renderCandidateProfile(): HTMLElement {
    const profileContainer = document.createElement("div");
    profileContainer.className = "profile-container";

    // Recuperar o candidato atual do localStorage
    const candidateData: Candidate = mockCandidates[mockCandidates.length - 1];

    profileContainer.innerHTML = /*html*/`
        <div class="profile-header">
            <h2>${candidateData.name}</h2>
            <button class="edit-btn">
                <img src="${editPen}" alt="Editar" id="edit-profile" class="edit-icon" />
            </button>
        </div>

        <div class="profile-section">
            <h3>Dados pessoais</h3>
            <p><strong>Email:</strong> ${candidateData.email}</p>
            <p><strong>Idade:</strong> ${candidateData.age}</p>
        </div>

        <div class="profile-section">
            <h3>Formação acadêmica</h3>
            <p>${candidateData.formation.educationalInstitution} - ${candidateData.formation.startDate.toLocaleDateString()} - ${candidateData.formation.endDate.toLocaleDateString()}</p>
        </div>

        <div class="profile-section">
            <h3>Competências</h3>
            <p>${candidateData.skills.join(", ")}</p>
        </div>

        <div class="profile-section">
            <h3>Sobre</h3>
            <p>${candidateData.description}</p>
        </div>

        <div class="profile-section">
            <h3>Endereço</h3>
            <div class="address">
                <p><strong>CEP:</strong> ${candidateData.cep}</p>
                <p><strong>Estado:</strong> ${candidateData.state}</p>
                <p><strong>País:</strong> ${candidateData.country}</p>
            </div>
        </div>
    `;

    return profileContainer;
}
