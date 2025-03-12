import renderPageProfileCandidate from "@/pages/pageCandidate";
import { Candidate } from "@/interfaces/Candidate";

export default function renderFormsCandidate(nameButtonSubmit: string): HTMLElement {
    const form = document.createElement('form');
    form.id = 'form';

    // Tentar recuperar dados existentes do localStorage
    const savedData = localStorage.getItem("candidateData");
    const mockUser = savedData ? JSON.parse(savedData) : {
        name: "Fulano de Lima",
        age: 23,
        email: "fulano@gmail.com",
        skills: "Java, Spring, Docker",
        description: "Profissional experiente em desenvolvimento backend...",
        formation: "Ciência da Computação - UFSP",
        start_date: "2015-02-01",
        end_date: "2019-12-15",
        cpf: "123.456.789-00",
        cep: "49880-229",
        country: "Brasil",
        state: "Paraíba"
    };

    form.innerHTML = /*html*/`
        <div class="inline-group">
            <label class="name">Nome: 
                <input type="text" name="name" value="${mockUser.name}" placeholder="Digite seu nome completo" required />
            </label>
            <label>Idade: 
                <input type="number" name="age" value="${mockUser.age}" placeholder="Ex: 25" required />
            </label>
        </div>
        <label class="email">Email: 
            <input type="email" name="email" value="${mockUser.email}" placeholder="Digite seu email" required />
        </label>
        <label>Competências (separadas por vírgula): 
            <input type="text" name="skills" value="${mockUser.skills}" placeholder="Ex: Python, React, Inglês fluente" required />
        </label>
        <label>Descrição Pessoal: 
            <textarea name="description" placeholder="Fale um pouco sobre você" required>${mockUser.description}</textarea>
        </label>
        <fieldset>
            <legend>Formação Acadêmica</legend>
            <label>Curso - Instituição 
                <input type="text" name="formationInstitution" value="${mockUser.formation}" placeholder="Ex: Ciência da Computação - UFSP" required />
            </label>
            <div class="inline-group date">
                <label>Data de Início: 
                    <input type="date" name="start_date" value="${mockUser.start_date}" />
                </label>
                <label>Data de Fim: 
                    <input type="date" name="end_date" value="${mockUser.end_date}" />
                </label>
            </div>
        </fieldset>
        <label>CPF: 
            <input type="text" name="cpf" value="${mockUser.cpf}" placeholder="000.000.000-00" required />
        </label>
        <div class="inline-group">
            <label>CEP: 
                <input type="text" name="cep" value="${mockUser.cep}" placeholder="00000-000" required />
            </label>
            <label>País: 
                <input type="text" name="country" value="${mockUser.country}" placeholder="Digite seu país" required />
            </label>
            <label>Estado: 
                <input type="text" name="state" value="${mockUser.state}" placeholder="Digite seu estado" required />
            </label>
        </div>
        <div class="form-actions">
            <button class="submit" type="submit">${nameButtonSubmit}</button>
        </div>
    `;

    form.addEventListener("submit", (event) => {
        event.preventDefault();

        // Capturar os dados brutos do formulário
        const formData = new FormData(form);
        const formValues: Record<string, string> = {};

        formData.forEach((value, key) => {
            formValues[key] = value.toString();
        });

        // Converter para o formato da interface Candidate
        const candidate: Candidate = {
            id: Math.floor(Math.random() * 10000), // ID aleatório para demo
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
            isMatched: false
        };

        // Salvar os dados formatados no localStorage
        localStorage.setItem("candidateData", JSON.stringify(candidate));

        // Salvar dados extras que não estão na interface Candidate mas que você quer manter
        const extraData = {
            cep: formValues.cep,
            country: formValues.country,
            state: formValues.state
        };
        localStorage.setItem("candidateExtraData", JSON.stringify(extraData));

        // Atualizar a URL sem recarregar a página
        history.pushState({}, "", "perfilCandidato");

        renderPageProfileCandidate();
    });

    return form;
}
