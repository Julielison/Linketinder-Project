import like from '@/assets/img/like.svg';
import { Vacancy } from '@/interfaces/Vacancy';
import { mockJobs } from '@/persistenceMock/VacancysMock';

export default function createJobElement(job: Vacancy): HTMLElement {
    const jobElement = document.createElement("div");
    jobElement.className = "job";

    // Recuperar o candidato atual do localStorage
    const savedCandidateData = localStorage.getItem("candidateData");
    const currentCandidate = savedCandidateData ? JSON.parse(savedCandidateData) : null;
    
    // Verificar se o candidato atual curtiu esta vaga
    const candidateId = currentCandidate?.id;
    const isLikedByCandidate = job.likedByCandidatesId?.includes(candidateId);

    // Verificar se a empresa curtiu o candidato
    const candidateIsLikedByCompany = currentCandidate?.likedByCompanysId?.includes(job.idCompany);
    
    // Determinar o nome da empresa a ser exibido
    // Só mostra o nome real da empresa se ambos deram match
    const displayCompanyName = isLikedByCandidate && candidateIsLikedByCompany ? job.companyName : "Empresa Anônima";

    // Verificar se o candidato curtiu a vaga para a classe CSS
    const isLiked = isLikedByCandidate;

    jobElement.innerHTML = /*html*/`
        <div class='group-left'>
            <div class="job-header">
                <h3>${job.title}</h3>
            </div>
            <div class='location-company'>
                <div class="job-location">${job.location}</div>
                <div class="job-company">${displayCompanyName}</div>
            </div>
            <div class="job-tags">
                ${job.skills.map(tag => `<span class="job-tag">#${tag}</span>`).join(" ")}
            </div>
        </div>
        <div class="job-actions">
            <button class="like-button" data-job-id="${job.id}">
                <img src="${like}" alt="Like Logo" class="like-logo ${isLiked ? 'liked' : ''}" />
            </button>
        </div>
    `;

    // Adicionar evento de clique para o botão de curtir
    const likeButton = jobElement.querySelector('.like-button');
    if (likeButton) {
        likeButton.addEventListener('click', () => {
            // Se não houver candidato logado, não faz nada
            if (!currentCandidate) return;
            
            // Encontra a vaga correspondente nos dados mockados
            const jobId = Number(likeButton.getAttribute('data-job-id'));
            const jobToUpdate = mockJobs.find(j => j.id === jobId);
            
            if (jobToUpdate) {
                // Inicializa o array se ele não existir
                if (!jobToUpdate.likedByCandidatesId) {
                    jobToUpdate.likedByCandidatesId = [];
                }
                
                // Verifica se o candidato já curtiu
                const index = jobToUpdate.likedByCandidatesId.indexOf(currentCandidate.id);
                
                if (index === -1) {
                    // Adicionar o like
                    jobToUpdate.likedByCandidatesId.push(currentCandidate.id);
                    likeButton.querySelector('.like-logo')?.classList.add('liked');
                    
                    // Verificar se agora existe um match
                    const hasMatch = currentCandidate.likedByCompanysId?.includes(jobToUpdate.idCompany);
                    
                    // Atualizar a exibição do nome da empresa - só mostra se houver match
                    const companyElement = jobElement.querySelector('.job-company');
                    if (companyElement && hasMatch) {
                        companyElement.textContent = jobToUpdate.companyName;
                    }
                } else {
                    // Remover o like
                    jobToUpdate.likedByCandidatesId.splice(index, 1);
                    likeButton.querySelector('.like-logo')?.classList.remove('liked');
                    
                    // Atualizar a exibição do nome da empresa para anônimo
                    const companyElement = jobElement.querySelector('.job-company');
                    if (companyElement) {
                        companyElement.textContent = "Empresa Anônima";
                    }
                }
                
                // Salvar o estado atualizado
                if (currentCandidate && !currentCandidate.likedJobs) {
                    currentCandidate.likedJobs = [];
                }
                
                // Atualizar os likes do candidato
                if (index === -1) {
                    // Adicionar à lista de vagas que o candidato curtiu
                    currentCandidate.likedJobs.push(jobId);
                } else {
                    // Remover da lista de vagas que o candidato curtiu
                    const likedJobIndex = currentCandidate.likedJobs.indexOf(jobId);
                    if (likedJobIndex !== -1) {
                        currentCandidate.likedJobs.splice(likedJobIndex, 1);
                    }
                }
                
                // Atualizar localStorage
                localStorage.setItem("candidateData", JSON.stringify(currentCandidate));
            }
        });
    }

    return jobElement;
}

// Função para criar todos os elementos de vaga de uma vez
export function createJobList(): HTMLElement[] {
    return mockJobs.map(job => createJobElement(job));
}