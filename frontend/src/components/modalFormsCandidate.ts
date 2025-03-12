import renderFormsCandidate from "@/components/formsCandidate";
import { createModal } from "@/utils/modalUtils";

export default function renderModalFormsCandidate(nameButtonSubmit: string): HTMLElement {
    // Renderizar o formulário do candidato
    const form = renderFormsCandidate(nameButtonSubmit);

    // Remover a possibilidade de editar o CPF
    const cpfInput = form.querySelector('input[name="cpf"]');
    if (cpfInput) {
        cpfInput.setAttribute('readonly', 'true');
    }

    // Criar o modal com o formulário como conteúdo
    const modal = createModal("Editar Perfil", form);
    
    return modal;
}