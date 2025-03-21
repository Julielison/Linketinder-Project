import { mockCandidates } from "@/dataMocked/candidatesMock";
import { Candidate } from "@/interfaces/ICandidate";
import renderPageProfileCandidate from "@/pages/candidatePage";
import { validateAge, validateCEP, validateCPF, validateCountry, validateDescription, validateEmail, validateLinkedIn, validateNameCandidate, validateNumber, validateSkills, validateState } from "@/utils/validations";
import { setupInputValidation, validateAllFields } from "@/utils/validateSubmit";

export default function renderFormsCandidate(nameButtonSubmit: string): HTMLElement {
    const form = document.createElement('form');
    form.id = 'form';

    form.innerHTML = /*html*/`
        <div class="inline-group">
            <label class="name">Nome* 
                <input type="text" name="name" placeholder="Digite seu nome completo" required />
                <span class="error-message">Nome inválido. Deve conter pelo menos 3 caracteres.</span>
            </label>
            <label>Idade*
                <input type="number" name="age" placeholder="Ex: 25" required />
                <span class="error-message">Idade inválida. Deve ser entre 16 e 100 anos.</span>
            </label>
        </div>
        <label class="email">Email*
            <input type="email" name="email" placeholder="Digite seu email" required />
            <span class="error-message">Email inválido.</span>
        </label>
        <label>Número de Telefone*
            <input type="text" name="phone" placeholder="Digite apenas números (incluindo ddd)" required />
            <span class="error-message">Número de telefone inválido.</span>
        </label>
        <label>Linkedin*
            <input type="text" name="linkedin" placeholder="https://linkedin.com.br/in/seu_usuário" required />
            <span class="error-message">LinkedIn inválido.</span>
        </label>
        <label>Competências (separadas por vírgula)* 
            <input type="text" name="skills" placeholder="Ex: Python, React, Inglês fluente" required />
            <span class="error-message">Competências inválidas.</span>
        </label>
        <label>Descrição Pessoal 
            <textarea name="description" placeholder="Fale um pouco sobre você" required></textarea>
            <span class="error-message">Descrição inválida. Deve conter entre 10 e 500 caracteres.</span>
        </label>
        <fieldset>
            <legend>Formação Acadêmica</legend>
            <label>Curso - Instituição*
                <input type="text" name="formationInstitution" placeholder="Ex: Ciência da Computação - UFSP" required />
                <span class="error-message">Curso/Instituição inválido. Deve conter entre 5 e 100 caracteres.</span>
            </label>
            <div class="inline-group date">
                <label>Data de Início: 
                    <input type="date" name="start_date" />
                </label>
                <label>Data de Fim: 
                    <input type="date" name="end_date" />
                </label>
        </fieldset>
        <label>CPF: 
            <input type="text" name="cpf" placeholder="Digite seu cpf" required/>
            <span class="error-message">CPF inválido.</span>
        </label>
        <div class="inline-group">
            <label>CEP: 
                <input type="text" name="cep" placeholder="Digite seu cep" required/>
                <span class="error-message">CEP inválido.</span>
            </label>
            <label>País: 
                <input type="text" name="country" placeholder="Digite seu país" required />
                <span class="error-message">País inválido. Deve conter entre 2 e 50 caracteres.</span>
            </label>
            <label>Estado: 
                <input type="text" name="state" placeholder="Digite seu estado" required />
                <span class="error-message">Estado inválido. Deve conter entre 2 e 50 caracteres.</span>
            </label>
        </div>
        <div class="form-actions">
            <button class="submit" type="submit">${nameButtonSubmit}</button>
        </div>
    `;

    const validationRules = [
        { name: 'name', validator: validateNameCandidate, errorMsg: "Digite um nome e sobrenome válido!", tag: 'input' },
        { name: 'email', validator: validateEmail, errorMsg: "Email inválido", tag: 'input' },
        { name: 'phone', validator: validateNumber, errorMsg: "Número deve conter exatamente 11 dígitos!", tag: 'input' },
        { name: 'cpf', validator: validateCPF, errorMsg: "O CPF deve conter exatamente 11 dígitos!", tag: 'input' },
        { name: 'cep', validator: validateCEP, errorMsg: "O CEP deve conter exatamente 8 dígitos!", tag: 'input' },
        { name: 'age', validator: validateAge, errorMsg: "Idade mínima > 15", tag: 'input' },
        { name: 'skills', validator: validateSkills, errorMsg: "As competências devem ser separadas por vírgula!", tag: 'input' },
        { name: 'linkedin', validator: validateLinkedIn, errorMsg: "Siga essa formatação: https://linkedin.com.br/in/seu_usuário", tag: 'input' },
        { name: 'description', validator: validateDescription, errorMsg: "A descrição deve conter entre 10 e 500 caracteres!", tag: 'textarea' },
        { name: 'country', validator: validateCountry, errorMsg: "O país deve ter entre 2 e 50 caracteres!", tag: 'input' },
        { name: 'state', validator: validateState, errorMsg: "O estado deve ter entre 2 e 50 caracteres!", tag: 'input' }
    ];
    
    setupInputValidation(form, validationRules);

    form.addEventListener("submit", (event) => {
        event.preventDefault();
        
        if (!validateAllFields(form, validationRules)) {
            return;
        }

        const formData = new FormData(form);
        const formValues: Record<string, string> = {};

        formData.forEach((value, key) => {
            formValues[key] = value.toString();
        });

        const candidate: Candidate = {
            id: 23, // ID fixo para demo
            name: formValues.name,
            email: formValues.email,
            cpf: formValues.cpf,
            age: parseInt(formValues.age) || 0,
            skills: formValues.skills.split(',').map(skill => skill.trim()),
            formation: {
                educationalInstitution: formValues.formationInstitution,
                startDate: new Date(formValues.start_date),
                endDate: new Date(formValues.end_date)
            },
            description: formValues.description,
            likedByCompaniesId: [1], // Empresa com id 1 curtiu o candidato
            cep: formValues.cep,
            country: formValues.country,
            state: formValues.state,
            phone: formValues.phone
        };
        
        mockCandidates.push(candidate);

        renderPageProfileCandidate();
    });

    return form;
}
