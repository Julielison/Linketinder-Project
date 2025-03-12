/**
 * Cria uma estrutura base de modal reutilizável
 * @param title - Título do modal
 * @param content - Elemento HTML ou string que será o conteúdo do modal
 * @returns Elemento HTML do modal
 */
export function createModal(title: string, content: HTMLElement | string): HTMLElement {
    // Adicionar classe ao body para impedir scroll
    document.body.classList.add('modal-open');

    // Criar a estrutura do modal
    const modal = document.createElement('div');
    modal.className = 'modal';

    const modalContent = document.createElement('div');
    modalContent.className = 'modal-content';

    // Criar o cabeçalho do modal
    const modalHeader = document.createElement('div');
    modalHeader.className = 'modal-header';
    modalHeader.innerHTML = `
        <h2>${title}</h2>
        <span class="close">&times;</span>
    `;

    // Função para fechar o modal
    const closeModal = () => {
        modal.remove();
        // Remover classe do body para permitir scroll novamente
        document.body.classList.remove('modal-open');
    };

    // Adicionar evento de fechar ao botão de fechar
    const closeButton = modalHeader.querySelector('.close');
    closeButton?.addEventListener("click", closeModal);

    // Adicionar evento de fechar ao clicar fora do modal
    window.addEventListener("click", (event) => {
        if (event.target === modal) {
            closeModal();
        }
    });

    // Adicionar conteúdo ao modal
    modalContent.appendChild(modalHeader);
    
    // Verificar se o conteúdo é um elemento HTML ou uma string
    if (typeof content === 'string') {
        const contentElement = document.createElement('div');
        contentElement.innerHTML = content;
        modalContent.appendChild(contentElement);
    } else {
        modalContent.appendChild(content);
    }

    modal.appendChild(modalContent);
    document.body.appendChild(modal);

    return modal;
}