import editPen from "@/assets/img/edit-pen.svg";
import { mockCompanies } from "@/dataMocked/companysMock";

export default function renderCompanyProfile(): HTMLElement {
    const profileContainer = document.createElement("div");
    profileContainer.className = "profile-container";
    
    // Recuperando os dados
    const empresaData = mockCompanies[mockCompanies.length - 1];
    
    profileContainer.innerHTML = /*html*/`
        <div class="profile-header">
            <h1>${empresaData.name}</h1>
            <div class="profile-buttons">
                <button class="publish-btn">Publicar Vaga</button>
                <button class="edit-btn">
                    <img src="${editPen}" alt="Editar" id="edit-profile" class="edit-icon" />
                </button>
            </div>
        </div>
        <div class="profile-section">
            <h3>Contato</h3>
            <p><strong>Email:</strong> ${empresaData.email}</p>
        </div>
        <div class="profile-section">
            <h3>Competências esperadas</h3>
            <p>${empresaData.expectedSkills.join(", ")}</p>
        </div>
        <div class="profile-section">
            <h3>Sobre</h3>
            <p>${empresaData.description}</p>
        </div>
        <div class="profile-section">
            <h3>CNPJ:</h3>
            <p>${empresaData.cnpj}</p>
        </div>
        <div class="profile-section">
            <h3>Endereço</h3>
            <div class="address">
                <p><strong>CEP:</strong> ${empresaData.cep}</p>
                <p><strong>Estado:</strong> ${empresaData.state}</p>
                <p><strong>País:</strong> ${empresaData.country}</p>
            </div>
        </div>
    `;
    
    return profileContainer;
}