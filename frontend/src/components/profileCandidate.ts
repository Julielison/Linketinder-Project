export default function renderCandidateProfile(): HTMLElement {
    const profileContainer = document.createElement("div");
    profileContainer.className = "profile-container";

    // Dados mockados
    const candidateData = {
        name: "Fulano de Lima",
        email: "fulano@gmail.com",
        age: 23,
        academic: "Sistemas para Internet - IFPB - 12/12/2012 - 12/12/2012",
        skills: ["Java", "Spring", "Docker"],
        about: "sknskdsn sonsksncksncks knckncksdkcsdbcjksdc skncksdksdbjcdbsjcdbsjdcbsjd sjsdjcbsjdcbsjdcbsjd jsdcjbjsdcjsdcbjsdcjbjsd",
        address: {
            cep: "49880-229",
            state: "Paraíba",
            country: "Brasil"
        }
    };

    profileContainer.innerHTML = /*html*/`
        <div class="profile-header">
            <h2>${candidateData.name}</h2>
            <button class="edit-btn">✏️</button>
        </div>

        <div class="profile-section">
            <h3>Dados pessoais</h3>
            <p><strong>Email:</strong> ${candidateData.email}</p>
            <p><strong>Idade:</strong> ${candidateData.age}</p>
        </div>

        <div class="profile-section">
            <h3>Formação acadêmica</h3>
            <p>${candidateData.academic}</p>
        </div>

        <div class="profile-section">
            <h3>Competências</h3>
            <p>${candidateData.skills.join(", ")}</p>
        </div>

        <div class="profile-section">
            <h3>Sobre</h3>
            <p>${candidateData.about}</p>
        </div>

        <div class="profile-section">
            <h3>Endereço</h3>
            <div class="address">
                <p><strong>CEP:</strong> ${candidateData.address.cep}</p>
                <p><strong>Estado:</strong> ${candidateData.address.state}</p>
                <p><strong>País:</strong> ${candidateData.address.country}</p>
            </div>
        </div>
    `;

    return profileContainer;
}
