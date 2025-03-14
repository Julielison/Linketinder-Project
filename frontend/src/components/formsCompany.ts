import { Company } from "@/interfaces/ICompany";
import renderCompanyPage from "@/pages/companyPage";
import { mockCompanies } from "@/dataMocked/companiesMock";

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
  
    form.addEventListener("submit", (event) => {
        event.preventDefault();

        // Capturar os dados do formulário
        const formData = new FormData(form);
        const companyData: Record<string, string> = {};

        formData.forEach((value, key) => {
            companyData[key] = value.toString();
        });

        const company: Company = {
            id: Date.now(), // Gerar um ID único
            name: companyData.company_name,
            email: companyData.email,
            cnpj: companyData.cnpj,
            cep: companyData.cep,
            expectedSkills: companyData.skills.split(",").map((skill) => skill.trim()),
            state: companyData.state,
            country: companyData.country,
            description: companyData.company_description,
            vacancies: [],
        };

        mockCompanies.push(company);

        renderCompanyPage();
    });
  
    return form;
}
