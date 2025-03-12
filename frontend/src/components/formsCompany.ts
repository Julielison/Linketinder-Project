import renderNewPage from "@/pages/candidatePage";
import renderCompanyProfile from "./profileCompany";

export default function renderFormsCompany(nameButtonSubmit: string): HTMLFormElement {
    const form = document.createElement("form");
    form.id = "form";
  
    form.innerHTML = /*html*/`
          <label class="name company">Nome da Empresa: 
              <input type="text" name="company_name" placeholder="Digite o nome da empresa" required />
          </label>
          <label class="email">Email: 
              <input type="email" name="email" placeholder="Digite o email da empresa" required />
          </label>
          <label>Competências (separadas por vírgula): 
              <input type="text" name="skills" placeholder="Ex: JavaScript, Marketing, Design" required />
          </label>
          <label>Descrição da Empresa: 
              <textarea name="company_description" placeholder="Descreva a empresa brevemente" required></textarea>
          </label>
          <div class="inline-group-company">
              <label>CNPJ: 
                  <input type="text" name="cnpj" placeholder="00.000.000/0000-00" required />
              </label>
              <label>CEP: 
                  <input type="text" name="cep" placeholder="00000-000" required />
              </label>
          </div>
          <div class="inline-group-company">
              <label>País: 
                  <input type="text" name="country" placeholder="Digite o país" required />
              </label>
              <label>Estado: 
                  <input type="text" name="state" placeholder="Digite o estado" required />
              </label>
          </div>
          <div class="form-actions">
              <button class="submit" type="submit">${nameButtonSubmit}</button>
          </div>
    `;
  
    // Mock para autopreencher os dados
    const mockData = {
        company_name: "Tech Solutions Ltda.",
        email: "contato@techsolutions.com.br",
        skills: "JavaScript, Node.js, React, Marketing, Design",
        company_description: "A Tech Solutions é uma empresa de tecnologia especializada no desenvolvimento de soluções digitais.",
        cnpj: "12.345.678/0001-99",
        cep: "12345-678",
        country: "Brasil",
        state: "São Paulo"
    };

    // Preenchendo os campos com os dados do mock
    const companyNameInput = form.querySelector('[name="company_name"]') as HTMLInputElement;
    if (companyNameInput) companyNameInput.value = mockData.company_name;

    const emailInput = form.querySelector('[name="email"]') as HTMLInputElement;
    if (emailInput) emailInput.value = mockData.email;

    const skillsInput = form.querySelector('[name="skills"]') as HTMLInputElement;
    if (skillsInput) skillsInput.value = mockData.skills;

    const companyDescriptionInput = form.querySelector('[name="company_description"]') as HTMLTextAreaElement;
    if (companyDescriptionInput) companyDescriptionInput.value = mockData.company_description;

    const cnpjInput = form.querySelector('[name="cnpj"]') as HTMLInputElement;
    if (cnpjInput) cnpjInput.value = mockData.cnpj;

    const cepInput = form.querySelector('[name="cep"]') as HTMLInputElement;
    if (cepInput) cepInput.value = mockData.cep;

    const countryInput = form.querySelector('[name="country"]') as HTMLInputElement;
    if (countryInput) countryInput.value = mockData.country;

    const stateInput = form.querySelector('[name="state"]') as HTMLInputElement;
    if (stateInput) stateInput.value = mockData.state;

    form.addEventListener("submit", (event) => {
        event.preventDefault();

        // Capturar os dados do formulário
        const formData = new FormData(form);
        const userData: Record<string, string> = {};

        formData.forEach((value, key) => {
            userData[key] = value.toString();
        });

        // Salvar os dados no localStorage
        localStorage.setItem("userData", JSON.stringify(userData));

        // Atualizar a URL sem recarregar a página
        history.pushState({}, "", "perfilEmpresa");

        const mainContent: HTMLElement = renderCompanyProfile();

        renderNewPage(mainContent);
    });
  
    return form;
}
