import renderFormsCandidate from "@/components/formsCandidate";

export default function renderModalFormsCandidate(nameButtonSubmit: string): HTMLElement {
    const modal = document.createElement('div');
    modal.className = 'modal';

    const modalContent = document.createElement('div');
    modalContent.className = 'modal-content';

    const modalHeader = document.createElement('div');
    modalHeader.className = 'modal-header';
    modalHeader.innerHTML = `
        <h2>Editar Perfil</h2>
        <span class="close">&times;</span>
    `;

    const closeButton = modalHeader.querySelector('.close');
    closeButton?.addEventListener("click", () => {
        modal.remove();
    });

    // Adiciona o evento de fechar o modal ao clicar fora dele
    window.addEventListener("click", (event) => {
        if (event.target === modal) {
            modal.remove();
        }
    });

    const form = renderFormsCandidate(nameButtonSubmit);

    // Remover a possibilidade de editar o CPF
    const cpfInput = form.querySelector('input[name="cpf"]');
    if (cpfInput) {
        cpfInput.setAttribute('readonly', 'true');
    }

    modalContent.appendChild(modalHeader);
    modalContent.appendChild(form);
    modal.appendChild(modalContent);

    document.body.appendChild(modal);

    return modal;
}