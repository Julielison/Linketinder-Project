import like from '@/assets/img/like.svg';
import { Candidate } from '@/interfaces/ICandidate';
import { mockCandidates } from '@/dataMocked/candidatesMock';

export default function createCandidateElement(candidate: Candidate): HTMLElement {
    const candidateElement = document.createElement("div");
    candidateElement.className = "candidate";

    // Recuperar a empresa atual do localStorage
    const savedCompanyData = localStorage.getItem("companyData");
    const currentCompany = savedCompanyData ? JSON.parse(savedCompanyData) : null;
    
    // Verificar se a empresa atual curtiu este candidato
    const companyId = currentCompany?.id;
    const isLikedByCompany = candidate.likedByCompaniesId?.includes(companyId);

    // Verificar se o candidato curtiu a empresa
    const companyIsLikedByCandidate = candidate.likedByCompaniesId?.includes(companyId);
    
    // Determinar o nome do candidato a ser exibido
    // Só mostra o nome real do candidato se ambos deram match
    const displayCandidateName = isLikedByCompany && companyIsLikedByCandidate ? candidate.name : "Candidato Anônimo";

    const isLiked = isLikedByCompany;

    // Recuperando dados estruturados
    const education = candidate.formation.educationalInstitution || "Não informado";
    const location = candidate.country || "Localização não informada";

    candidateElement.innerHTML = /*html*/`
        <div class='group-left'>
            <div class="candidate-header">
                <h3>${displayCandidateName}</h3>
            </div>
            <div class='location-education'>
                <div class="candidate-location">${location}</div>
                <div class="candidate-education">${education}</div>
            </div>
            <div class="candidate-skills">
                ${candidate.skills.map(skill => `<span class="candidate-skill">#${skill}</span>`).join(" ")}
            </div>
        </div>
        <div class="candidate-actions">
            <button class="like-button" data-candidate-id="${candidate.id}">
                <img src="${like}" alt="Like Logo" class="like-logo ${isLiked ? 'liked' : ''}" />
            </button>
        </div>
    `;

    // Adicionar evento de clique para o botão de curtir
    const likeButton = candidateElement.querySelector('.like-button');
    if (likeButton) {
        likeButton.addEventListener('click', () => {
            // Se não houver empresa logada, não faz nada
            if (!currentCompany) return;
            
            // Encontra o candidato correspondente nos dados mockados
            const candidateId = Number(likeButton.getAttribute('data-candidate-id'));
            const candidateToUpdate = mockCandidates.find(c => c.id === candidateId);
            
            if (candidateToUpdate) {
                // Inicializa o array se ele não existir
                if (!candidateToUpdate.likedByCompaniesId) {
                    candidateToUpdate.likedByCompaniesId = [];
                }
                
                // Verifica se a empresa já curtiu
                const index = candidateToUpdate.likedByCompaniesId.indexOf(currentCompany.id);
                
                if (index === -1) {
                    // Adicionar o like
                    candidateToUpdate.likedByCompaniesId.push(currentCompany.id);
                    likeButton.querySelector('.like-logo')?.classList.add('liked');
                    
                    // Verificar se agora existe um match
                    const hasMatch = candidateToUpdate.likedByCompaniesId?.includes(currentCompany.id);
                    
                    // Atualizar a exibição do nome do candidato - só mostra se houver match
                    const candidateNameElement = candidateElement.querySelector('.candidate-header h3');
                    if (candidateNameElement && hasMatch) {
                        candidateNameElement.textContent = candidateToUpdate.name;
                    }
                } else {
                    // Remover o like
                    candidateToUpdate.likedByCompaniesId.splice(index, 1);
                    likeButton.querySelector('.like-logo')?.classList.remove('liked');
                    
                    // Atualizar a exibição do nome do candidato para anônimo
                    const candidateNameElement = candidateElement.querySelector('.candidate-header h3');
                    if (candidateNameElement) {
                        candidateNameElement.textContent = "Candidato Anônimo";
                    }
                }
                
                // Salvar o estado atualizado
                if (currentCompany && !currentCompany.likedCandidates) {
                    currentCompany.likedCandidates = [];
                }
                
                // Atualizar os likes da empresa
                if (index === -1) {
                    // Adicionar à lista de candidatos que a empresa curtiu
                    currentCompany.likedCandidates.push(candidateId);
                } else {
                    // Remover da lista de candidatos que a empresa curtiu
                    const likedCandidateIndex = currentCompany.likedCandidates.indexOf(candidateId);
                    if (likedCandidateIndex !== -1) {
                        currentCompany.likedCandidates.splice(likedCandidateIndex, 1);
                    }
                }
                
                // Atualizar localStorage
                localStorage.setItem("companyData", JSON.stringify(currentCompany));
            }
        });
    }

    return candidateElement;
}