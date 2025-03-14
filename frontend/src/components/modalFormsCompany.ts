import renderFormsCompany from "@/components/formsCompany";
import { createModal } from "@/utils/modalUtils";

export default function renderModalFormsCompany(nameButtonSubmit: string): HTMLElement {
    // Renderizar o formulário da empresa
    const form = renderFormsCompany(nameButtonSubmit);

    // Remover a possibilidade de editar o CNPJ
    const cnpjInput = form.querySelector('input[name="cnpj"]');
    if (cnpjInput) {
        cnpjInput.setAttribute('readonly', 'true');
    }

    // Criar o modal com o formulário como conteúdo
    const modal = createModal("Editar Perfil", form);
    
    return modal;
}