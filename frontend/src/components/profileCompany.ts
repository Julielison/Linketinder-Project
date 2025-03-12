export default function renderCompanyProfile(): HTMLElement {
    const profileContainer = document.createElement("div");
    profileContainer.className = "profile-container";
    
    // Dados mockados baseados na imagem
    const empresaData = {
        name: "Empresa X",
        contato: {
            email: "fulano@gmail.com"
        },
        competencias: ["java", "spring", "docker"],
        sobre: "sknskdsn sonsksncksncks knckncksdkcsdbcjksdc skncksdksdbjcdbsjcdbsjdcbsjd sjsdjcbsjdcbsjdcbsjd jsdcjbjsdcjsdcbjsdcjbjsd",
        cnpj: "13.638.767/0001-92",
        endereco: {
            cep: "49880-229",
            estado: "Paraíba",
            pais: "Brasil"
        }
    };
    
    profileContainer.innerHTML = /*html*/`
        <div class="profile-header">
            <h1>${empresaData.name}</h1>
            <button class="edit-btn">
                <span class="publish-btn"><strong>Publicar Vaga</strong></span> ✚
            </button>
        </div>
        <div class="profile-section">
            <h3>Contato</h3>
            <p><strong>Email:</strong> ${empresaData.contato.email}</p>
        </div>
        <div class="profile-section">
            <h3>Competências esperadas</h3>
            <p>${empresaData.competencias.join(", ")}</p>
        </div>
        <div class="profile-section">
            <h3>Sobre</h3>
            <p>${empresaData.sobre}</p>
        </div>
        <div class="profile-section">
            <h3>CNPJ:</h3>
            <p>${empresaData.cnpj}</p>
        </div>
        <div class="profile-section">
            <h3>Endereço</h3>
            <div class="address">
                <p><strong>CEP:</strong> ${empresaData.endereco.cep}</p>
                <p><strong>Estado:</strong> ${empresaData.endereco.estado}</p>
                <p><strong>País:</strong> ${empresaData.endereco.pais}</p>
            </div>
        </div>
    `;
    
    return profileContainer;
}